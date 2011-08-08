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
package com.alexpucher.batchcompare;

/**
 * Equalizer for difference values. Statistical information is collected by the
 * container and handed to the equalizer. The equalizer then processes values on
 * a per-tuple basis.
 * 
 * @author Alexander Pucher
 * 
 */
public interface Equalizer {

    /**
     * Setup the equalizer based on statistical information.
     * 
     * @param values
     *            array of observed values
     */
    public abstract void setup(int[] values);

    /**
     * Normalize input value based on setup.
     * 
     * @param value
     *            observed value
     * @return normalized value
     */
    public abstract int equalize(int value);

}
