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
 * Represents a single point in the geometric coordinate system.
 * Objects of this class are immutable.
 */
public class Point {
    private final double _x;
    private final double _y;

    /**
     * Only supported constructor.
     * @param x x-coordinate for the point.
     * @param y y-coordinate for the point.
     */
    public Point(double x, double y) {
        _x = x;
        _y = y;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public double distance(Point other) {
        assert other != null : "other can't be null";

        double rise = getY() - other.getY();
        double travel = getX() - other.getX();

        return Math.sqrt(rise * rise + travel * travel);
    }

    public int hashCode() {
        return (int) (_x * _y);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Point other = (Point) obj;

        return getX() == other.getX() && getY() == other.getY();
    }
}
