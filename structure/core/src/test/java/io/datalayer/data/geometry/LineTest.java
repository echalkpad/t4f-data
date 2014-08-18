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

import io.datalayer.data.geometry.Line;
import io.datalayer.data.geometry.Point;
import junit.framework.TestCase;

/**
 */
public class LineTest extends TestCase {
    public void testContainsForNonVerticalLine() {
        Point p = new Point(0, 0);
        Point q = new Point(3, 3);

        Line l = new Line(p, q);

        assertTrue(l.contains(p));
        assertTrue(l.contains(q));

        assertTrue(l.contains(new Point(1, 1)));
        assertTrue(l.contains(new Point(2, 2)));
        assertTrue(l.contains(new Point(0.5, 0.5)));

        assertFalse(l.contains(new Point(3.1, 3.1)));
        assertFalse(l.contains(new Point(3, 3.1)));
        assertFalse(l.contains(new Point(0, 1)));
        assertFalse(l.contains(new Point(-1, -1)));
    }

    public void testContainsForVerticalLine() {
        Point p = new Point(0, 0);
        Point q = new Point(0, 3);

        Line l = new Line(p, q);

        assertTrue(l.contains(p));
        assertTrue(l.contains(q));

        assertTrue(l.contains(new Point(0, 1)));
        assertTrue(l.contains(new Point(0, 2)));
        assertTrue(l.contains(new Point(0, 0.5)));

        assertFalse(l.contains(new Point(0, 3.1)));
        assertFalse(l.contains(new Point(0.1, 1)));
        assertFalse(l.contains(new Point(1, 0)));
        assertFalse(l.contains(new Point(-1, -1)));
    }

    public void testIsParallelForTwoNonVerticalParallelLines() {
        Point p = new Point(1, 1);
        Point q = new Point(6, 6);
        Point r = new Point(4, -2);
        Point s = new Point(6, 0);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertTrue(l.isParallelTo(m));
        assertTrue(m.isParallelTo(l));
    }

    public void testIsParallelForTwoNonVerticalNonParallelLines() {
        Point p = new Point(1, 1);
        Point q = new Point(6, 4);
        Point r = new Point(4, -2);
        Point s = new Point(6, 0);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertFalse(l.isParallelTo(m));
        assertFalse(m.isParallelTo(l));
    }

    public void testIsParallelForTwoVerticalParallelLines() {
        Point p = new Point(1, 1);
        Point q = new Point(1, 6);
        Point r = new Point(4, -2);
        Point s = new Point(4, 0);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertTrue(l.isParallelTo(m));
        assertTrue(m.isParallelTo(l));
    }

    public void testIsParallelForOneVerticalAndOneNonVerticalLine() {
        Point p = new Point(1, 1);
        Point q = new Point(1, 6);
        Point r = new Point(4, -2);
        Point s = new Point(6, 0);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertFalse(l.isParallelTo(m));
        assertFalse(m.isParallelTo(l));
    }

    public void testParallelNonVerticalLinesDoNotIntersect() {
        Point p = new Point(0, 0);
        Point q = new Point(3, 3);
        Point r = new Point(5, 0);
        Point s = new Point(8, 3);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertNull(l.intersectionPoint(m));
        assertNull(m.intersectionPoint(l));
    }

    public void testVerticalLinesDoNotIntersect() {
        Point p = new Point(0, 0);
        Point q = new Point(0, 3);
        Point r = new Point(5, 0);
        Point s = new Point(5, 3);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertNull(l.intersectionPoint(m));
        assertNull(m.intersectionPoint(l));
    }

    public void testIntersectionOfNonParallelNonVerticalLines() {
        Point p = new Point(0, 0);
        Point q = new Point(4, 4);
        Point r = new Point(4, 0);
        Point s = new Point(0, 4);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        Point i = new Point(2, 2);

        assertEquals(i, l.intersectionPoint(m));
        assertEquals(i, m.intersectionPoint(l));
    }

    public void testIntersectionOfVerticalAndNonVerticalLines() {
        Point p = new Point(0, 0);
        Point q = new Point(4, 4);
        Point r = new Point(2, 0);
        Point s = new Point(2, 4);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        Point i = new Point(2, 2);

        assertEquals(i, l.intersectionPoint(m));
        assertEquals(i, m.intersectionPoint(l));
    }

    public void testDisjointLinesDoNotIntersect() {
        Point p = new Point(0, 0);
        Point q = new Point(0, 3);
        Point r = new Point(5, 0);
        Point s = new Point(-1, -3);

        Line l = new Line(p, q);
        Line m = new Line(r, s);

        assertNull(l.intersectionPoint(m));
        assertNull(m.intersectionPoint(l));
    }
}
