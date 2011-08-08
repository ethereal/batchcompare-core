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
package com.alexpucher.batchcompare.processor;

import java.util.Collection;

import com.alexpucher.batchcompare.Equalizer;
import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;

/**
 * Executes score equalizer functions on collections of pairs. Typically used to
 * normalize Operator results. (i.e. in aggregation tasks)<br />
 * NOTE: This class acts as Processor wrapper for Equalizers
 * 
 * @author Alexander Pucher
 * 
 */
public class BatchEqualizer implements Processor {

    private Equalizer equalizer;

    /**
     * Create {@link BatchEqualizer} instace with given equalizer
     * 
     * @param equalizer
     *            equalizer function
     */
    public BatchEqualizer(Equalizer equalizer) {
        super();
        this.equalizer = equalizer;
    }

    @Override
    public Collection<Pair> execute(Collection<Pair> pairs) {
        int[] values = new int[pairs.size()];
        int counter = 0;

        for (Pair pair : pairs) {
            values[counter++] = pair.getDifference();
        }

        this.equalizer.setup(values);

        for (Pair pair : pairs) {
            pair.setDifference(this.equalizer.equalize(pair.getDifference()));
        }

        return pairs;
    }

    public Equalizer getEqualizer() {
        return equalizer;
    }

    public void setEqualizer(Equalizer equalizer) {
        this.equalizer = equalizer;
    }

}
