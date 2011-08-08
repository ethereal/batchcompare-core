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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alexpucher.batchcompare.processor.BatchOperator;
import com.alexpucher.batchcompare.processor.Generator;
import com.alexpucher.batchcompare.processor.PairImpl;
import com.alexpucher.batchcompare.processor.filter.PercentFilter;
import com.alexpucher.batchcompare.processor.operator.MapKeyOperator;
import com.alexpucher.batchcompare.processor.operator.MapKeyValueOperator;
import com.alexpucher.batchcompare.processor.operator.MapOperator;
import com.alexpucher.batchcompare.task.ProcessorTask;
import com.alexpucher.batchcompare.task.SerialTask;

public class ProcessorTest {
    
    private final static String TEST_KEY1 = "key";
    private final static String TEST_KEY2 = "key2";
    private final static String TEST_VALUE1 = "value";
    private final static String TEST_VALUE1_DIFFERENT = "valueDifferent";
    private final static String TEST_VALUE2 = "value2";
    
    private static final int keyPenalty = MapOperator.defaultKeyPenalty;
    private static final int valuePenalty = MapOperator.defaultValuePenalty;
    
    private Map<String, String> emptyMap;
    private Map<String, String> singleMap;
    private Map<String, String> multiMap;
    
    private Operator keyOp;
    private Operator keyValueOp;
    
    private Collection<Map<String, String>> inputSet;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        emptyMap = Collections.emptyMap();
        singleMap = Collections.singletonMap(TEST_KEY1, TEST_VALUE1);
        
        multiMap = new HashMap<String, String>();
        multiMap.put(TEST_KEY1, TEST_VALUE1_DIFFERENT);
        multiMap.put(TEST_KEY2, TEST_VALUE2);
        
        keyOp = new MapOperator(new MapKeyOperator());
        keyValueOp = new MapOperator(new MapKeyValueOperator());
        
        inputSet = Arrays.asList(emptyMap, singleMap, multiMap);
    }

    @After
    public void tearDown() throws Exception {

    }
    
    @Test
    public void keyOpEqualTest() {
        assertEquals("empty maps equal", 0, keyOp.execute(makePair(emptyMap, emptyMap)).getDifference());
        assertEquals("single map equal", 0, keyOp.execute(makePair(singleMap, singleMap)).getDifference());
        assertEquals("multi map equal", 0, keyOp.execute(makePair(multiMap, multiMap)).getDifference());
    }

    @Test
    public void keyOpEmptySingleTest() {
        assertEquals("empty-single", 0, keyOp.execute(makePair(emptyMap, singleMap)).getDifference());
        assertEquals("single-empty", keyPenalty, keyOp.execute(makePair(singleMap, emptyMap)).getDifference());
    }

    @Test
    public void keyOpEmptyMultiTest() {
        assertEquals("empty-multi", 0, keyOp.execute(makePair(emptyMap, multiMap)).getDifference());
        assertEquals("multi-empty", 2 * keyPenalty, keyOp.execute(makePair(multiMap, emptyMap)).getDifference());
    }

    @Test
    public void keyOpSingleMultiTest() {
        assertEquals("single-multi", 0, keyOp.execute(makePair(singleMap, multiMap)).getDifference());
        assertEquals("multi-single", keyPenalty, keyOp.execute(makePair(multiMap, singleMap)).getDifference());
    }
    
    @Test
    public void keyValueOpEqualTest() {
        assertEquals("empty maps equal", 0, keyValueOp.execute(makePair(emptyMap, emptyMap)).getDifference());
        assertEquals("single map equal", 0, keyValueOp.execute(makePair(singleMap, singleMap)).getDifference());
        assertEquals("multi map equal", 0, keyValueOp.execute(makePair(multiMap, multiMap)).getDifference());
    }

    @Test
    public void keyValueOpEmptySingleTest() {
        assertEquals("empty-single", 0, keyValueOp.execute(makePair(emptyMap, singleMap)).getDifference());
        assertEquals("single-empty", keyPenalty, keyValueOp.execute(makePair(singleMap, emptyMap)).getDifference());
    }

    @Test
    public void keyValueOpEmptyMultiTest() {
        assertEquals("empty-multi", 0, keyValueOp.execute(makePair(emptyMap, multiMap)).getDifference());
        assertEquals("multi-empty", 2 * keyPenalty, keyValueOp.execute(makePair(multiMap, emptyMap)).getDifference());
    }

    @Test
    public void keyValueOpSingleMultiTest() {
        assertEquals("single-multi", valuePenalty, keyValueOp.execute(makePair(singleMap, multiMap)).getDifference());
        assertEquals("multi-single", keyPenalty + valuePenalty, keyValueOp.execute(makePair(multiMap, singleMap)).getDifference());
    }
    
    @Test
    public void batchOperatorTest() {
        BatchOperator eval = new BatchOperator(this.keyOp);
        
        List<Pair> results = new ArrayList<Pair>(eval.execute(Generator.generate(singleMap, inputSet)));
        
        Collections.sort(results);
        
        assertEquals("(single-single) comparison", 0, results.get(0).getDifference());
        assertEquals("(single-multi) comparison", 0, results.get(1).getDifference());
        assertEquals("(single-empty) comparison", keyPenalty, results.get(2).getDifference());
        
    }

    @SuppressWarnings("unchecked")
    @Test
    public void batchOperatorReuseTest() {
        BatchOperator eval = new BatchOperator(this.keyOp);
        
        // first comparison
        eval.execute(Generator.generate(singleMap, inputSet));
        
        Collection<Map<String, String>> newInputSet = Arrays.asList(emptyMap, multiMap);
        
        // second comparison
        List<Pair> results = new ArrayList<Pair>(eval.execute(Generator.generate(singleMap, newInputSet)));
        Collections.sort(results);
        
        assertEquals("(single-multi) comparison", 0, results.get(0).getDifference());
        assertEquals("(single-empty) comparison", keyPenalty, results.get(1).getDifference());
    }
    
    @Test
    public void querySimulatingBatchTest() {
        SerialTask query = setupQuery();
        
        query.execute();
        
        List<Pair> results = new ArrayList<Pair>(query.pull());
        
        Collections.sort(results);
        
        assertEquals("(single-single) comparison", 0, results.get(0).getDifference());
        assertEquals("(single-multi) comparison", 0, results.get(1).getDifference());
        assertEquals("(single-empty) comparison", keyPenalty, results.get(2).getDifference());
    }

    @Test
    public void queryDiscFilterTest() {
        SerialTask query = setupQuery();
        
        query.addTask(new ProcessorTask(new PercentFilter(0.33)));
        
        query.execute();
        
        List<Pair> results = new ArrayList<Pair>(query.pull());
        
        Collections.sort(results);
        
        assertEquals("number of elements", 1, results.size());
        assertEquals("(single-single) comparison", 0, results.get(0).getDifference());
    }
    
    private static Pair makePair(Object base, Object candidate) {
        return new PairImpl(base, candidate);
    }

    private SerialTask setupQuery() {
        SerialTask query = new SerialTask();
        
        query.push(Generator.generate(this.singleMap, this.inputSet));
        query.addTask(new ProcessorTask(new BatchOperator(this.keyOp)));
        
        return query;
    }
    
}
