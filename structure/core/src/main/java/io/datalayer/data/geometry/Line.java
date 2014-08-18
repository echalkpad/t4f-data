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


/**
 * Represents a line in the geometric coordinate system.
 * A line has two defined endpoints; it does not extend indefinitely.
 * Objects of this class are immutable.
 */
public class Line {
    private final Point _p;
    private final Point _q;
    private final Slope _slope;

    /**
     * Constructor for the immutable line class.
     * @param p the first end-point.
     * @param q the second end-point.
     */
    public Line(Point p, Point q) {
        assert p != null : "point defining a line cannot be null";
        assert q != null : "point defining a line cannot be null";

        _p = p;
        _q = q;
        _slope = new Slope(_p.getY() - _q.getY(), _p.getX() - _q.getX());
    }

    /**
     * Determines if the supplied line is parallel to this object.
     * @param line the line to compare.
     * @return true if the lines are parallel; false otherwise.
     */
    public boolean isParallelTo(Line line) {
        assert line != null : "line can't be null";
        return _slope.equals(line._slope);
    }

    /**
     * Determines if the supplied a is part of this line.
     * @param a the a to test.
     * @return true if the a is part of the line; otherwise false.
     */
    public boolean contains(Point a) {
        assert a != null : "point to test can't be null";

        if (!isWithin(a.getX(), _p.getX(), _q.getX())) {
            return false;
        }

        if (!isWithin(a.getY(), _p.getY(), _q.getY())) {
            return false;
        }

        return _slope.isVertical() || a.getY() == solveY(a.getX());
    }

    private double solveY(double x) {
        return _slope.asDouble() * x + base();
    }

    private double base() {
        return _p.getY() - _slope.asDouble() * _p.getX();
    }

    private static boolean isWithin(double test, double bound1, double bound2) {
        return test >= Math.min(bound1, bound2) && test <= Math.max(bound1, bound2);
    }

    public Point intersectionPoint(Line line) {
        assert line != null : "line can't be null";

        if (isParallelTo(line)) {
            return null;
        }

        double x = getIntersectionXCoordinate(line);
        double y = getIntersectionYCoordinate(line, x);

        Point p = new Point(x, y);

        if (line.contains(p) && this.contains(p)) {
            return p;
        }

        return null;
    }

    private double getIntersectionYCoordinate(Line line, double x) {
        if (_slope.isVertical()) {
            return line.solveY(x);
        }

        return solveY(x);
    }

    private double getIntersectionXCoordinate(Line line) {
        if (_slope.isVertical()) {
            return _p.getX();
        }

        if (line._slope.isVertical()) {
            return line._p.getX();
        }

        double m = _slope.asDouble();
        double b = base();

        double n = line._slope.asDouble();
        double c = line.base();

        return (c - b) / (m - n);
    }
}
