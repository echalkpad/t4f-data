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
package io.datalayer.data.stack;

import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;
import io.datalayer.data.queue.EmptyQueueException;

/**
 * A {@link Stack} that uses a {@link List} internally.
 *
 */
public class ListStack implements Stack {
    /** The underlying list. */
    private final List _list = new LinkedList();

    public void push(Object value) {
        _list.add(value);
    }

    public Object pop() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return _list.delete(_list.size() - 1);
    }

    public Object peek() throws EmptyStackException {
        Object result = pop();
        push(result);
        return result;
    }

    public void enqueue(Object value) {
        push(value);
    }

    public Object dequeue() throws EmptyQueueException {
        try {
            return pop();
        } catch (EmptyStackException e) {
            throw new EmptyQueueException();
        }
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
