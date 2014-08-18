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
// An implementation of queues, based on arrays.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;

/**
 * An implementation of a queue based on arrays.
 *
 * @version $Id: QueueArray.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class QueueArray extends AbstractQueue implements Queue
{
    /**
     * The references to values stored within the queue.
     */
    protected Object data[]; // an array of the data
    /**
     * index of the head of queue.
     */
    protected int head; // next dequeue-able value
    /**
     * current size of queue
     */
    protected int count; // current size of queue

    /**
     * Construct a queue holding at most size elements.
     *
     * @post create a queue capable of holding at most size values
     * 
     * @param size The maximum size of the queue.
     */
    public QueueArray(int size)
    {
        data = new Object[size];
        head = 0;
        count = 0;
    }

    /**
     * Add a value to the tail of the queue.
     *
     * @pre the queue is not full
     * @post the value is added to the tail of the structure
     * 
     * @param value The value added.
     * @see #enqueue
     */
    public void add(Object value)
    {
        Assert.pre(!isFull(),"Queue is not full.");
        int tail = (head + count) % data.length;
        data[tail] = value;
        count++;
    }

    /**
     * Remove a value from the head of the queue.
     *
     * @pre the queue is not empty
     * @post the head of the queue is removed and returned
     * 
     * @return The value actually removed.
     * @see #dequeue
     */
    public Object remove()
    {
        Assert.pre(!isEmpty(),"The queue is not empty.");
        Object value = data[head];
        head = (head + 1) % data.length;
        count--;
        return value;
    }

    /**
     * Fetch the value at the head of the queue.
     *
     * @pre the queue is not empty
     * @post the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public Object get()
    {
        Assert.pre(!isEmpty(),"The queue is not empty.");
        return data[head];
    }

    /**
     * Determine the number of elements within the queue
     *
     * @post returns the number of elements in the queue
     * 
     * @return The number of elements within the queue.
     */
    public int size()
    {
        return count;
    }

    /**
     * Remove all the values from the queue.
     *
     * @post removes all elements from the queue
     */
    public void clear()
    {
        // we could remove all the elements from the queue
        count = 0;
        head = 0;
    }
    
    /**
     * Determines if the queue is not able to accept any new values.
     *
     * @post returns true if the queue is at its capacity
     * 
     * @return True iff the queue is full.
     */
    public boolean isFull()
    {
        return count == data.length;
    }

    /**
     * Determine if the queue is empty.
     *
     * @post returns true iff the queue is empty
     * 
     * @return True iff the queue is empty.
     */
    public boolean isEmpty()
    {
        return count == 0;
    }

    public Iterator iterator()
    {
        return new ArrayIterator(data,head,count);
    }
    /**
     * Construct a string representation of the queue.
     *
     * @post returns string representation of queue
     * 
     * @return String representing the queue.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        int i,l;

        s.append("<QueueArray:");
        for (i = 0, l = head; i < count; i++, l = (l+1)%data.length)
        {
            s.append(" "+data[l]);
        }
        s.append(">");
        return s.toString();
    }
}
