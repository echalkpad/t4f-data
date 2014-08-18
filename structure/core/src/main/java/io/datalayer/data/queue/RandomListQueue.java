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

import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;

/**
 * A {@link Queue} that uses a {@link List} internally and retrieves values in random order.
 *
 */
public class RandomListQueue implements Queue {
    /** The underlying list. */
    private final List _list;

    /**
     * Default constructor. Uses a {@link LinkedList} as the underlying list.
     */
    public RandomListQueue() {
        this(new LinkedList());
    }

    /**
     * Constructor.
     *
     * @param list The underlying list.
     */
    public RandomListQueue(List list) {
        _list = list;
    }

    public void enqueue(Object value) {
        _list.add(value);
    }

    public Object dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }
        return _list.delete((int) (Math.random() * size()));
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
