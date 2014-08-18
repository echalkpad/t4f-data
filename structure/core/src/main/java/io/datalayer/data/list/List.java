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
package io.datalayer.data.list;

import io.datalayer.data.iterable.Iterable;

/**
 * A generic interface for lists.
 *
 */
public interface List extends Iterable {
    /**
     * Inserts a value into the list at a specified position. The size of the list will increase by one.
     *
     * @param index The position (0, 1, 2...) at which the value should be inserted.
     * @param value The value to add.
     * @throws IndexOutOfBoundsException if the specified position falls outside the range
     *                                   (index &lt; 0 || index &gt;= size()).
     */
    public void insert(int index, Object value) throws IndexOutOfBoundsException;

    /**
     * Adds a value to the end of the list. The size of the list will increase by one.
     *
     * @param value The value to be added.
     */
    public void add(Object value);

    /**
     * Deletes the value from a specified position in the list. The size of the list will decrease by one.
     *
     * @param index The position (0, 1, 2...) from which the value should be deleted.
     * @return The value that was contained at the specified position.
     * @throws IndexOutOfBoundsException if the specified position falls outside the range
     *                                   (index &lt; 0 || index &gt;= size()).
     */
    public Object delete(int index) throws IndexOutOfBoundsException;

    /**
     * Deletes the first ocurrence of a specified value. The size of the list will decrease by one if the value is
     * found.
     *
     * @param value The value to be deleted.
     * @return <code>true</code> if removed; otherwise <code>false</code> if not found.
     */
    public boolean delete(Object value);

    /**
     * Deletes all elements from the list. The size of the list will be reset to zero (0).
     */
    public void clear();

    /**
     * Sets the value at a specified position in the List.
     *
     * @param index The position (0, 1, 2...) from which the value should be obtained.
     * @param value The value to set at the specified position.
     * @return The value originally at the specified position.
     * @throws IndexOutOfBoundsException if the specified position falls outside the range
     *                                   (index &lt; 0 || index &gt;= size()).
     */
    public Object set(int index, Object value) throws IndexOutOfBoundsException;

    /**
     * Obtains the value from a specified position in the List.
     *
     * @param index The position (0, 1, 2...) from which the value should be obtained.
     * @return The value at the specified position.
     * @throws IndexOutOfBoundsException if the specified position falls outside the range
     *                                   (index &lt; 0 || index &gt;= size()).
     */
    public Object get(int index) throws IndexOutOfBoundsException;

    /**
     * Obtains the position of the first occurrence of a specified value within a list.
     *
     * @param value The value for which the index is required.
     * @return The position (0, 1, 2...) of the first occurrence of value; otherwise -1.
     */
    public int indexOf(Object value);

    /**
     * Determines if the list contains a specified value.
     *
     * @param value The value for which to search.
     * @return <code>true</code> if the value is found; otherwise <code>false</code>.
     */
    public boolean contains(Object value);

    /**
     * Obtains the number of elements in the list.
     *
     * @return The number of elements in the list.
     */
    public int size();

    /**
     * Determines if the list is empty or not.
     *
     * @return <code>true</code> if the list is empty (<code>size() == 0</code>); otherwise returns <code>false</code>.
     */
    public boolean isEmpty();
}
