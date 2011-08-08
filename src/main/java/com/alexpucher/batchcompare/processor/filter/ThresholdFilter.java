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

import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;

/**
 * Filters out any element surpassing the threshold value.
 * 
 * @author Alexander Pucher
 * 
 */
public class ThresholdFilter implements Processor {

    private int threshold;

    /**
     * Create {@link ThresholdFilter} instance with given threshold value.
     * 
     * @param threshold
     */
    public ThresholdFilter(int threshold) {
        super();
        this.threshold = threshold;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.alexpucher.batchcompare.Processor#execute(java.util.Collection)
     */
    @Override
    public Collection<Pair> execute(Collection<Pair> pairs) {

        Collection<Pair> output = new ArrayList<Pair>();

        for (Pair pair : pairs) {
            if (pair.getDifference() <= this.threshold) {
                output.add(pair);
            }
        }

        return output;
    }

}
