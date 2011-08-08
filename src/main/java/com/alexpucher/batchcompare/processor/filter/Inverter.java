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
package com.alexpucher.batchcompare.processor.filter;

import java.util.Collection;
import java.util.HashSet;

import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;

/**
 * Inverts the result set of a filter.<br />
 * Given a set {a, b, c} and a filter result {a} the output of the inverter is
 * {b, c}.
 * 
 * @author Alexander Pucher
 * 
 */
public class Inverter implements Processor {

    private Processor filter;

    /**
     * Create a {@link Inverter} instance with given filter.
     * 
     * @param filter
     *            filter processor
     */
    public Inverter(Processor filter) {
        super();
        this.filter = filter;
    }

    /*
     * (non-Javadoc)
     * @see com.alexpucher.batchcompare.Processor#execute(java.util.Collection)
     */
    @Override
    public Collection<Pair> execute(Collection<Pair> pairs) {
        Collection<Pair> inputCopy = new HashSet<Pair>(pairs);
        Collection<Pair> output = this.filter.execute(pairs);

        inputCopy.removeAll(output);

        return inputCopy;
    }

    public Processor getFilter() {
        return filter;
    }

    public void setFilter(Processor filter) {
        this.filter = filter;
    }

}
