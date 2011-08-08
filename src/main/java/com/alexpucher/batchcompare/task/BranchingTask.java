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

import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;

/**
 * Task wrapper that splits up processing based on a filter decision. Tuples
 * matched by the filter are processed by the matching-task the remaining tuples
 * by the remainder-task. After both tasks complete, the results are merged
 * together.
 * 
 * @author Alexander Pucher
 * 
 */
public class BranchingTask extends AbstractTask {
    private Task taskMatch;
    private Task taskRemainder;

    private Processor filter;

    /**
     * Create {@link BranchingTask} instance with given filter.
     * 
     * @param filter
     *            decision filter
     */
    public BranchingTask(Processor filter) {
        super();
        this.filter = filter;
    }

    /**
     * Create {@link BranchingTask} instance with given Filter and alternative
     * tasks.
     * 
     * @param taskMatch
     *            task executed on data set matching the filter
     * @param taskRemainder
     *            task executed on non-matching data set
     * @param filter
     *            decision filter
     */
    public BranchingTask(Task taskMatch, Task taskRemainder, Processor filter) {
        super();
        this.taskMatch = taskMatch;
        this.taskRemainder = taskRemainder;
        this.filter = filter;
    }

    @Override
    protected void executeImpl() {
        Collection<Pair> dataMatch = new ArrayList<Pair>(this.data);
        Collection<Pair> dataRemainder = new ArrayList<Pair>(this.data);

        dataMatch = this.filter.execute(dataMatch);
        dataRemainder.removeAll(dataMatch);

        this.data = new ArrayList<Pair>();
        this.data.addAll(TaskUtils.runTask(this.taskMatch, dataMatch));
        this.data.addAll(TaskUtils.runTask(this.taskRemainder, dataRemainder));

    }

    public Task getTaskMatch() {
        return taskMatch;
    }

    public BranchingTask setTaskMatch(Task taskMatch) {
        this.taskMatch = taskMatch;
        return this;
    }

    public Task getTaskRemainder() {
        return taskRemainder;
    }

    public BranchingTask setTaskRemainder(Task taskRemainder) {
        this.taskRemainder = taskRemainder;
        return this;
    }

    public Processor getFilter() {
        return filter;
    }

    public void setFilter(Processor filter) {
        this.filter = filter;
    }

}
