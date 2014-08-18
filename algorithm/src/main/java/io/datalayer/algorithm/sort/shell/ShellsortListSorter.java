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
package io.datalayer.algorithm.sort.shell;

import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.list.List;

/**
 * A {@link ListSorter} that uses an shellsort algorithm.
 *
 */
public class ShellsortListSorter implements ListSorter {
    /** The comparator to control the order of the sorted objects. */
    private final Comparator _comparator;

    /** our sequence of increments for H-sorting the list. */
    private final int[] _increments = {121, 40, 13, 4, 1};

    /**
     * @param comparator the comparator to control the order of the sorted objects.
     */
    public ShellsortListSorter(Comparator comparator) {
        assert comparator != null : "comparator cannot be null";
        _comparator = comparator;
    }

    /**
     * Sorts a list using the shellsort algorithm.
     *
     * @param list The list to sort.
     * @return the original List in sorted order.
     */
    public List sort(List list) {
        assert list != null : "list cannot be null";

        for (int i = 0; i < _increments.length; ++i) {
            int increment = _increments[i];
            hSort(list, increment);
        }

        return list;
    }

    private void hSort(List list, int increment) {
        if (list.size() < (increment * 2)) {
            return;
        }

        for (int i = 0; i < increment; ++i) {
            sortSublist(list, i, increment);
        }
    }

    private void sortSublist(List list, int startIndex, int increment) {
        for (int i = startIndex + increment; i < list.size(); i += increment) {
            Object value = list.get(i);
            int j;
            for (j = i; j > startIndex; j -= increment) {
                Object previousValue = list.get(j - increment);
                if (_comparator.compare(value, previousValue) >= 0) {
                    break;
                }
                list.set(j, previousValue);
            }
            list.set(j, value);
        }
    }
}
