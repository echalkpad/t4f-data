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

import io.datalayer.algorithm.bsearch.IterativeBinaryListSearcher;
import io.datalayer.algorithm.list.ListInserter;
import io.datalayer.data.comparator.XYPointComparator;
import io.datalayer.data.geometry.Point;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import io.datalayer.data.set.ListSet;
import io.datalayer.data.set.Set;

/**
 * Implementation of a brute force algorithm for finding the closest pair among a set of points.
 *
 */
public final class BruteForceClosestPairFinder implements ClosestPairFinder {
    /** The single instance of this class. */
    public static final BruteForceClosestPairFinder INSTANCE = new BruteForceClosestPairFinder();

    /** Inserter to use for sorting points into a list. */
    private static final ListInserter INSERTER = new ListInserter(
            new IterativeBinaryListSearcher(XYPointComparator.INSTANCE));

    /**
     * Constructor marked private to prevent instantiation.
     */
    private BruteForceClosestPairFinder() {
    }

    public Set findClosestPair(Set points) {
        assert points != null : "points can't be null";

        if (points.size() < 2) {
            return null;
        }

        List list = sortPoints(points);

        Point p = null;
        Point q = null;
        double distance = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            Point r = (Point) list.get(i);
            for (int j = 0; j < list.size(); ++j) {
                Point s = (Point) list.get(j);
                if (r != s && r.distance(s) < distance) {
                    distance = r.distance(s);
                    p = r;
                    q = s;
                }
            }
        }

        return createPointPair(p, q);
    }

    private static List sortPoints(Set points) {
        assert points != null : "points can't be null";

        List list = new ArrayList(points.size());

        AosIterator i = points.iterator();
        for (i.first(); !i.isDone(); i.next()) {
            INSERTER.insert(list, i.current());
        }

        return list;
    }

    private Set createPointPair(Point p, Point q) {
        Set result = new ListSet();
        result.add(p);
        result.add(q);
        return result;
    }
}
