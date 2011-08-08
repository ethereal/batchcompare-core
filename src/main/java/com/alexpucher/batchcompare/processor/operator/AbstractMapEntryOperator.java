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

import java.util.Map.Entry;

import com.alexpucher.batchcompare.Operator;
import com.alexpucher.batchcompare.Pair;

/**
 * Skeleton implementation of map-entry level Operator. Override
 * {@link AbstractMapEntryOperator#entryDifference(java.util.Map.Entry, java.util.Map.Entry)}
 * to generate a difference value for the tuple.
 * 
 * @author Alexander Pucher
 * 
 */
public abstract class AbstractMapEntryOperator implements Operator {

    @Override
    public final Pair execute(Pair pair) {
        Entry<?, ?> base = (Entry<?, ?>) pair.getBase();
        Entry<?, ?> candidate = (Entry<?, ?>) pair.getCandidate();

        pair.setDifference(entryDifference(base, candidate));

        return pair;
    }

    /**
     * Determine difference value of given map entries.
     * 
     * @param base
     *            base entry
     * @param candidate
     *            candidate entry
     * @return difference value
     */
    public abstract int entryDifference(Entry<?, ?> base, Entry<?, ?> candidate);

}
