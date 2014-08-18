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
// An implementation of and ordered structure, based on vectors.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;

/**
 * Implementation of an ordered structure implemented using a vector
 *
 * @see io.datalayer.data.structure.Vector
 * @version $Id: OrderedVector.java 19 2006-08-10 04:52:00Z bailey $
 * @author, 2001 duane a. bailey
 */
public class OrderedVector extends AbstractStructure implements OrderedStructure
{
    /**
     * The vector of values.  Values are stored in increasing order
     */
    protected Vector data;
    /**
     * Construct an empty ordered vector
     *
     * @post constructs an empty, ordered vector
     */
    public OrderedVector()
    {
        data = new Vector();
    }

    /**
     * Add a comparable value to an ordered vector
     *
     * @pre value is non-null
     * @post inserts value, leaves vector in order
     * 
     * @param value The comparable value to be added to the ordered vector
     */
    public void add(Object value)
    {
        int position = locate((Comparable)value);
        data.add(position,value);
    }

    /**
     * Determine if a comparable value is a member of the ordered vector
     *
     * @pre value is non-null
     * @post returns true if the value is in the vector
     * 
     * @param value The comparable value sought
     * @return True if the value is found within the ordered vector
     */
    public boolean contains(Object value)
    {
        int position = locate((Comparable)value);
        return (position < size()) &&
               data.get(position).equals(value);
    }

    /**
     * Remove a comparable value from an ordered vector
     * At most one value is removed
     *
     * @pre value is non-null
     * @post removes one instance of value, if found in vector
     * 
     * @param value The comparable value to be removed
     * @return The actual comparable removed
     */
    public Object remove(Object value)
    {
        if (contains(value)) {
            // we know value is pointed to by locate
            int position = locate((Comparable)value);
            // since vector contains value, position < size()
            // keep track of the value for return
            Object target = data.get(position);
            // remove the value from the underlying vector
            data.remove(position);
            return target;
        }
        return null;
    }

    /**
     * Determine if the ordered vector is empty.        
     *
     * @post returns true if the OrderedVector is empty
     * 
     * @return True iff the ordered vector is empty
     */
    public boolean isEmpty()
    {
        return data.size() == 0;
    }

    /**
     * Removes all the values from a an ordered vector
     *
     * @post vector is emptied
     */
    public void clear()
    {
        data.setSize(0);
    }

    /**
     * Determine the number of elements within the ordered vector
     *
     * @post returns the number of elements in vector
     * 
     * @return The number of elements within the ordered vector
     */
    public int size()
    {
        return data.size();
    }

    /**
     * Construct an iterator to traverse the ordered vector in ascending
     * order
     *
     * @post returns an iterator for traversing vector
     * 
     * @return An iterator to traverse the ordered vector
     */
    public Iterator iterator()
    {
        return data.iterator();
    }

    protected int locate(Comparable target)
    {
        Comparable midValue;
        int low = 0;  // lowest possible location
        int high = data.size(); // highest possible location
        int mid = (low + high)/2; // low <= mid <= high
        // mid == high iff low == high
        while (low < high) {
            // get median value
            midValue = (Comparable)data.get(mid);
            // determine on which side median resides:
            if (midValue.compareTo(target) < 0) {
                low = mid+1;
            } else {
                high = mid;
            }
            // low <= high
            // recompute median index
            mid = (low+high)/2;
        }
        return low;
    }
    
    /**
     * Construct a string representation of an ordered vector
     *
     * @pre returns string representation of ordered vector
     * 
     * @return The string representing the ordered vector
     */
    public String toString()
    {
        return "<OrderedVector: "+data+">";
    }
}
