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
// Interface for queues.
// (c) 1998,2001 duane a. bailey
package io.datalayer.data.structure;
/**
 * A first-in, first-out structure.  Values are added at the tail, and removed
 * from the head.  Used to process values in the order that they appear.
 * 
 * @see io.datalayer.data.structure.Stack
 * @version $Id: Queue.java 19 2006-08-10 04:52:00Z bailey $
 * @author, 2001 duane a. bailey
 */
public interface Queue extends Linear
{
    /**
     * Add a value to the tail of the queue.
     *
     * @post the value is added to the tail of the structure
     * 
     * @param value The value added.
     * @see #enqueue
     */
    public void add(Object value);

    /**
     * Add a value to the tail of the queue.
     *
     * @post the value is added to the tail of the structure
     * 
     * @param value The value to be added.
     */
    public void enqueue(Object value);

    /**
     * Remove a value form the head of the queue.
     *
     * @pre the queue is not empty
     * @post the head of the queue is removed and returned
     * 
     * @return The value actually removed.
     * @see #dequeue
     */
    public Object remove();

    /**
     * Remove a value from the head of the queue.
     *
     * @pre the queue is not empty
     * @post the head of the queue is removed and returned
     * 
     * @return The value removed from the queue.
     */
    public Object dequeue();

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre the queue is not empty
     * @post the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public Object getFirst();

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre the queue is not empty
     * @post the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public Object get();

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre the queue is not empty
     * @post the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public Object peek();

    /**
     * Returns true iff the queue is empty.  Provided for
     * compatibility with java.util.Vector.empty.
     *
     * @post returns true if and only if the queue is empty
     * 
     * @return True iff the queue is empty.
     */
    public boolean empty();

    /**
     * Returns the number of elements in the queue.
     *
     * @post returns the number of elements in the queue
     * @return number of elements in queue.
     */
    public int size();
}
