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

/**
 * A simple thread-safe {@link Queue}.
 *
 * TODO: Test.
 */
public class SynchronizedQueue implements Queue {
    /** The lock object to use for synchronisation. */
    private final Object _mutex = new Object();

    /** The underlying queue. */
    private final Queue _queue;

    /**
     * Constructor.
     *
     * @param queue The underlying Queue.
     */
    public SynchronizedQueue(Queue queue) {
        assert queue != null : "queue can't be null";
        _queue = queue;
    }

    public void enqueue(Object value) {
        synchronized (_mutex) {
            _queue.enqueue(value);
        }
    }

    public Object dequeue() throws EmptyQueueException {
        synchronized (_mutex) {
            return _queue.dequeue();
        }
    }

    public void clear() {
        synchronized (_mutex) {
            _queue.clear();
        }
    }

    public int size() {
        synchronized (_mutex) {
            return _queue.size();
        }
    }

    public boolean isEmpty() {
        synchronized (_mutex) {
            return _queue.isEmpty();
        }
    }
}
