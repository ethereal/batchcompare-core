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
package com.alexpucher.batchcompare.processor.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;

/**
 * Filters out any element not placed in the first group of x elements in an
 * ordered ranking.
 * 
 * @author Alexander Pucher
 * 
 */
public class CountFilter implements Processor {

    int count;

    /**
     * Create {@link CountFilter} instance with given tuple count.
     * 
     * @param count
     *            maximum tuple count
     */
    public CountFilter(int count) {
        super();
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.Processor#execute(java.util.Collection)
     */
    @Override
    public Collection<Pair> execute(Collection<Pair> pairs) {

        List<Pair> list = new ArrayList<Pair>(pairs);
        Collections.sort(list);

        return list.subList(0, Math.min(this.count, list.size()));
    }

}
