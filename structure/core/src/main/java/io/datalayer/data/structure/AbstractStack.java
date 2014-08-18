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
// An abstract implementation of stacks.
// (c) 1998,2001 duane a. bailey

package io.datalayer.data.structure;

/**
 * An abstract structure describing a Last-In, First-Out structure.
 * Stacks are typically used to store the state of a recursively
 * solved problem.
 * 
 * @version $Id: AbstractStack.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public abstract class AbstractStack extends AbstractLinear implements Stack 
{
    /**
     * Add an element from the top of the stack.
     *
     * @post item is added to stack
     *       will be popped next if no intervening add
     * 
     * @param item The element to be added to the stack top.
     */
    public void push(Object item)
    {
        add(item);
    }

    /**
     * Remove an element from the top of the stack.
     *
     * @pre stack is not empty
     * @post most recently added item is removed and returned
     * 
     * @return The item removed from the top of the stack.
     */
    public Object pop()
    {
        return remove();
    }

    /**
     * Fetch a reference to the top element of the stack.
     * @pre stack is not empty
     * @post top value (next to be popped) is returned
     * @deprecated Please use method get, rather than getFirst!
     * @return A reference to the top element of the stack.
     */
    public Object getFirst()
    {
        return get();
    }

    /**
     * Fetch a reference to the top element of the stack.
     * Provided for compatibility with java.util.Stack.
     * @pre stack is not empty
     * @post top value (next to be popped) is returned
     * 
     * @return A reference to the top element of the stack.
     */
    public Object peek()
    {
        return get();
    }
}
