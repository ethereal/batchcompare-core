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


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alexpucher.batchcompare.Pair;
import com.alexpucher.batchcompare.Processor;
import com.alexpucher.batchcompare.processor.PairImpl;
import com.alexpucher.batchcompare.processor.filter.BlockadeFilter;
import com.alexpucher.batchcompare.processor.filter.CountFilter;
import com.alexpucher.batchcompare.processor.filter.IdentityFilter;
import com.alexpucher.batchcompare.processor.filter.Inverter;
import com.alexpucher.batchcompare.processor.filter.PercentFilter;
import com.alexpucher.batchcompare.processor.filter.ThresholdFilter;

public class FilterTest {
    
    private Collection<Pair> inputPairs;

    @Before
    public void setUp() throws Exception {
        this.inputPairs = new ArrayList<Pair>();
        this.inputPairs.add(new PairImpl(new Object(), new Object()).setDifference(2));
        this.inputPairs.add(new PairImpl(new Object(), new Object()).setDifference(0));
        this.inputPairs.add(new PairImpl(new Object(), new Object()).setDifference(1));
        this.inputPairs.add(new PairImpl(new Object(), new Object()).setDifference(5));
        this.inputPairs.add(new PairImpl(new Object(), new Object()).setDifference(3));
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void identityFilterTest() {
        Processor filter = new IdentityFilter();
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 5, outputPairs.size());
        assertEquals("max difference of elements remaining", 5, Collections.max(outputPairs).getDifference());
        assertEquals("min difference of elements remaining", 0, Collections.min(outputPairs).getDifference());
    }
    
    @Test
    public void blockadeFilterTest() {
        Processor filter = new BlockadeFilter();
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 0, outputPairs.size());
    }
    
    @Test
    public void percentFilterTest() {
        Processor filter = new PercentFilter(0.40);
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 2, outputPairs.size());
        assertEquals("max difference of elements remaining", 1, Collections.max(outputPairs).getDifference());
    }

    @Test
    public void percentFilterEmptyInputTest() {
        Processor filter = new PercentFilter(1.00);
        
        Collection<Pair> outputPairs = filter.execute(new ArrayList<Pair>());
        
        assertEquals("number of elements remaining", 0, outputPairs.size());
    }

    @Test
    public void percentFilterEmptyOutputTest() {
        Processor filter = new PercentFilter(0.0);
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 0, outputPairs.size());
    }

    @Test
    public void thresholdFilterTest() {
        Processor filter = new ThresholdFilter(3);
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 4, outputPairs.size());
        assertEquals("max difference of elements remaining", 3, Collections.max(outputPairs).getDifference());
    }

    @Test
    public void countFilterTest() {
        Processor filter = new CountFilter(3);
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 3, outputPairs.size());
        assertEquals("max difference of elements remaining", 2, Collections.max(outputPairs).getDifference());
    }

    @Test
    public void countFilterEmptyInputTest() {
        Processor filter = new CountFilter(3);
        
        Collection<Pair> outputPairs = filter.execute(new ArrayList<Pair>());
        
        assertEquals("number of elements remaining", 0, outputPairs.size());
    }

    @Test
    public void countFilterEmptyOutputTest() {
        Processor filter = new CountFilter(0);
        
        Collection<Pair> outputPairs = filter.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 0, outputPairs.size());
    }

    @Test
    public void invertTest() {
        Processor inv = new Inverter(new ThresholdFilter(2));
        
        Collection<Pair> outputPairs = inv.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 2, outputPairs.size());
        assertEquals("min difference of elements remaining", 3, Collections.min(outputPairs).getDifference());
    }
    
    @Test
    public void invertNoneTest() {
        Processor inv = new Inverter(new BlockadeFilter());
        
        Collection<Pair> outputPairs = inv.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 5, outputPairs.size());
        assertEquals("min difference of elements remaining", 0, Collections.min(outputPairs).getDifference());
    }
    
    @Test
    public void invertAllTest() {
        Processor inv = new Inverter(new IdentityFilter());
        
        Collection<Pair> outputPairs = inv.execute(this.inputPairs);
        
        assertEquals("number of elements remaining", 0, outputPairs.size());
    }
    
}
