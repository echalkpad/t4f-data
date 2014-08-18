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
// A comparator that implements the natural ordering.
// (c) 2001 duane a. bailey

package io.datalayer.data.structure;

import java.util.Comparator;
public class NaturalComparator implements Comparator
{
    /**
     * Compare two values, a and b.  Simply calls the default
     * compareTo method for a on b.
     * @param a object performing the compare
     * @param b the object being compared
     * @pre a, b non-null, and b is same type as a
     * @post returns value <, ==, > 0 if a <, ==, > b
     * @return value <, ==, > 0 if a <, ==, > b using a.compareTo
     */
    public int compare(Object a, Object b)
    {
        return ((Comparable)a).compareTo(b);
    }

    /**
     * Returns true if the other object is a NaturalComparator.
     * @param b a possible NaturalComparator
     * @post returns true if b is a NaturalComparator
     * @return true if b is a NaturalComparator
     */
    public boolean equals(Object b)
    {
        return (b != null) && (b instanceof NaturalComparator);
    }
}
