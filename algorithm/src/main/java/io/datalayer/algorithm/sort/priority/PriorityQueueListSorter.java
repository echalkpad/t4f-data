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
package io.datalayer.algorithm.sort.priority;

import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.ReverseComparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import io.datalayer.data.queue.HeapOrderedListPriorityQueue;
import io.datalayer.data.queue.Queue;

/**
 * Sample solution for exercise 8-3.
 * A {@link io.datalayer.algorithm.list.ListSorter} that uses a priority queue internally.
 *
 */
public class PriorityQueueListSorter implements ListSorter {
    private final Comparator _comparator;

    /**
     * @param comparator the comparator to control the order of the sorted objects.
     */
    public PriorityQueueListSorter(Comparator comparator) {
        assert comparator != null : "comparator cannot be null";
        _comparator = comparator;
    }

    /**
     * Sorts a list using a priority queue.
     *
     * @param list The list to sort.
     * @return a new list containing the items in sorted order.
     */
    public List sort(List list) {
        assert list != null : "list cannot be null";

        Queue queue = createPriorityQueue(list);

        List result = new ArrayList(list.size());

        while (!queue.isEmpty()) {
            result.add(queue.dequeue());
        }

        return result;
    }

    private Queue createPriorityQueue(List list) {
        Comparator comparator = new ReverseComparator(_comparator);
        Queue queue = new HeapOrderedListPriorityQueue(comparator);

        AosIterator i = list.iterator();
        i.first();
        while (!i.isDone()) {
            queue.enqueue(i.current());
            i.next();
        }

        return queue;
    }
}
