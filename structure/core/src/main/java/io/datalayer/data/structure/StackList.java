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
// An implementation of stacks using lists.
// (c) 1998,2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;

/**
 * An implementation of a stack, based on lists.  The head of the
 * stack is stored at the head of the list.
 * 
 * @version $Id: StackList.java 32 2007-08-06 19:40:38Z bailey $
 * @author, 2001 duane a. bailey
 */
public class StackList extends AbstractStack implements Stack
{
    /**
     * The list that maintains the stack data.
     */
    protected List data;

    /**
     * Construct an empty stack.
     *
     * @post constructs a new stack, based on lists
     */
    public StackList()
    {
        // Think about why we use singly linked lists here:
        // They're simple, and take less space.
        data = new SinglyLinkedList();
    }
    
    /**
     * Remove all elements from the stack.
     *
     * @post removes all elements from stack
     */
    public void clear()
    {
        data.clear();
    }

    /**
     * Determine if the stack is empty.
     * Provided for compatibility with java.util.Stack.empty.
     *
     * @post returns true iff the stack is empty
     * 
     * @return True iff the stack is empty.
     * @see #isEmpty
     */
    public boolean empty()
    {
        return data.isEmpty();
    }

    public Iterator iterator()
    {
        return data.iterator();
    }


    /**
     * Get a reference to the top value in the stack.
     *
     * @pre stack is not empty
     * @post returns the top element (most recently pushed) from stack
     * 
     * @return A reference to the top element of the top of the stack.
     */
    public Object get()
    {
        return data.getFirst();
    }

    /**
     * Add a value to the top of the stack.
     *
     * @post adds an element to stack;
     *       will be next element popped if no intervening push
     * 
     * @param item The value to be added.
     * @see #push
     */
    public void add(Object value)
    {
        data.addFirst(value);
    }

    /**
     * Remove a value from the top of the stack.
     *
     * @pre stack is not empty
     * @post removes and returns the top element from stack
     * 
     * @return The value removed from the top of the stack.
     * @see #pop
     */
    public Object remove()
    {
        return data.removeFirst();
    }

    /**
     * Determine the number of elements in the stack.
     *
     * @post returns the size of the stack
     * 
     * @return The number of values within the stack.
     */
    public int size()
    {
        return data.size();
    }

    /**
     * Construct a string representation of the stack.
     *
     * @post returns a string representation of stack
     * 
     * @return A string representing the stack.
     */
    public String toString()
    {
        return "<StackList: "+data+">";
    }
}
