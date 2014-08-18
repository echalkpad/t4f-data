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
package io.datalayer.data.queue;

import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;

/**
 * A Priority {@link Queue} that uses a sorted {@link List} internally.
 *
 */
public class SortedListPriorityQueue implements Queue {
    /** The underlying list. */
    private final List _list;

    /** The comparator to determine priority. */
    private final Comparator _comparator;

    /**
     * Constructor. Uses a {@link LinkedList} as the underlying list.
     *
     * @param comparator The comparator to determine priority.
     */
    public SortedListPriorityQueue(Comparator comparator) {
        assert comparator != null : "comparator cannot be null";
        _comparator = comparator;
        _list = new LinkedList();
    }

    // TODO: This will perform like a dog as it uses a linked list! Consider using an iterator instead.
    // TODO: Or at least an ArrayList.
    public void enqueue(Object value) {
        int pos = _list.size();
        while (pos > 0 && _comparator.compare(_list.get(pos - 1), value) > 0) {
            --pos;
        }
        _list.insert(pos, value);
    }

    public Object dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }
        return _list.delete(_list.size() - 1);
    }

    public void clear() {
        _list.clear();
    }

    public int size() {
        return _list.size();
    }

    public boolean isEmpty() {
        return _list.isEmpty();
    }
}
