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
// Implementation of queues using linked lists.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;

/**
 * An implementation of queues based on lists.
 *
 * @version $Id: QueueList.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class QueueList extends AbstractQueue implements Queue
{
    /**
     * The list holding queue values in order.
     */
    protected List data;

    /**
     * Construct a new queue with no data.
     *
     * @post Constructs a new, empty queue
     */
    public QueueList()
    {
        data = new CircularList();
    }

    /**
     * Add a value to the tail of the queue.
     *
     * @post The value is added to the tail of the structure
     * 
     * @param value The value added.
     * @see #enqueue
     */
    public void add(Object value)
    {
        data.addLast(value);
    }

    /**
     * Remove a value from the head of the queue.
     *
     * @pre The queue is not empty
     * @post The head of the queue is removed and returned
     * 
     * @return The value actually removed.
     * @see #dequeue
     */
    public Object remove()
    {
        return data.removeFirst();
    }

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre The queue is not empty
     * @post The element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public Object get()
    {
        return data.getFirst();
    }

    /**
     * Determine the number of elements within the queue.
     *
     * @post Returns the number of elements in the queue
     * 
     * @return The number of elements within the queue.
     */
    public int size()
    {
        return data.size();
    }

    /**
     * Remove all the values from the queue.
     *
     * @post Removes all elements from the queue
     */
    public void clear()
    {
        data.clear();
    }

    /**
     * Determine if the queue is empty.
     *
     * @post Returns true iff the queue is empty
     * 
     * @return True iff the queue is empty.
     */
    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    public Iterator iterator()
    {
        return data.iterator();
    }

    /**
     * Construct a string representation of the queue.
     *
     * @post Returns string representation of queue
     * 
     * @return String representing the queue.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<QueueList:");
        Iterator li = data.iterator();
        while (li.hasNext())
        {
            s.append(" "+li.next());
        }
        s.append(">");
        return s.toString();
    }
}
