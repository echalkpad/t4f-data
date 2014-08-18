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
// A simple CharSet class.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;

/**
 * Implementation of a set of characters whose ASCII values are between
 * 0 and 255, inclusive.  This set is specialized for use with characters.
 * It is implemented using the BitSet class.
 * 
 * @version $Id: CharSet.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 * @see io.datalayer.data.structure.BitSet
 */
public class CharSet
{
    protected BitSet s; // underlying set of bits 0..255

    /**
     * Constructs an empty charset.
     *
     * @post constructs an empty set of characters
     */
    public CharSet()
    {   
        s = new BitSet();
    }
    
    /**
     * Adds character c to set if not already there.
     *
     * @post adds c to set
     * 
     * @param c The character to be added to set.
     */
    public void add(char c)
    {
        s.add((int)c);
    }

    /**
     * Removes a character from set.  Does nothing if char not in set.
     *
     * @post removes c from set, if present
     * 
     * @param c The character to be removed.
     */
    public void remove(char c)
    {
        s.remove((int)c);
    }

    /**
     * Detects whether c is a member of this set.
     *
     * @post returns true iff c in set
     * 
     * @param c The char sought.
     * @return True iff c is a member of this set.
     */
    public boolean contains(char c)
    {
        return s.contains((int)c);
    }

    /**
     * Removes the characters of the set.
     *
     * @post removes all characters from set
     */
    public void clear()
    {
        s.clear();
    }

    /**
     * Construct a duplicate of the character set.
     *
     * @post constructs a copy of this set
     * 
     * @return Returns a duplicate of this character set.
     */
    public Object clone()
    {
        CharSet duplicate = new CharSet();
        duplicate.s = (BitSet)s.clone();
        return duplicate;
    }

    /**
     * Constructs a charset that is the union of this and other.
     *
     * @pre other is not null
     * @post returns new set with characters from this or other
     * 
     * @param other The other character set.
     * @return The result of the union --- contains c if in either set.
     */
    public Object union(CharSet other)
    {
        return s.union(other.s);
    }

    /**
     * Computes the intersection of this charset and other.
     *
     * @pre other is not null
     * @post returns new set with characters from this and other
     * 
     * @param other The other character set.
     * @return The intersection of this and other --- char in result if in both.
     */
    public Object intersection(CharSet other)
    {
        return s.intersection(other.s);
    }

    /**
     * Computes the difference between this and other charset.
     *
     * @pre other is not null
     * @post returns new set with characters from this but not other
     * 
     * @param other The other character set.
     * @return the result of difference --- chars in this but not other.
     */
    public Object difference(CharSet other)
    {
        return s.difference(other.s);
    }

    /**
     * Detects if this set within the other.
     *
     * @pre other is not null
     * @post returns true if this is a subset of other
     * 
     * @param other The potential superset.
     * @returns True if every element of this is in other.
     */
    public boolean subset(CharSet other)
    {
        return s.subset(other.s);
    }

    /**
     * Detect an empty charset.
     *
     * @post returns true iff set is empty
     * 
     * @return True if this charset is empty.
     */
    public boolean isEmpty()
    {
        return s.isEmpty();
    }

    /**
     * Detect if two sets are equal.
     *
     * @pre other is not null
     * @post returns true if this contains same values as other
     * 
     * @param other  The other set.
     * @return  True if this set and other set are identical.
     */
    public boolean equals(Object other)
    {
        return s.equals(((CharSet)other).s);
    }

    /**
     * Compute String representation of charset.
     *
     * @post returns string representation of set
     * 
     * @return String representing this charset.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        sb.append("<CharSet:");
        for (i = 0; s.probe(i); i++) {
            if (s.contains(i)) {
                if (i < ' ' || i > '~') {
                    sb.append(" "+(char)i);
                } else {
                    sb.append(" (char)"+i);
                }
            }
        }
        sb.append(">");
        return s.toString();
    }
}
