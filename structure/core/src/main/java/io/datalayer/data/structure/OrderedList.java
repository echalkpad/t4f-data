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
// An implementation of an ordered structure, using lists
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;
import java.util.Comparator;
/**
 * A class that implements a collection of values that are kept in order.
 * Base values must be comparable.  Unlike Lists there is no notion of head
 * or tail.
 *
 * @version $Id: OrderedList.java 31 2007-08-06 17:19:56Z bailey $
 * @author, 2001 duane a. bailey
 */
public class OrderedList 
    extends AbstractStructure implements OrderedStructure
{
    /**
     * Pointer to the smallest element, maintained as a singly linked list
     */
    protected Node data; // smallest value
    /**
     * Number of elements in list
     */
    protected int count;        // number of values in list
    /**
     * The ordereding used to arange the values
     */
    protected Comparator ordering;      // the comparison function

    /**
     * Construct an empty ordered list
     *
     * @post constructs an empty ordered list
     */
    public OrderedList()
    {
        this(new NaturalComparator());
    }

    /**
     * Construct an empty ordered list with alternative ordering
     *
     * @param ordering the Comparator to be used in comparison
     * @post constructs an empty ordered list ordered by ordering
     */
    public OrderedList(Comparator ordering)
    {
        this.ordering = ordering;
        clear();
    }

    /**
     * Remove all the elements from the ordered list
     *
     * @post the ordered list is empty
     */
    public void clear()
    {
        data = null;
        count = 0;
    }
    
    /**
     * Add a value to the ordered list, keeping values in order
     *
     * @pre value is non-null
     * @post value is added to the list, leaving it in order
     * 
     * @param value The value to be added to the list
     */
    public void add(Object value)
    {
        Node previous = null; // element to adjust
        Node finger = data;   // target element

        // search for the correct location
        while ((finger != null) &&
               ordering.compare(finger.value(),value) < 0)
        {
            previous = finger;
            finger = finger.next();
        }
        // spot is found, insert
        if (previous == null) // check for insert at top
        {
            data = new Node(value,data);
        } else {
            previous.setNext(
               new Node(value,previous.next()));
        }
        count++;
    }

    /**
     * Determine if the ordered list contains a value
     *
     * @pre value is a non-null comparable object
     * @post returns true iff contains value
     * 
     * @param value The value sought in the list
     * @return The actual value found, or null, if not
     */
    public boolean contains(Object value)
    {
        Node finger = data; // target

        // search down list until we fall off or find bigger value
        while ((finger != null) &&
               ordering.compare(finger.value(),value) < 0)
        {
            finger = finger.next();
        }
        return finger != null && value.equals(finger.value());
    }

    /**
     * Remove a value from the ordered list.  At most one value
     * is removed.
     *
     * @pre value is non-null
     * @post an instance of value is removed, if in list
     * 
     * @param value The value to be removed
     * @return The actual value removed from the list
     */
    public Object remove(Object value)
    {
        Node previous = null; // element to adjust
        Node finger = data;      // target element

        // search for value or fall off list
        while ((finger != null) &&
               ordering.compare(finger.value(),value) < 0)
        {
            previous = finger;
            finger = finger.next();
        }
        // did we find it?
        if ((finger != null) && value.equals(finger.value())) {
            // yes, remove it
            if (previous == null)  // at top? 
            {
                data = finger.next();
            } else {
                previous.setNext(finger.next());
            }
            count--;
            // return value
            return finger.value();
        }
        // return nonvalue
        return null;
    }

    /**
     * Determine the number of elements in the list
     *
     * @pre returns the number of elements in the ordered list
     * 
     * @return The number of elements in the list
     */
    public int size()
    {
        return count;
    }

    /**
     * Determine if the list is empty
     *
     * @post returns true iff the size is non-zero
     * 
     * @return True if the ordered list is empty
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }
    /**
     * Construct an iterator for traversing elements of ordered list
     * in ascending order
     *
     * @post returns an iterator over ordered list
     * 
     * @return An iterator traversing elements in ascending order
     */
    public Iterator iterator()
    {
        return new SinglyLinkedListIterator(data);
    }

    /**
     * Generate string representation of the ordered list
     *
     * @post returns string representation of list
     * 
     * @return String representing ordered list
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<OrderedList:");
        Iterator si = iterator();
        while (si.hasNext())
        {
            s.append(" "+si.next());
        }
        s.append(">");
        return s.toString();
    }
}
