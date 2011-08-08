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
 * Linear equalizer. Determines minimum offset and range of input value and
 * projects these onto output value with given offset and range:<br />
 * <code>I_offset = min(inputs), I_range = max(inputs) - I_offset</code><br />
 * <code>f(x) = (x - I_offset) / I_range * O_range + O_offset</cdoe>
 * 
 * @author Alexander Pucher
 * 
 */
public class LinearEqualizer implements Equalizer {

    protected int outputOffset;
    protected int outputRange;
    protected int inputOffset;
    protected int inputRange;

    /**
     * Create {@link LinearEqualizer} instance with given output value offset
     * and range.<br />
     * NOTE: min = offset, max = offset + range
     * 
     * @param outputRange
     *            output value range starting from offset
     * @param outputOffset
     *            output value offset from zero
     */
    public LinearEqualizer(int outputRange, int outputOffset) {
        super();
        this.outputOffset = outputOffset;
        this.outputRange = outputRange;
    }

    /**
     * Create {@link LinearEqualizer} instance with given output value range.<br />
     * NOTE: min = 0; max = range
     * 
     * @param outputRange
     *            output value range starting from zero
     */
    public LinearEqualizer(int outputRange) {
        this(outputRange, 0);
    }

    @Override
    public int equalize(int value) {
        return (int) (((double) (value - this.inputOffset))
                / (double) this.inputRange * (double) this.outputRange) + this.outputOffset;
    }

    @Override
    public void setup(int[] values) {
        int min = 0;
        int max = 0;

        for (int value : values) {
            if (value < min)
                min = value;
            if (value > max)
                max = value;
        }

        this.inputOffset = min;
        this.inputRange = max - min;
    }

    public int getOutputOffset() {
        return outputOffset;
    }

    public void setOutputOffset(int outputOffset) {
        this.outputOffset = outputOffset;
    }

    public int getOutputRange() {
        return outputRange;
    }

    public void setOutputRange(int outputRange) {
        this.outputRange = outputRange;
    }

}
