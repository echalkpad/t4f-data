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

import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.queue.EmptyQueueException;
import io.datalayer.data.queue.HeapOrderedListPriorityQueue;

/**
 * A {@link Stack} that uses a priority queue.
 *
 */
public class PriorityQueueStack extends HeapOrderedListPriorityQueue implements Stack {
    /** The comparator to use. */
    private final static Comparator COMPARATOR = new StackItemComparator();

    /** the item counter */
    private long _count = 0;

    public PriorityQueueStack() {
        super(COMPARATOR);
    }

    public void enqueue(Object value) {
        super.enqueue(new StackItem(++_count, value));
    }

    public Object dequeue() throws EmptyQueueException {
        return ((StackItem) super.dequeue()).getValue();
    }

    public void push(Object value) {
        enqueue(value);
    }

    public Object pop() throws EmptyStackException {
        try {
            return dequeue();
        } catch (EmptyQueueException e) {
            throw new EmptyStackException();
        }
    }

    public Object peek() throws EmptyStackException {
        Object result = pop();
        push(result);
        return result;
    }

    private static final class StackItem {
        private final long _key;
        private final Object _value;

        public StackItem(long key, Object value) {
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

    private static final class StackItemComparator implements Comparator {
        public int compare(Object left, Object right) throws ClassCastException {
            StackItem si1 = (StackItem) left;
            StackItem si2 = (StackItem) right;

            return (int) (si1.getKey() - si2.getKey());
        }
    }
}
