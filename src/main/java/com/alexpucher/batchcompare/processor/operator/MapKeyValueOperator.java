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

/**
 * Determines distance of map entries based on a difference in keys and values.
 * Difference is <code>0</code> in case of a key and value match,
 * {@link MapOperator#defaultValuePenalty} for key match only and
 * {@link MapOperator#defaultKeyPenalty} otherwise.
 * 
 * @author Alexander Pucher
 * 
 */
public class MapKeyValueOperator extends AbstractMapEntryOperator {

    @Override
    public int entryDifference(Entry<?, ?> base, Entry<?, ?> candidate) {
        if (!base.getKey().equals(candidate.getKey())) {
            return MapOperator.defaultKeyPenalty;
        }

        if (!base.getValue().equals(candidate.getValue())) {
            return MapOperator.defaultValuePenalty;
        }

        return 0;
    }

}
