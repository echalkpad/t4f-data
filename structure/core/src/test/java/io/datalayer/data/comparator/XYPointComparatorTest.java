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
package io.datalayer.data.comparator;

import io.datalayer.data.comparator.XYPointComparator;
import io.datalayer.data.geometry.Point;
import junit.framework.TestCase;

/**
 * Test cases for {@link XYPointComparator}.
 *
 */
public class XYPointComparatorTest extends TestCase {
    private final XYPointComparator _comparator = XYPointComparator.INSTANCE;

    public void testEqualPointsCompareCorrectly() {
        Point p = new Point(4, 4);
        Point q = new Point(4, 4);

        assertEquals(0, _comparator.compare(p, q));
        assertEquals(0, _comparator.compare(p, p));
    }

    public void testXCoordinateIsPrimaryKey() {
        Point p = new Point(-1, 4);
        Point q = new Point(0, 4);
        Point r = new Point(1, 4);

        assertEquals(-1, _comparator.compare(p, q));
        assertEquals(-1, _comparator.compare(p, r));
        assertEquals(-1, _comparator.compare(q, r));

        assertEquals(1, _comparator.compare(q, p));
        assertEquals(1, _comparator.compare(r, p));
        assertEquals(1, _comparator.compare(r, q));
    }

    public void testYCoordinateIsSecondaryKey() {
        Point p = new Point(4, -1);
        Point q = new Point(4, 0);
        Point r = new Point(4, 1);

        assertEquals(-1, _comparator.compare(p, q));
        assertEquals(-1, _comparator.compare(p, r));
        assertEquals(-1, _comparator.compare(q, r));

        assertEquals(1, _comparator.compare(q, p));
        assertEquals(1, _comparator.compare(r, p));
        assertEquals(1, _comparator.compare(r, q));
    }
}
