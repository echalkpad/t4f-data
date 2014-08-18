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
package io.datalayer.data.structure;
/**
 * A private iterator for traversing a doubly linked list.
 *
 * @version $Id: DoublyLinkedListIterator.java 31 2007-08-06 17:19:56Z bailey $
 * @author, 2001 duane a. bailey
 */
public class DoublyLinkedListIterator extends AbstractIterator
{
    /**
     * Reference to head of the list.
     */
    protected DoublyLinkedNode head;

    /**
     * Sign of the end of the list.
     */
    protected DoublyLinkedNode tail;

    /**
     * Reference to the current node in the list.
     */
    protected DoublyLinkedNode current;

    /**
     * Construct an iterator over a doubly linked list hanging from head.
     *
     * @post constructs an iterator rooted at list head, h
     * 
     * @param h The head of the list to be traversed.
     */
    public DoublyLinkedListIterator(DoublyLinkedNode h)
    {
        head = h;
        tail = null;
        reset();
    }


    public DoublyLinkedListIterator(DoublyLinkedNode headDummy, 
                                    DoublyLinkedNode tailDummy)
    {
        head = headDummy.next();
        tail = tailDummy;
        reset();
    }

    /**
     * Reset the iterator to the head of the list.
     *
     * @post resets iterator to list head
     */
    public void reset()
    {
        current = head;
    }

    /**
     * Determine if there are more elements to be considered.
     *
     * @post returns true iff current element is valid
     * 
     * @return True iff there are more elements to be considered.
     */
    public boolean hasNext()
    {
        return current != tail;
    }

    /**
     * Returns reference to the current element, then increments iterator.
     *
     * @post returns current element and increments iterator
     * 
     * @return Reference to element that was current before increment.
     */
    public Object next()
    {
        Object result = current.value();
        current = current.next();
        return result;
    }

    /**
     * Get reference to value that is current.
     *
     * @pre hasNext
     * @post returns current element
     * 
     * @return A reference to the value that is current.
     */
    public Object get()
    {
        return current.value();
    }
}
