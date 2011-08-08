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
 * Filters out any element surpassing the x% mark in an ordered ranking.
 * 
 * @author Alexander Pucher
 * 
 */
public class PercentFilter implements Processor {

    private double percent;

    /**
     * Create PercentFilter instance with given result set percentage<br />
     * <b>INVARIANT:</b> 0.0 < percent < 1.0
     * 
     * @param percent
     *            percentage of results kept
     */
    public PercentFilter(double percent) {
        super();
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    /**
     * Percentile of individual results let through.<br />
     * <b>INVARIANT:</b> 0.0 < percentile < 1.0
     * 
     * @param percent
     */
    public void setPercent(double percent) {
        this.percent = percent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.Processor#execute(java.util.Collection)
     */
    @Override
    public Collection<Pair> execute(Collection<Pair> pairs) {
        int numElements = (int) Math.ceil(pairs.size() * this.percent);

        List<Pair> list = new ArrayList<Pair>(pairs);
        Collections.sort(list);

        return list.subList(0, numElements);
    }

}
