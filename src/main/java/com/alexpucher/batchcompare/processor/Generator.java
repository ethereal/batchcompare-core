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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.alexpucher.batchcompare.Pair;

/**
 * Helper class generating collections of pairs from sets of bases and
 * candidates.
 * 
 * @author Alexander Pucher
 * 
 */
public class Generator {

    private Generator() {
        // intentionally left blank
    }

    /**
     * Generate cross product from bases and pairs. The amount of pairs
     * generated equals |bases| x |candidates|. Use with caution on large
     * datasets.
     * 
     * @param bases
     *            base objects
     * @param candidates
     *            candidate objects
     * @return complete enumeration of base-candidate pairs
     */
    public static Collection<Pair> generate(Collection<?> bases,
            Collection<?> candidates) {
        Collection<Pair> pairs = new ArrayList<Pair>(bases.size()
                * candidates.size());

        for (Object base : bases) {
            for (Object candidate : candidates) {
                pairs.add(new PairImpl(base, candidate));
            }
        }

        return pairs;
    }

    /**
     * Generate a full set of pair for a single base and multiple candidates.
     * The amount of pairs generated equals |candidates|.
     * 
     * @param base
     *            base object
     * @param candidates
     *            candidate objects
     * @return complete enumeration of base-candidate pairs
     */
    public static Collection<Pair> generate(Object base,
            Collection<?> candidates) {
        return generate(Collections.singleton(base), candidates);
    }

}
