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

import java.util.LinkedList;
import java.util.Queue;


/**
 * A SerialTask represents a series of processing instructions, which may be
 * arbitrary Tasks or SerialTasks themselves.
 * 
 * @author Alexander Pucher
 * 
 */
public class SerialTask extends AbstractTask {

    private Queue<Task> tasks;
    /**
     * Create {@link SerialTask} instance with empty task queue.
     */
    public SerialTask() {
        super();
        this.tasks = new LinkedList<Task>();
    }

    @Override
    public void executeImpl() {
        for (Task task : this.tasks) {
            task.execute();
        }
    }

    /**
     * Add task to processing queue.<br />
     * NOTE: tasks are NOT consumed when executing the query.
     * 
     * @param task
     *            processing task
     * @return Query instance (monadic)
     */
    public SerialTask addTask(Task task) {
        task.setParent(this);

        this.tasks.add(task);
        return this;
    }

}
