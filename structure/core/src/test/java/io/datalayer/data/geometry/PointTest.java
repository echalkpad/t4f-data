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

import io.datalayer.data.geometry.Point;
import junit.framework.TestCase;

/**
 */
public class PointTest extends TestCase {
    public void testEquals() {
        assertEquals(new Point(0, 0), new Point(0, 0));
        assertEquals(new Point(5, 8), new Point(5, 8));
        assertEquals(new Point(-4, 6), new Point(-4, 6));

        assertFalse(new Point(0, 0).equals(new Point(1, 0)));
        assertFalse(new Point(0, 0).equals(new Point(0, 1)));
        assertFalse(new Point(4, 4).equals(new Point(-4, 4)));
        assertFalse(new Point(4, 4).equals(new Point(4, -4)));
        assertFalse(new Point(4, 4).equals(new Point(-4, -4)));
        assertFalse(new Point(-4, 4).equals(new Point(-4, -4)));
    }

    public void testDistance() {
        assertEquals(13d, new Point(0, 0).distance(new Point(0, 13)), 0);
        assertEquals(13d, new Point(0, 0).distance(new Point(13, 0)), 0);
        assertEquals(13d, new Point(0, 0).distance(new Point(0, -13)), 0);
        assertEquals(13d, new Point(0, 0).distance(new Point(-13, 0)), 0);

        assertEquals(5d, new Point(1, 1).distance(new Point(4, 5)), 0);
        assertEquals(5d, new Point(1, 1).distance(new Point(-2, -3)), 0);
    }
}
