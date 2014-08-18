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
package io.datalayer.algorithm.geometry;

import io.datalayer.algorithm.geometry.ClosestPairFinder;
import io.datalayer.data.geometry.Point;
import io.datalayer.data.set.ListSet;
import io.datalayer.data.set.Set;
import junit.framework.TestCase;

/**
 * Abstract nase class for testing implementations of {@link ClosestPairFinder}.
 *
 */
public abstract class AbstractClosestPairFinderTestCase extends TestCase {
    /**
     * Test cases for specific algorithms should implement this factory
     * method to instantiate the given algorithm implementation.
     * @return the algorithm implementation.
     */
    protected abstract ClosestPairFinder createClosestPairFinder();

    public void testEmptySetOfPoints() {
        ClosestPairFinder finder = createClosestPairFinder();
        assertNull(finder.findClosestPair(new ListSet()));
    }

    public void testASinglePointReturnsNull() {
        ClosestPairFinder finder = createClosestPairFinder();

        Set points = new ListSet();
        points.add(new Point(1, 1));

        assertNull(finder.findClosestPair(points));
    }

    public void testASinglePairOfPoints() {
        ClosestPairFinder finder = createClosestPairFinder();

        Set points = new ListSet();
        Point p = new Point(1, 1);
        Point q = new Point(2, 4);

        points.add(p);
        points.add(q);

        Set pair = finder.findClosestPair(points);

        assertNotNull(pair);
        assertEquals(2, pair.size());
        assertTrue(pair.contains(p));
        assertTrue(pair.contains(q));
    }

    public void testThreePointsEquallySpacedApart() {
        ClosestPairFinder finder = createClosestPairFinder();

        Set points = new ListSet();
        Point p = new Point(1, 0);
        Point q = new Point(1, 4);
        Point r = new Point(1, -4);

        points.add(p);
        points.add(q);
        points.add(r);

        Set pair = finder.findClosestPair(points);

        assertNotNull(pair);
        assertEquals(2, pair.size());
        assertTrue(pair.contains(p));
        assertTrue(pair.contains(r));
    }

    public void testLargeSetOfPointsWithTwoEqualShortestPairs() {
        ClosestPairFinder finder = createClosestPairFinder();

        Set points = new ListSet();

        points.add(new Point(0, 0));
        points.add(new Point(4, -2));
        points.add(new Point(2, 7));
        points.add(new Point(3, 7));
        points.add(new Point(-1, -5));
        points.add(new Point(-5, 3));
        points.add(new Point(-5, 4));
        points.add(new Point(-0, -9));
        points.add(new Point(-2, -2));

        Set pair = finder.findClosestPair(points);

        assertNotNull(pair);
        assertEquals(2, pair.size());
        assertTrue(pair.contains(new Point(-5, 3)));
        assertTrue(pair.contains(new Point(-5, 4)));
    }
}
