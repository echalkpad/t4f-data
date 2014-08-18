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
package io.datalayer.algorithm.sort.insertion;

import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;

/**
 * A {@link ListSorter} that uses an insertion sort algorithm.
 *
 */
public class InsertionSortListSorter implements ListSorter {
    private final Comparator _comparator;

    /**
     * @param comparator the comparator to control the order of the sorted objects.
     */
    public InsertionSortListSorter(Comparator comparator) {
        assert comparator != null : "comparator cannot be null";
        _comparator = comparator;
    }

    /**
     * Sorts a list using the inseriton sort algorithm.
     *
     * @param list The list to sort.
     * @return a new List containing the objects from the original List in sorted order.
     */
    public List sort(List list) {
        assert list != null : "list cannot be null";

        final List result = new LinkedList();

        sort(list, result);

        return result;
    }

    void sort(List list, final List result) {
        assert list != null : "list can't be null";
        assert result != null : "result can't be null";

        AosIterator it = list.iterator();

        for (it.first(); !it.isDone(); it.next()) {
            int slot = result.size();
            while (slot > 0) {
                if (_comparator.compare(it.current(), result.get(slot - 1)) >= 0) {
                    break;
                }
                --slot;
            }
            result.insert(slot, it.current());
        }
    }
}
