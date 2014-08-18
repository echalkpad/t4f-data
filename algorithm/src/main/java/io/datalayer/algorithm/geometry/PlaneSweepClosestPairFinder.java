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
 * Implementation of the Plane Sweep algorithm for finding the closest pair among a set of points.
 *
 */
public final class PlaneSweepClosestPairFinder implements ClosestPairFinder {
    /** The single instance of this class. */
    public static final PlaneSweepClosestPairFinder INSTANCE = new PlaneSweepClosestPairFinder();

    /** Inserter to use for sorting points into a list. */
    private static final ListInserter INSERTER = new ListInserter(
            new IterativeBinaryListSearcher(XYPointComparator.INSTANCE));

    /**
     * Constructor marked private to prevent instantiation.
     */
    private PlaneSweepClosestPairFinder() {
    }

    public Set findClosestPair(Set points) {
        assert points != null : "points can't be null";

        if (points.size() < 2) {
            return null;
        }

        List sortedPoints = sortPoints(points);

        Point p = (Point) sortedPoints.get(0);
        Point q = (Point) sortedPoints.get(1);

        return findClosestPair(p, q, sortedPoints);
    }

    private Set findClosestPair(Point p, Point q, List sortedPoints) {
        Set result = createPointPair(p, q);
        double distance = p.distance(q);
        int dragPoint = 0;

        for (int i = 2; i < sortedPoints.size(); ++i) {
            Point r = (Point) sortedPoints.get(i);
            double sweepX = r.getX();
            double dragX = sweepX - distance;

            while (((Point) sortedPoints.get(dragPoint)).getX() < dragX) {
                ++dragPoint;
            }

            for (int j = dragPoint; j < i; ++j) {
                Point test = (Point) sortedPoints.get(j);
                double checkDistance = r.distance(test);
                if (checkDistance < distance) {
                    distance = checkDistance;
                    result = createPointPair(r, test);
                }
            }
        }

        return result;
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
