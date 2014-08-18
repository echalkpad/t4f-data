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
// A simple Set interface.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;

/**
 * Implementation of a set of elements.
 * As with the mathematical object, the elements of the set are
 * not duplicated.  No order is implied or enforced in this structure.
 *
 * @version $Id: Set.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 */
public interface Set extends Structure
{
    /**
     * Union other set into this set.
     * @pre other is non-null
     * @post values from other are added into this set
     */
    public void addAll(Structure other);

    /**
     * Check to see if this set is contained in the other structure.
     * @pre other is non-null
     * @post returns true if every value in set is in other
     */
    public boolean containsAll(Structure other);

    /**
     * Computes the difference between this set and the other structure
     * @pre other is non-null
     * @post values of this set contained in other are removed
     */
    public void removeAll(Structure other);

    /**
     * Computes the intersection between this set and the other structure.
     * @pre other is non-null
     * @post values not appearing in the other structure are removed
     */
    public void retainAll(Structure other);
}
