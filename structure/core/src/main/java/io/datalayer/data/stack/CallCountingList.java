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
package io.datalayer.data.stack;

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.List;

/**
 * A {@link List} that counts calls made to it.
 *
 * TODO: Move to sorting package.
 * TODO: Delete changed to return boolean!
 * TODO: Order of add changed changed!
 */
public class CallCountingList implements List {
    /** The underlying list. */
    private final List _list;

    private int _insertCount;
    private int _addCount;
    private int _deleteCount;
    private int _getCount;
    private int _setCount;

    /**
     * Constructor.
     *
     * @param list The underlying list.
     */
    public CallCountingList(List list) {
        assert list != null : "list can't be null";
        _list = list;
    }

    public void insert(int index, Object value) throws IndexOutOfBoundsException {
        ++_insertCount;
        _list.insert(index, value);
    }

    public void add(Object value) {
        ++_addCount;
        _list.add(value);
    }

    public Object delete(int index) throws IndexOutOfBoundsException {
        ++_deleteCount;
        return _list.delete(index);
    }

    public boolean delete(Object value) {
        ++_deleteCount;
        return _list.delete(value);
    }

    public Object get(int index) throws IndexOutOfBoundsException {
        ++_getCount;
        return _list.get(index);
    }

    public Object set(int index, Object value) throws IndexOutOfBoundsException {
        ++_setCount;
        return _list.set(index, value);
    }


    public void clear() {
        _list.clear();
    }

    public int indexOf(Object value) {
        return _list.indexOf(value);
    }

    public boolean contains(Object value) {
        return _list.contains(value);
    }

    public boolean isEmpty() {
        return _list.isEmpty();
    }

    public AosIterator iterator() {
        return _list.iterator();
    }

    public int size() {
        return _list.size();
    }

    public String toString() {
        return new StringBuffer("Call-counting List: ")
                .append("add: " + _addCount)
                .append(" insert: " + _insertCount)
                .append(" delete: " + _deleteCount)
                .append(" set: " + _setCount)
                .append(" get: " + _getCount).toString();
    }
}
