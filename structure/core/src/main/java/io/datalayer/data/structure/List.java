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
// Interface for lists.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;
/**
 * Interface describing lists.  Lists are collections of data with
 * a head and tail.  Values may be added or removed from either end,
 * as well as by value from the middle.
 *
 * @version $Id: List.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 * @see SinglyLinkedList
 * @see DoublyLinkedList
 * @see CircularList
 */
public interface List extends Structure
{
    /**
     * Determine size of list.
     *
     * @post returns number of elements in list
     * 
     * @return The number of elements in list.
     */
    public int size();

    /**
     * Determine if list is empty.
     *
     * @post returns true iff list has no elements
     * 
     * @return True if list has no elements.
     */
    public boolean isEmpty();

    /**
     * Remove all elements of list.
     *
     * @post empties list
     */
    public void clear();

    /**
     * Add a value to the head of the list.
     *
     * @post value is added to beginning of list
     * 
     * @param value The value to be added to the head of the list.
     */
    public void addFirst(Object value);

    /**
     * Add a value to tail of list.
     *
     * @post value is added to end of list
     * 
     * @param value The value to be added to tail of list.
     */
    public void addLast(Object value);

    /**
     * Fetch first element of list.
     *
     * @pre list is not empty
     * @post returns first value in list
     * 
     * @return A reference to first element of list.
     */
    public Object getFirst();

    /**
     * Fetch last element of list.
     *
     * @pre list is not empty
     * @post returns last value in list
     * 
     * @return A reference to last element of list.
     */
    public Object getLast();

    /**
     * Remove a value from first element of list.
     *
     * @pre list is not empty
     * @post removes first value from list
     * 
     * @return The value actually removed.
     */
    public Object removeFirst();

    /**
     * Remove last value from list.
     *
     * @pre list is not empty
     * @post removes last value from list
     * 
     * @return The value actually removed.
     */
    public Object removeLast();

    /**
     * Remove a value from list.  At most one of value
     * will be removed.
     *
     * @post removes and returns element equal to value
     *       otherwise returns null
     * 
     * @param value The value to be removed.
     * @return The actual value removed.
     */
    public Object remove(Object value);

    /**
     * Add an object to tail of list.
     *
     * @post value is added to tail of list
     * 
     * @param value The value to be added to tail of list.
     * @see #addLast
     */
    public void add(Object value);

    /**
     * Removes value from tail of list.
     *
     * @pre list has at least one element
     * @post removes last value found in list
     * @return object removed.
     */
    public Object remove();

    /**
     * Retrieves value from tail of list.
     *
     * @pre list has at least one element
     * @post returns last value found in list
     * @return object found at end of list
     */
    public Object get();

    /**
     * Check to see if a value is in list.
     *
     * @pre value is not null
     * @post returns true iff list contains an object equal to value
     * 
     * @param value value sought.
     * @return True if value is within list.
     */
    public boolean contains(Object value);

    /**
     * Determine first location of a value in list.
     *
     * @pre value is not null
     * @post returns (0-origin) index of value,
     *   or -1 if value is not found
     * 
     * @param value The value sought.
     * @return index (0 is first element) of value, or -1
     */
    public int indexOf(Object value);

    /**
     * Determine last location of a value in list.
     *
     * @pre value is not null
     * @post returns (0-origin) index of value,
     *   or -1 if value is not found
     * 
     * @param value value sought.
     * @return index (0 is first element) of value, or -1
     */
    public int lastIndexOf(Object value);

    /**
     * Get value at location i.
     *
     * @pre 0 <= i < size()
     * @post returns object found at that location
     *
     * @param i position of value to be retrieved.
     * @return value retrieved from location i (returns null if i invalid)
     */
    public Object get(int i);

    /**
     * Set value stored at location i to object o, returning old value.
     *
     * @pre 0 <= i < size()
     * @post sets ith entry of list to value o;
     *    returns old value
     * @param i location of entry to be changed.
     * @param o new value
     * @return former value of ith entry of list.
     */
    public Object set(int i, Object o);

    /**
     * Insert value at location.
     *
     * @pre 0 <= i <= size()
     * @post adds ith entry of list to value o
     * @param i index of this new value
     * @param o value to be stored
     */
    public void add(int i, Object o);

    /**
     * Remove and return value at location i.
     *
     * @pre 0 <= i < size()
     * @post removes and returns object found at that location
     *
     * @param i position of value to be retrieved.
     * @return value retrieved from location i (returns null if i invalid)
     */
    public Object remove(int i);

    /**
     * Construct an iterator to traverse elements of list
     * from head to tail, in order.
     *
     * @post returns an iterator allowing 
     *   ordered traversal of elements in list
     * 
     * @return Iterator that traverses list.
     */
    public Iterator iterator();
}

