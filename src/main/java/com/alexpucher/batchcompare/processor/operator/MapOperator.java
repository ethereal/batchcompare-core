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
package com.alexpucher.batchcompare.processor.operator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.alexpucher.batchcompare.Operator;
import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.processor.BatchOperator;
import com.alexpucher.batchcompare.processor.Generator;
import com.alexpucher.batchcompare.task.ProcessorTask;
import com.alexpucher.batchcompare.task.Task;
import com.alexpucher.batchcompare.task.TaskUtils;

/**
 * Unwraps map container by running a task for each base map entry on the cross
 * product of this entry with the candidate map entries. The total difference
 * equals the sum of the lowest result of each task run.<br />
 * <code>difference = SUM(i = [0, N]: min(task(base_entry[i] x candidate_entries))</code>
 * 
 * @author Alexander Pucher
 * 
 */
public class MapOperator implements Operator {
    
    public static final int defaultKeyPenalty = 100;
    public static final int defaultValuePenalty = 1;

    private Task task;

    /**
     * Create {@link MapOperator} instance with given task to be run on
     * entry-sets
     * 
     * @param task
     *            map entry task
     */
    public MapOperator(Task task) {
        super();
        this.task = task;
    }

    /**
     * Create {@link MapOperator} instance with default task based on given
     * {@link AbstractMapEntryOperator} entry-sets.
     * 
     * @param entryOperator
     *            map entry operator for default task
     */
    public MapOperator(AbstractMapEntryOperator entryOperator) {
        super();
        this.task = new DefaultTask(entryOperator);
    }

    @Override
    public Pair execute(Pair pair) {
        Map<?, ?> base = (Map<?, ?>) pair.getBase();
        Map<?, ?> candidate = (Map<?, ?>) pair.getCandidate();
        
        if(base.isEmpty()) {
            pair.setDifference(0);
            return pair;
        }
        
        if(candidate.isEmpty()) {
            pair.setDifference(base.size() * defaultKeyPenalty);
            return pair;
        }

        int diffValue = 0;

        for (Map.Entry<?, ?> entry : base.entrySet()) {
            Collection<Pair> data = Generator.generate(entry, candidate
                    .entrySet());
            Collection<Pair> results = TaskUtils.runTask(this.task, data);

            if (!results.isEmpty()) {
                Pair min = Collections.min(results);
                diffValue += min.getDifference();
            }
        }

        pair.setDifference(diffValue);

        return pair;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Use default task on entry sets.
     */
    public void setTaskDefault(AbstractMapEntryOperator entryOperator) {
        this.task = new DefaultTask(entryOperator);
    }

    /**
     * ProcessorTask wrapper for arbitrary {@link AbstractMapEntryOperator}.
     * 
     * @author Alexander Pucher
     *
     */
    private static class DefaultTask extends ProcessorTask {

        private DefaultTask(AbstractMapEntryOperator entryOperator) {
            super(new BatchOperator(entryOperator));
        }

    }

}
