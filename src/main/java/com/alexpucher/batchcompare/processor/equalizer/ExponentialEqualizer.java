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
 * Exponential equalizer with base e. Projects the result of an exponential
 * limit function onto the output range:<br />
 * <code>f(x) = (1 - e ^ -x) * O_range</code><br />
 * NOTE: suggested input range is [0, inf+)
 * NOTE: setup is not required
 * 
 * @author Alexander Pucher
 * 
 */
public class ExponentialEqualizer implements Equalizer {

    protected int outputRange;

    /**
     * Create {@link ExponentialEqualizer} instance with given output range.
     * 
     * @param outputRange
     */
    public ExponentialEqualizer(int outputRange) {
        super();
        this.outputRange = outputRange;
    }

    @Override
    public int equalize(int value) {
        return (int) ((1.0d - Math.exp((double) -value))
                * (double) this.outputRange);
    }

    @Override
    public void setup(int[] values) {
        // left blank
    }

    public int getOutputRange() {
        return outputRange;
    }

    public void setOutputRange(int outputRange) {
        this.outputRange = outputRange;
    }

}
