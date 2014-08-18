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
package io.datalayer.data.hash;

/**
 * A {@link Hashtable} that uses linear probing to store and locate values.
 *
 */
public class LinearProbingHashtable implements Hashtable {
    /** The values. */
    private Object[] _values;

    /** The number of values in the table. */
    private int _size;

    /**
     * Constructor.
     *
     * @param initialCapacity The number of available slots.
     */
    public LinearProbingHashtable(int initialCapacity) {
        assert initialCapacity > 0 : "initialCapacity can't be < 1";
        _values = new Object[initialCapacity];
    }

    public void add(Object value) {
        ensureCapacityForOneMore();

        int index = indexFor(value);

        if (_values[index] == null) {
            _values[index] = value;
            ++_size;
        }
    }

    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    public int size() {
        return _size;
    }

    /**
     * Obtains the appropriate index for storing a new value.
     *
     * @param value The value to store.
     * @return The index.
     */
    private int indexFor(Object value) {
        int start = startingIndexFor(value);

        int index = indexFor(value, start, _values.length);
        if (index == -1) {
            index = indexFor(value, 0, start);
            assert index == -1 : "no free slots";
        }

        return index;
    }

    /**
     * Obtains the appropriate index for storing a new value.
     *
     * @param value The value to store.
     * @param start The index from which to start searching
     * @param end The index at which to stop searching
     * @return The index.
     */
    private int indexFor(Object value, int start, int end) {
        assert value != null : "value can't be null";

        for (int i = start; i < end; ++i) {
            if (_values[i] == null || value.equals(_values[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Obtains the index of an existing value.
     *
     * @param value The value to find.
     * @return The index; or <code>-1</code> if the value was not found.
     */
    private int indexOf(Object value) {
        int start = startingIndexFor(value);

        int index = indexOf(value, start, _values.length);
        if (index == -1) {
            index = indexOf(value, 0, start);
        }
        return index;
    }

    /**
     * Obtains the index of an existing value.
     *
     * @param value The value to find.
     * @param start The index from which to start searching
     * @param end The index at which to stop searching
     * @return The index; or <code>-1</code> if the value was not found.
     */
    private int indexOf(Object value, int start, int end) {
        assert value != null : "value can't be null";

        for (int i = start; i < end; ++i) {
            if (value.equals(_values[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Obtains the natural index for a given value based on its hash code.
     *
     * @param value The value for which the index is required.
     * @return The index.
     */
    private int startingIndexFor(Object value) {
        assert value != null : "value can't be null";
        return Math.abs(value.hashCode() % _values.length);
    }

    /**
     * Ensures there is enough room for one more value.
     */
    private void ensureCapacityForOneMore() {
        if (size() == _values.length) {
            resize();
        }
    }

    /**
     * Re-sizes the table.
     */
    private void resize() {
        LinearProbingHashtable copy = new LinearProbingHashtable(_values.length * 2);

        for (int i = 0; i < _values.length; ++i) {
            if (_values[i] != null) {
                copy.add(_values[i]);
            }
        }

        _values = copy._values;
    }
}
