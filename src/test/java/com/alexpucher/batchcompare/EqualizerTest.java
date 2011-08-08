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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alexpucher.batchcompare.processor.equalizer.BinaryEqualizer;
import com.alexpucher.batchcompare.processor.equalizer.ExponentialEqualizer;
import com.alexpucher.batchcompare.processor.equalizer.LinearEqualizer;

public class EqualizerTest {

    private int[] values;
    
    @Before
    public void setUp() throws Exception {
        values = new int[]{0, 4, 2, 3, 1};

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void linearTest() {
        Equalizer equalizer = new LinearEqualizer(100);
        equalizer.setup(this.values);
        
        assertEquals("upper bound", 100, equalizer.equalize(4));
        assertEquals("lower bound", 0, equalizer.equalize(0));
        assertEquals("mean", 50, equalizer.equalize(2));
    }
    
    @Test
    public void linearOffsetTest() {
        Equalizer equalizer = new LinearEqualizer(100, 1000);
        equalizer.setup(this.values);
        
        assertEquals("upper bound", 1100, equalizer.equalize(4));
        assertEquals("lower bound", 1000, equalizer.equalize(0));
        assertEquals("mean", 1050, equalizer.equalize(2));
    }
    
    @Test
    public void exponentialTest() {
        Equalizer equalizer = new ExponentialEqualizer(100);

        assertEquals("lower bound", 0, equalizer.equalize(0));
        assertEquals(63, equalizer.equalize(1));
        assertEquals(86, equalizer.equalize(2));
        assertEquals(95, equalizer.equalize(3));
        assertEquals("upper bound", 100, equalizer.equalize(Integer.MAX_VALUE));
        
    }
    
    @Test
    public void binaryTest() {
        Equalizer equalizer = new BinaryEqualizer(0, 100);
        equalizer.setup(this.values);
        
        assertEquals("upper bound", 100, equalizer.equalize(4));
        assertEquals("mean", 100, equalizer.equalize(2));
        assertEquals("lower bound", 0, equalizer.equalize(0));
    }
    
}
