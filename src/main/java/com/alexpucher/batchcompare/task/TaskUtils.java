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
package com.alexpucher.batchcompare.task;

import java.util.ArrayList;
import java.util.Collection;

import com.alexpucher.batchcompare.Equalizer;
import com.alexpucher.batchcompare.Operator;
import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;
import com.alexpucher.batchcompare.processor.BatchEqualizer;
import com.alexpucher.batchcompare.processor.BatchOperator;
import com.alexpucher.batchcompare.processor.PairImpl;
import com.alexpucher.batchcompare.processor.operator.AbstractMapEntryOperator;
import com.alexpucher.batchcompare.processor.operator.MapOperator;

/**
 * Utilities and convenience functions for Task programming.
 * 
 * @author Alexander Pucher
 * 
 */
public class TaskUtils {

    private TaskUtils() {
        // left blank
    }

    /**
     * Create a Tuple-level copy of the input data set.
     * 
     * @param data
     *            collection of tuples
     * @return independent copy of data set
     */
    public static Collection<Pair> copyData(Collection<Pair> data) {
        Collection<Pair> copy = new ArrayList<Pair>(data.size());

        for (Pair pair : data) {
            copy.add(new PairImpl(pair));
        }

        return copy;
    }

    /**
     * Setup and execute Task with given data set.
     * 
     * @param task
     *            Task
     * @param data
     *            collection of tuples
     * @return result data set
     */
    public static Collection<Pair> runTask(Task task, Collection<Pair> data) {
        TaskWrapper wrapper = new TaskWrapper(task);
        wrapper.push(data);
        wrapper.execute();
        return wrapper.pull();
    }
    
    /**
     * Create ProcessorTask wrapper for given processor.
     * 
     * @param processor {@link Processor} implementation
     * @return
     */
    public static ProcessorTask createTask(Processor processor) {
        return new ProcessorTask(processor);
    }

    /**
     * Create ProcessorTask & BatchOperator wrapper for given operator.
     * 
     * @param operator {@link Operator} implementation
     * @return
     */
    public static ProcessorTask createTask(Operator operator) {
        return new ProcessorTask(new BatchOperator(operator));
    }
    
    /**
     * Create ProcessorTask & BatchEqualizer wrapper for given equalizer.
     * 
     * @param equalizer {@link Equalizer} implementation
     * @return
     */
    public static ProcessorTask createTask(Equalizer equalizer) {
        return new ProcessorTask(new BatchEqualizer(equalizer));
    }

    /**
     * Create ProcessorTask & BatchOperator & MapOperator wrapper for given mapOperator.
     * 
     * @param mapOperator {@link AbstractMapEntryOperator} implementation
     * @return
     */
    public static ProcessorTask createTask(AbstractMapEntryOperator mapOperator) {
        return new ProcessorTask(new BatchOperator(new MapOperator(mapOperator)));
    }

    /**
     * Helper class for {@link TaskUtils#runTask(Task, Collection)}.
     * 
     * @author Alexander Pucher
     * 
     */
    private static class TaskWrapper extends AbstractTask {
        private Task task;

        private TaskWrapper(Task task) {
            super();
            this.task = task;
            this.task.setParent(this);
        }

        @Override
        protected void executeImpl() {
            task.execute();
        }

    }

}
