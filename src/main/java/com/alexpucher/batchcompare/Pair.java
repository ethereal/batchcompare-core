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

import java.util.UUID;

/**
 * A single base-candidate pair with a difference value.
 * 
 * @author Alexander Pucher
 * 
 */
public interface Pair extends Comparable<Pair> {

    /**
     * Return comparison base of the tuple.
     * 
     * @return comparison base
     */
    public abstract Object getBase();

    /**
     * Return comparison candidate of the tuple.
     * 
     * @return comparison candidate
     */
    public abstract Object getCandidate();

    /**
     * Return tuple UUID.
     * 
     * @return tuple uuid
     */
    public abstract UUID getUUID();

    /**
     * Return current difference score as set by the last processor.
     * 
     * @return difference value
     */
    public abstract int getDifference();

    /**
     * Set difference value of the tuple.
     * 
     * @param difference
     *            new difference value
     * @return pair instance (monadic)
     */
    public abstract Pair setDifference(int difference);

}
