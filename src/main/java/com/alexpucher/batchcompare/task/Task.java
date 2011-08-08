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

import java.util.Collection;

import com.alexpucher.batchcompare.Pair;

/**
 * Task representing an arbitrary processing instructions.<br />
 * NOTE: Strategy pattern
 * 
 * @author Alexander Pucher
 * 
 */
public interface Task {

    /**
     * Execute task. Typically, the implementation with call
     * {@link SerialTask#pull()} to obtain the current data set, operate on it
     * and {@link SerialTask#push()} back an updated instance.
     */
    public abstract void execute();

    /**
     * Return task's parent query.
     * 
     * @return parent query
     */
    public abstract Task getParent();

    /**
     * Set task parent. Called by Query class when task is attached to it.
     * 
     * @param parent
     *            parent query
     */
    public abstract void setParent(Task parent);

    /**
     * Push data to the task to operate on. Typically called by a sub-task in
     * preparation of {@link Task#execute()}. Also used to fill in top level
     * data set.
     * 
     * @param data
     *            input data
     */
    public void push(Collection<Pair> data);

    /**
     * Pull data from the task. Typically called by a sub-task during completion
     * of {@link Task#execute()}. Also used to obtain top level result.
     * 
     * @return output data
     */
    public Collection<Pair> pull();

}
