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

import io.datalayer.data.geometry.Point;


/**
 * A {@link Comparator} that orders {@link Point} objects based on their x coordinates, followed by their y coordinates.
 *
 */
public final class XYPointComparator implements Comparator {
    /** The single instance of the class. */
    public static final XYPointComparator INSTANCE = new XYPointComparator();

    /**
     * Constructor marked private to prevent instantiation.
     */
    private XYPointComparator() {
    }

    public int compare(Object left, Object right) throws ClassCastException {
        return compare((Point) left, (Point) right);
    }

    public int compare(Point p, Point q) throws ClassCastException {
        assert p != null : "p can't be null";
        assert q != null : "q can't be null";

        int result = new Double(p.getX()).compareTo(new Double(q.getX()));
        if (result != 0) {
            return result;
        }
        return new Double(p.getY()).compareTo(new Double(q.getY()));
    }
}
