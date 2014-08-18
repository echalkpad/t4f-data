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
 * A generic interface for queues.
 *
 */
public interface Queue {
    /**
     * Stores a value in the queue. The size of the queue will increase by one.
     *
     * @param value The value to add.
     */
    public void enqueue(Object value);

    /**
     * Retrieves the value at the head of the queue. The size of the queue will decrease by one.
     *
     * @return The value at the head of the queue.
     * @throws EmptyQueueException If the queue is empty (<code>isEmpty() == true</code>).
     */
    public Object dequeue() throws EmptyQueueException;

    /**
     * Deletes all elements from the queue. The size of the queue will be reset to zero (0).
     */
    public void clear();

    /**
     * Obtains the number of elements in the queue.
     *
     * @return The number of elements in the queue.
     */
    public int size();

    /**
     * Determines if the queue is empty or not.
     *
     * @return <code>true</code> if the queue is empty (<code>size() == 0</code>); otherwise returns <code>false</code>.
     */
    public boolean isEmpty();
}
