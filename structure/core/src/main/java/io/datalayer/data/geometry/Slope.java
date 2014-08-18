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
 * Represents the slope of a {@link Line}.
 * Objects of this class are immutable.
 */
public class Slope {
    private final double _rise;
    private final double _travel;

    public Slope(double rise, double travel) {
        _rise = rise;
        _travel = travel;
    }

    public boolean isVertical() {
        return _travel == 0;
    }

    public int hashCode() {
        return (int) (_rise * _travel);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        Slope other = (Slope) object;

        if (isVertical() && other.isVertical()) {
            return true;
        }

        if (isVertical() || other.isVertical()) {
            return false;
        }

        return (asDouble()) == (other.asDouble());
    }

    public double asDouble() {
        if (isVertical()) {
            throw new IllegalStateException("Vertical slope cannot be represented as double");
        }

        return _rise / _travel;
    }
}
