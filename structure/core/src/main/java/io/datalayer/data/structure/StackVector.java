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
 * An implementation of stacks using Vectors.
 * 
 * @version $Id: StackVector.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class StackVector extends AbstractStack implements Stack
{
    /**
     * The vector containing the stack data.
     */
    protected Vector data;

    /**
     * Construct an empty stack.
     *
     * @post an empty stack is created
     */
    public StackVector()
    {
        data = new Vector();
    }

    /**
     * Construct a stack with initial capacity
     * Vector will grow if the stack fills vector.
     *
     * @post an empty stack with initial capacity of size is created
     * 
     * @param size The initial capacity of the vector.
     */
    public StackVector(int size)
    {
        data = new Vector(size);
    }

    /**
     * Add an element from the top of the stack.
     *
     * @post item is added to stack
     *       will be popped next if no intervening add
     * 
     * @param item The element to be added to the stack top.
     */
    public void add(Object item)
    {
        data.add(item);
    }

    /**
     * Remove an element from the top of the stack.
     *
     * @pre stack is not empty
     * @post most recently added item is removed and returned
     * 
     * @return The item removed from the top of the stack.
     * @see #pop
     */
    public Object remove()
    {
        return data.remove(size()-1);
    }

    /**
     * Fetch a reference to the top element of the stack.
     *
     * @pre stack is not empty
     * @post top value (next to be popped) is returned
     * 
     * @return A reference to the top element of the stack.
     */
    public Object get()
    {
        // raise an exception if stack is already empty
        return data.get(size()-1);
    }

    /**
     * Returns true iff the stack is empty.  Provided for
     * compatibility with java.util.Vector.empty.
     *
     * @post returns true if and only if the stack is empty
     * 
     * @return True iff the stack is empty.
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Determine the number of elements in stack.
     * 
     * @post returns the number of elements in stack
     * 
     * @return The number of elements in stack.
     */
    public int size()
    {
        return data.size();
    }

    /**
     * Remove all elements from stack.
     *
     * @post removes all elements from stack
     */
    public void clear()
    {
        data.clear();
    }

    public Iterator iterator()
    {
        return data.iterator();
    }

    /**
     * Construct a string representation of stack.
     *
     * @post returns a string representation of stack
     * 
     * @return A string representing the stack.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        int i;

        sb.append("<StackVector:");
        for (i = data.size()-1; i >= 0; i--)
        {
            sb.append(" "+i);
        }
        return sb.toString()+">";
    }
}
