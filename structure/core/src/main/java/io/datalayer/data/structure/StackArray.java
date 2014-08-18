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
// An implementation of stacks, using vectors.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;

/**
 * An implementation of a stack using an array.
 *
 * @version $Id: StackArray.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class StackArray extends AbstractStack implements Stack
{
    /**
     * An index to the top element of the stack.
     */
    protected int top;
    /**
     * The array of value references.  Top of the stack
     * is higher in array.
     */
    protected Object data[];
    /**
     * Construct a stack capable of holding at least size elements.
     *
     * @post an empty stack with initial capacity of size is created
     * 
     * @param size The maximum size of the stack.
     */
    public StackArray(int size)
    {
        data = new Object[size];
        clear();
    }

    /**
     * Remove all elements from the stack.
     *
     * @post removes all elements from stack
     */
    public void clear()
    {
        top = -1;
    }

    /**
     * Add a value to the top of the stack.
     *
     * @post adds an element to stack;
     *       Will be next element popped if no intervening push
     * 
     * @param item The value to be added.
     * @see #push
     */
    public void add(Object item)
    {
        Assert.pre(!isFull(),"Stack is not full.");
        top++;
        data[top] = item;
    }    

    /**
     * Remove a value from the top of the stack.
     *
     * @pre stack is not empty
     * @post removes and returns the top element from stack;
     * 
     * @return The value removed from the top of the stack.
     * @see #pop
     */
    public Object remove()
    {
        Assert.pre(!isEmpty(),"Stack is not empty.");
        Object result = data[top];
        data[top] = null;
        top--;
        return result;
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
        // raise an exception if stack is already empty
        Assert.pre(!isEmpty(),"Stack is not empty.");
        return data[top];
    }

    public Iterator iterator()
    {
        return new ArrayIterator(data,0,size());
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
        return top+1;
    }

    /**
     * Determine if the stack is empty.
     *
     * @post returns true iff the stack is empty
     * 
     * @return True iff the stack is empty.
     * @see #empty
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Determine if the stack is full.
     *
     * @post returns true iff the stack is empty
     * 
     * @return True iff there is no more room in the stack.
     */
    public boolean isFull()
    {
        return top == (data.length-1);
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
        StringBuffer sb = new StringBuffer();
        int i;
        sb.append("<StackArray: ");
        for (i = top; i >= 0; i--)
        {
            sb.append(" "+data[i]);
        }
        sb.append(">");
        return sb.toString();
    }
}
