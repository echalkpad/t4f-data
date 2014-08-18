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

/**
 * Sample solution for exercise 8-2.
 * A First-In-First-Out (FIFO) {@link io.datalayer.data.queue.Queue}
 * that uses a priority {@link io.datalayer.data.queue.Queue} internally.
 *
 */
public class PriorityQueueFifoQueue extends HeapOrderedListPriorityQueue {
    private static final Comparator COMPARATOR = new QueueItemComparator();

    /** the item counter */
    private long _count = Long.MAX_VALUE;

    public PriorityQueueFifoQueue() {
        super(COMPARATOR);
    }

    public void enqueue(Object value) {
        super.enqueue(new QueueItem(--_count, value));
    }

    public Object dequeue() throws EmptyQueueException {
        return ((QueueItem) super.dequeue()).getValue();
    }

    private static final class QueueItem {
        private final long _key;
        private final Object _value;

        public QueueItem(long key, Object value) {
            _key = key;
            _value = value;
        }

        public long getKey() {
            return _key;
        }

        public Object getValue() {
            return _value;
        }
    }

    private static final class QueueItemComparator implements Comparator {
        public int compare(Object left, Object right) throws ClassCastException {
            QueueItem si1 = (QueueItem) left;
            QueueItem si2 = (QueueItem) right;

            return (int) (si1.getKey() - si2.getKey());
        }
    }
}
