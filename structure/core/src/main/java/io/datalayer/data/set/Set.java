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
package io.datalayer.data.set;

import io.datalayer.data.iterable.Iterable;

/**
 * Generic interface for sets.
 *
 */
public interface Set extends Iterable {
    /**
     * Determines if a value exists in the set.
     *
     * @param value The value for which to search.
     * @return <code>true</code> if found; otherwise <code>false</code>.
     */
    public boolean contains(Object value);

    /**
     * Adds a value to the set.
     *
     * @param value The value to add.
     * @return <code>true</code> if added; otherwise <code>false</code> if already existed.
     */
    public boolean add(Object value);

    /**
     * Removes a value from the set.
     *
     * @param value The value to delete.
     * @return <code>true</code> if removed; otherwise <code>false</code> if not found.
     */
    public boolean delete(Object value);

    /**
     * Deletes all values from the set. The size of the set will be reset to zero (0).
     */
    public void clear();

    /**
     * Obtains the size of the set.
     *
     * @return The number of values in the set.
     */
    public int size();

    /**
     * Determines if the set is empty or not.
     *
     * @return <code>true</code> if the set is empty (<code>size() == 0</code>); otherwise returns <code>false</code>.
     */
    public boolean isEmpty();
}
