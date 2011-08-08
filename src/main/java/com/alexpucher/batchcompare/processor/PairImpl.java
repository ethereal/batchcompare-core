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

import java.util.UUID;

import com.alexpucher.batchcompare.Pair;

/**
 * Implementation of {@link Pair} interface.
 * 
 * @author Alexander Pucher
 * 
 */
public class PairImpl implements Pair {

    private Object base;
    private Object candidate;
    private int difference;
    private UUID uuid;

    /**
     * Create {@link PairImpl} instance with new UUID
     * 
     * @param base
     *            base object
     * @param candidate
     *            candidate object
     */
    public PairImpl(Object base, Object candidate) {
        super();
        this.base = base;
        this.candidate = candidate;
        this.difference = 0;
        this.uuid = UUID.randomUUID();
    }

    /**
     * Create {@link PairImpl} instance from given pair copying UUID and setting
     * new difference value.
     * 
     * @param pair
     *            source pair
     * @param difference
     *            difference value
     */
    public PairImpl(Pair pair, int difference) {
        super();
        this.base = pair.getBase();
        this.candidate = pair.getCandidate();
        this.difference = difference;
        this.uuid = pair.getUUID();
    }

    /**
     * Create {@link PairImpl} instance from given pair copying UUID
     * 
     * @param pair
     *            source pair
     */
    public PairImpl(Pair pair) {
        super();
        this.base = pair.getBase();
        this.candidate = pair.getCandidate();
        this.difference = pair.getDifference();
        this.uuid = pair.getUUID();
    }

    public PairImpl setBase(Object base) {
        this.base = base;
        return this;
    }

    public PairImpl setCandidate(Object candidate) {
        this.candidate = candidate;
        return this;
    }

    public PairImpl setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public Pair setDifference(int difference) {
        this.difference = difference;
        return this;
    }

    @Override
    public Object getBase() {
        return this.base;
    }

    @Override
    public Object getCandidate() {
        return this.candidate;
    }

    @Override
    public int getDifference() {
        return difference;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public int compareTo(Pair o) {
        return this.difference - o.getDifference();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((base == null) ? 0 : base.hashCode());
        result = prime * result
                + ((candidate == null) ? 0 : candidate.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PairImpl other = (PairImpl) obj;
        if (base == null) {
            if (other.base != null)
                return false;
        } else if (!base.equals(other.base))
            return false;
        if (candidate == null) {
            if (other.candidate != null)
                return false;
        } else if (!candidate.equals(other.candidate))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

}