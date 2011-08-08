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
package com.alexpucher.batchcompare.processor.equalizer;

import com.alexpucher.batchcompare.Equalizer;

/**
 * Binary equalizer based on mean value. Determines the statistical mean value
 * as threshold during setup. Outputs high or low value for tuples based on
 * whether they surpass the threshold or not.
 * 
 * @author Alexander Pucher
 * 
 */
public class BinaryEqualizer implements Equalizer {
    private int inputThreshold;
    private int outputLow;
    private int outputHigh;

    /**
     * Create {@link BinaryEqualizer} instance with given low and high output
     * values.
     * 
     * @param outputLow output for values or below mean
     * @param outputHigh output for values equal to or above mean
     */
    public BinaryEqualizer(int outputLow, int outputHigh) {
        super();
        this.outputLow = outputLow;
        this.outputHigh = outputHigh;
    }

    @Override
    public int equalize(int value) {
        return (value >= this.inputThreshold) ? this.outputHigh : this.outputLow;
    }

    @Override
    public void setup(int[] values) {
        int sum = 0;

        for (int value : values) {
            sum += value;
        }

        // NOTE: rounding up
        this.inputThreshold = (int)Math.ceil((double)sum / (double)values.length);
    }

}
