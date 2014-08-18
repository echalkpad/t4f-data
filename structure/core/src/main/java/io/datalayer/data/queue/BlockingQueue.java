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
 * A {@link Thread}-safe {@link Queue} that blocks (waits) until values are available to {@link #dequeue()}.
 *
 * TODO: Tests.
 */
public class BlockingQueue implements Queue {
    /** The lock object to use for synchronisation. */
    private final Object _mutex = new Object();

    /** The underlying queue. */
    private final Queue _queue;

    /** The maximum size of the queue. */
    private final int _maxSize;

    /**
     * Constructor.
     *
     * @param queue The underlying Queue.
     */
    public BlockingQueue(Queue queue) {
        this(queue, Integer.MAX_VALUE);
    }

    /**
     * Constructor.
     *
     * @param queue The underlying queue.
     * @param maxSize The maximum size of the queue.
     */
    public BlockingQueue(Queue queue, int maxSize) {
        assert queue != null : "queue can't be null";
        assert maxSize > 0 : "size can't be < 1";

        _queue = queue;
        _maxSize = maxSize;
    }

    public void enqueue(Object value) {
        synchronized (_mutex) {
            while (size() == _maxSize) {
                waitForNotification();
            }
            _queue.enqueue(value);
            _mutex.notifyAll();
        }
    }

    public Object dequeue() throws EmptyQueueException {
        synchronized (_mutex) {
            while (isEmpty()) {
                waitForNotification();
            }
            Object value = _queue.dequeue();
            _mutex.notifyAll();
            return value;
        }
    }

    public void clear() {
        synchronized (_mutex) {
            _queue.clear();
            _mutex.notifyAll();
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

    /**
     * Waits on the semaphore for a notify.
     */
    private void waitForNotification() {
        try {
            _mutex.wait();
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}
