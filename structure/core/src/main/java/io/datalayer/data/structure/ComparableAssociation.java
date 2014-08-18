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
// A class for structuring associations that may be compared.
// (c) 1998, 2001 duane a. bailey
package io.datalayer.data.structure;

/**
 * An association that can be compared.
 *
 * @version $Id: ComparableAssociation.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public class ComparableAssociation extends Association
    implements Comparable
{
    /**
     * Construct an association that can be ordered, from only a key.
     * The value is set to null.
     *
     * @pre key is non-null
     * @post constructs comparable association with null value
     * 
     * @param key The (comparable) key.
     */
    public ComparableAssociation(Comparable key)
    {
        this(key,null);
    }

    /**
     * Construct a key-value association that can be ordered.
     *
     * @pre key is non-null
     * @post constructs association between a comparable key and a value
     * 
     * @param key The (comparable) key.
     * @param value The (possibly comparable) associated value.
     */
    public ComparableAssociation(Comparable key, Object value)
    {
        super(key,value);
    }

    /**
     * Determine the order of two comparable associations, based on key.
     *
     * @pre other is non-null ComparableAssociation
     * @post returns integer representing relation between values
     * 
     * @param other The other comparable association.
     * @return Value less-than equal to or greater than zero based on comparison
     */
    public int compareTo(Object other)
    {
        Assert.pre(other instanceof ComparableAssociation,
                   "compareTo expects a ComparableAssociation");
        ComparableAssociation that = (ComparableAssociation)other;
        Comparable thisKey = (Comparable)this.getKey();
        Comparable thatKey = (Comparable)that.getKey();

        return thisKey.compareTo(thatKey);
    }

    /**
     * Construct a string representation of the ComparableAssociation.
     *
     * @post returns string representation
     * 
     * @return The string representing the ComparableAssociation.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<ComparableAssociation: "+getKey()+"="+getValue()+">");
        return s.toString();
    }
}
