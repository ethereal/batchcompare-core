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
package com.alexpucher.batchcompare.processor.operator;

import com.alexpucher.batchcompare.Operator;
import com.alexpucher.batchcompare.Pair;

/**
 * Set the difference value of the pair to a fixed value.
 * 
 * @author Alexander Pucher
 * 
 */
public class FixedOperator implements Operator {
    private int difference;

    /**
     * Create {@link FixedOperator} instance with given difference value.
     * 
     * @param difference
     *            fixed difference
     */
    public FixedOperator(int difference) {
        super();
        this.difference = difference;
    }

    @Override
    public Pair execute(Pair pair) {
        return pair.setDifference(this.difference);
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }

}
