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
package io.datalayer.algorithm.sort.merge;

import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;

/**
 * Sample solution for exercise 7-1.
 * Iterative implementation of mergesort algorithm.
 *
 */
public class IterativeMergesortListSorter implements ListSorter {
    /** The comparator to control the order of the sorted objects. */
    private final Comparator _comparator;

    /**
     * @param comparator the comparator to control the order of the sorted objects.
     */
    public IterativeMergesortListSorter(Comparator comparator) {
        assert comparator != null : "comparator cannot be null";
        _comparator = comparator;
    }

    /**
     * Sorts a list using the mergesort algorithm.
     *
     * @param list The list to sort.
     * @return A new List containing the items from the original List in sorted order.
     */
    public List sort(List list) {
        assert list != null : "list cannot be null";

        return mergeSublists(createSublists(list));
    }

    private List mergeSublists(List sublists) {
        List remaining = sublists;
        while (remaining.size() > 1) {
            remaining = mergeSublistPairs(remaining);

        }
        return (List) remaining.get(0);
    }

    private List mergeSublistPairs(List remaining) {
        List result = new ArrayList(remaining.size() / 2 + 1);

        AosIterator i = remaining.iterator();
        i.first();
        while (!i.isDone()) {
            List left = (List) i.current();
            i.next();
            if (i.isDone()) {
                result.add(left);
            } else {
                List right = (List) i.current();
                i.next();
                result.add(merge(left, right));
            }
        }

        return result;
    }

    private List createSublists(List list) {
        List result = new ArrayList(list.size());

        AosIterator i = list.iterator();
        i.first();
        while (!i.isDone()) {
            List singletonList = new ArrayList(1);
            singletonList.add(i.current());
            result.add(singletonList);
            i.next();
        }

        return  result;
    }

    private List merge(List left, List right) {
        List result = new ArrayList();

        AosIterator l = left.iterator();
        AosIterator r = right.iterator();

        l.first();
        r.first();

        while (!(l.isDone() && r.isDone())) {
            if (l.isDone()) {
                result.add(r.current());
                r.next();
            } else if (r.isDone()) {
                result.add(l.current());
                l.next();
            } else if (_comparator.compare(l.current(), r.current()) <= 0) {
                result.add(l.current());
                l.next();
            } else {
                result.add(r.current());
                r.next();
            }
        }

        return result;
    }
}
