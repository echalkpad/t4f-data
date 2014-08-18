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
import java.util.Iterator;
public abstract class AbstractList 
    extends AbstractStructure implements List
{
    /**
     * Default constructor for AbstractLists
     * @post does nothing
     */
    public AbstractList()
    {
    }

    /**
     * Determine if list is empty.
     *
     * @post returns true iff list has no elements
     * 
     * @return True if list has no elements.
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Add a value to head of list.
     *
     * @post value is added to beginning of list
     * 
     * @param value The value to be added to head of list.
     */
    public void addFirst(Object value)
    {
        add(0,value);
    }

    /**
     * Add a value to tail of list.
     *
     * @post value is added to end of list
     * 
     * @param value The value to be added to tail of list.
     */
    public void addLast(Object value)
    {
        add(size(),value);
    }

    /**
     * Fetch first element of list.
     *
     * @pre list is not empty
     * @post returns first value in list
     * 
     * @return A reference to first element of list.
     */
    public Object getFirst()
    {
        return get(0);
    }

    /**
     * Fetch last element of list.
     *
     * @pre list is not empty
     * @post returns last value in list
     * 
     * @return A reference to last element of list.
     */
    public Object getLast()
    {
        return get(size()-1);
    }

    /**
     * Remove a value from first element of list.
     *
     * @pre list is not empty
     * @post removes first value from list
     * 
     * @return value actually removed.
     */
    public Object removeFirst()
    {
        return remove(0);
    }

    /**
     * Remove last value from list.
     *
     * @pre list is not empty
     * @post removes last value from list
     * 
     * @return value actually removed.
     */
    public Object removeLast()
    {
        return remove(size()-1);
    }

    /**
     * Add an object to tail of list.
     *
     * @post value is added to tail of list
     * 
     * @param value The value to be added to tail of list.
     * @see #addLast
     */
    public void add(Object value)
    {
        addLast(value);
    }

    /**
     * Removes value from tail of list.
     *
     * @pre list has at least one element
     * @post removes last value found in list
     * @return object removed.
     */
    public Object remove()
    {
        return removeLast();
    }

    /**
     * Retrieves value from tail of list.
     *
     * @pre list has at least one element
     * @post returns last value found in list
     * @return object found at end of list
     */
    public Object get()
    {
        return getLast();
    }

    /**
     * Check to see if a value is in list.
     *
     * @pre value is not null
     * @post returns true iff list contains an object equal to value
     * 
     * @param value value sought.
     * @return True if value is within list.
     */
    public boolean contains(Object value)
    {
        return -1 != indexOf(value);
    }
}

