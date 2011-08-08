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
 * Skeleton implementation of Task interface. Handles parent relation and
 * external flow of data using {@link Task#pull()} and
 * {@link Task#push(Collection)}.<br />
 * NOTE: Implementation of {@link AbstractTask#executeImpl()} required.
 * 
 * @author Alexander Pucher
 * 
 */
public abstract class AbstractTask implements Task {

    protected Collection<Pair> data;
    private Task parent;

    public AbstractTask() {
        super();
    }

    /**
     * Execute internal Task logic. Use {@link AbstractTask#data} to access
     * local data. The field contents are used as output after completion of the
     * invocation.
     */
    protected abstract void executeImpl();

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.query.Task#execute()
     */
    @Override
    final public void execute() {
        if (this.parent != null) {
            this.data = this.parent.pull();
        }

        this.executeImpl();

        if (this.parent != null) {
            this.parent.push(this.data);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.query.Task#getParent()
     */
    @Override
    public Task getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}<br />
     * NOTE: also performs a rudimentary check for cyclic dependencies. Do NOT
     * depend on this however, it's a convenience feature for debugging.
     */
    @Override
    public void setParent(Task parent) {
        if (findParentCycle(parent)) {
            throw new IllegalArgumentException("Circular dependency detected.");
        }
        this.parent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.query.Task#push(java.util.Collection)
     */
    @Override
    public void push(Collection<Pair> data) {
        this.data = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.query.Task#pull()
     */
    @Override
    public Collection<Pair> pull() {
        return this.data;
    }

    /**
     * Bottom to top tree traversal looking for cyclic dependencies. Traverses a
     * single path only and does not support forking in generic directed graphs.
     * 
     * @param parent
     *            parent task
     * @return <code>true</code> if a cyclic dependency is found
     */
    private boolean findParentCycle(Task parent) {
        Task query = parent;

        while (query != null) {
            if (query.getParent() == this) {
                return true;
            }
            query = query.getParent();
        }

        return false;
    }

}