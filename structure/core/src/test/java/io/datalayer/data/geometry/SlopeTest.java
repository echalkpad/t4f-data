/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package io.datalayer.data.geometry;

import io.datalayer.data.geometry.Slope;
import junit.framework.TestCase;

/**
 */
public class SlopeTest extends TestCase {
    public void testIsVertical() {
        assertTrue(new Slope(4, 0).isVertical());
        assertTrue(new Slope(0, 0).isVertical());
        assertTrue(new Slope(-5, 0).isVertical());
        assertFalse(new Slope(0, 5).isVertical());
        assertFalse(new Slope(0, -5).isVertical());
    }

    public void testEquals() {
        assertTrue(new Slope(0, -5).equals(new Slope(0, 10)));
        assertTrue(new Slope(1, 3).equals(new Slope(2, 6)));
        assertFalse(new Slope(1, 3).equals(new Slope(-1, 3)));
        assertFalse(new Slope(1, 3).equals(new Slope(1, -3)));
        assertTrue(new Slope(5, 0).equals(new Slope(9, 0)));
    }

    public void testAsDoubleForNonVerticalSlope() {
        assertEquals(0, new Slope(0, 4).asDouble(), 0);
        assertEquals(0, new Slope(0, -4).asDouble(), 0);
        assertEquals(1, new Slope(3, 3).asDouble(), 0);
        assertEquals(1, new Slope(-3, -3).asDouble(), 0);
        assertEquals(-1, new Slope(3, -3).asDouble(), 0);
        assertEquals(-1, new Slope(-3, 3).asDouble(), 0);
        assertEquals(2, new Slope(6, 3).asDouble(), 0);
        assertEquals(1.5, new Slope(6, 4).asDouble(), 0);
    }

    public void testAsDoubleFailsForVerticalSlope() {
        try {
            new Slope(4, 0).asDouble();
            fail("should have blown up!");
        } catch (IllegalStateException e) {
            assertEquals("Vertical slope cannot be represented as double", e.getMessage());
        }
    }
}
