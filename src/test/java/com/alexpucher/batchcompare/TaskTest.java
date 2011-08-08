/**
 *    Copyright 2011 Alexander Pucher
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.alexpucher.batchcompare;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alexpucher.batchcompare.processor.BatchOperator;
import com.alexpucher.batchcompare.processor.Generator;
import com.alexpucher.batchcompare.processor.PairImpl;
import com.alexpucher.batchcompare.processor.equalizer.LinearEqualizer;
import com.alexpucher.batchcompare.processor.filter.BlockadeFilter;
import com.alexpucher.batchcompare.processor.filter.CountFilter;
import com.alexpucher.batchcompare.processor.filter.IdentityFilter;
import com.alexpucher.batchcompare.processor.operator.FixedOperator;
import com.alexpucher.batchcompare.processor.operator.OffsetOperator;
import com.alexpucher.batchcompare.task.AbstractTask;
import com.alexpucher.batchcompare.task.AggregatorTask;
import com.alexpucher.batchcompare.task.BranchingTask;
import com.alexpucher.batchcompare.task.ProcessorTask;
import com.alexpucher.batchcompare.task.SerialTask;
import com.alexpucher.batchcompare.task.Task;
import com.alexpucher.batchcompare.task.TaskUtils;

public class TaskTest {
    
    private static int testTaskCounter;

    private SerialTask serial;
    private Collection<Pair> input;
    private Collection<Pair> output;

    @Before
    public void setUp() throws Exception {
        testTaskCounter = 0;

        serial = new SerialTask();
        input = Generator.generate("base", Arrays.asList(new String[]{"c1", "c2", "c3", "c4", "c5"}));
        output = null;
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void circularDependencyTest() {
        SerialTask child = new SerialTask();
        SerialTask grandChild = new SerialTask();
        
        serial.addTask(child);
        child.addTask(grandChild);
        grandChild.addTask(serial);
    }
    
    @Test
    public void singleTaskTest() {
        serial.addTask(new TestTask());
        serial.execute();
        
        assertEquals(1, testTaskCounter);
    }
    
    @Test
    public void multiTaskTest() {
        serial.addTask(new TestTask());
        serial.addTask(new TestTask());
        serial.addTask(new TestTask());
        serial.execute();
        
        assertEquals(3, testTaskCounter);
    }
    
    @Test
    public void recursiveTaskTest() {
        SerialTask child = new SerialTask();
        SerialTask grandChild = new SerialTask();

        serial.addTask(new TestTask());
        serial.addTask(new TestTask());
        serial.addTask(child);
        child.addTask(new TestTask());
        child.addTask(new TestTask());
        child.addTask(grandChild);
        grandChild.addTask(new TestTask());
        serial.addTask(new TestTask());
        serial.execute();
        
        assertEquals(6, testTaskCounter);
    }
    
    @Test
    public void branchingTaskTest() {
        BranchingTask branch = new BranchingTask(new IdentityFilter());
        branch.setTaskMatch(new TestTask());
        branch.setTaskRemainder(new TestTask());
        
        execTask(branch);
        
        assertEquals(2, testTaskCounter);
    }
    
    @Test
    public void branchingTaskDataConsistencyTest() {
        BranchingTask branch = new BranchingTask(new CountFilter(1));
        branch.setTaskMatch(new ProcessorTask(new IdentityFilter()));
        branch.setTaskRemainder(new ProcessorTask(new IdentityFilter()));
        
        execTask(branch);
        
        assertEquals("number of items", this.input.size(), this.output.size());
    }
    
    @Test
    public void branchingTaskMatchTest() {
        BranchingTask branch = new BranchingTask(new CountFilter(1));
        branch.setTaskMatch(new ProcessorTask(new IdentityFilter()));
        branch.setTaskRemainder(new ProcessorTask(new BlockadeFilter()));
        
        execTask(branch);
        
        assertEquals("number of items", 1, this.output.size());
    }
    
    @Test
    public void branchingTaskRemainderTest() {
        BranchingTask branch = new BranchingTask(new CountFilter(1));
        branch.setTaskMatch(new ProcessorTask(new BlockadeFilter()));
        branch.setTaskRemainder(new ProcessorTask(new IdentityFilter()));
        
        execTask(branch);
        
        assertEquals("number of items", this.input.size() - 1, this.output.size());
    }
    
    @Test
    public void branchingTaskRecursiveTest() {
        BranchingTask branch = new BranchingTask(new CountFilter(2));
        BranchingTask branchA = new BranchingTask(new CountFilter(1));
        BranchingTask branchB = new BranchingTask(new CountFilter(1));
        
        branch.setTaskMatch(branchA);
        branch.setTaskRemainder(branchB);
        
        branchA.setTaskMatch(new ProcessorTask(new BlockadeFilter()));
        branchA.setTaskRemainder(new ProcessorTask(new IdentityFilter()));

        branchB.setTaskMatch(new ProcessorTask(new IdentityFilter()));
        branchB.setTaskRemainder(new ProcessorTask(new BlockadeFilter()));
        
        execTask(branch);
        
        assertEquals("number of items", 2, this.output.size());
    }
    
    @Test
    public void aggregatorNoneTest() {
        AggregatorTask aggregator = new AggregatorTask();
        
        execTask(aggregator);
        
        assertEquals("number of items", this.input.size(), this.output.size());
        
        for(Pair pair : this.output) {
            assertEquals("score set to zero", 0, pair.getDifference());
        }
    }
    
    @Test
    public void aggregatorTest() {
        AggregatorTask aggregator = new AggregatorTask();
        aggregator.addTask(new ProcessorTask(new BatchOperator(new FixedOperator(1))));
        aggregator.addTask(new ProcessorTask(new BatchOperator(new OffsetOperator(2))));
        
        execTask(aggregator);
        
        assertEquals("number of items", this.input.size(), this.output.size());
        
        for(Pair pair : this.output) {
            assertEquals(3, pair.getDifference());
        }
    }
    
    @Test
    public void aggregatorEqualizedTest() {
        SerialTask equalizedTest = new SerialTask();
        equalizedTest.addTask(TaskUtils.createTask(new TestOperator()));
        equalizedTest.addTask(TaskUtils.createTask(new LinearEqualizer(8)));
        
        AggregatorTask aggregator = new AggregatorTask();
        aggregator.addTask(equalizedTest);
        aggregator.addTask(TaskUtils.createTask(new OffsetOperator(2)));
        
        execTask(aggregator);
        
        assertEquals("number of items", this.input.size(), this.output.size());
        assertEquals("minimum score", 2, Collections.min(this.output).getDifference());
        assertEquals("maximum score", 10, Collections.max(this.output).getDifference());
    }
    
    @Test
    public void aggregatorSimilarTuplesTest() {
        Pair pairInput = this.input.iterator().next();
        Pair pairAdd = new PairImpl(pairInput.getBase(), pairInput.getCandidate());
        this.input.add(pairAdd);
        
        AggregatorTask aggregator = new AggregatorTask();
        aggregator.addTask(TaskUtils.createTask(new FixedOperator(1)));
        
        execTask(aggregator);
        
        assertEquals("number of items", this.input.size(), this.output.size());
        assertEquals("minimum score", 1, Collections.min(this.output).getDifference());
        assertEquals("maximum score", 1, Collections.max(this.output).getDifference());
    }
    
    private void execTask(Task task) {
        task.push(this.input);
        task.execute();
        this.output = task.pull();
    }
    
    private class TestTask extends AbstractTask {
        @Override
        public void executeImpl() {
            ++testTaskCounter;
        }
    }
    
    private class TestOperator implements Operator {
        private int counter = 0;

        @Override
        public Pair execute(Pair pair) {
            pair.setDifference(this.counter++);
            return pair;
        }
        
    }

}
