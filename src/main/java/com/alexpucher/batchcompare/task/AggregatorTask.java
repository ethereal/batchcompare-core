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
import java.util.HashMap;
import java.util.Map;

import com.alexpucher.batchcompare.Pair;

/**
 * Task wrapper that runs multiple Tasks on the same input set and merges the
 * results by adding scores on a per-tuple basis.
 * 
 * @author Alexander Pucher
 * 
 */
public class AggregatorTask extends AbstractTask {

    private Collection<Task> tasks;

    /**
     * Create {@link AggregatorTask} with empty task set.
     */
    public AggregatorTask() {
        super();
        this.tasks = new ArrayList<Task>();
    }

    @Override
    protected void executeImpl() {
        Collection<Pair> inputCopy = TaskUtils.copyData(this.data);
        Map<Integer, Pair> inputLookup = new HashMap<Integer, Pair>();

        for (Pair pair : this.data) {
            pair.setDifference(0);
            inputLookup.put(pair.hashCode(), pair);
        }

        for (Task task : this.tasks) {
            Collection<Pair> data = TaskUtils.copyData(inputCopy);
            Collection<Pair> results = TaskUtils.runTask(task, data);

            for (Pair result : results) {
                Pair pair = inputLookup.get(result.hashCode());

                pair.setDifference(pair.getDifference()
                        + result.getDifference());
            }
        }
    }

    /**
     * Add task to aggregation set.<br />
     * NOTE: adding equalizers to each processing path simplifies weighting
     * scores.
     * 
     * @param task
     *            processing task
     * @return Aggregator instance (monadic)
     */
    public AggregatorTask addTask(Task task) {
        task.setParent(this);

        this.tasks.add(task);
        return this;
    }

}
