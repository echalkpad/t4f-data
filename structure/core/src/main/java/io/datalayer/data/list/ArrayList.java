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

import io.datalayer.data.iterable.ArrayIterator;
import io.datalayer.data.iterator.AosIterator;

/**
 * A {@link List} implementation that uses an array as the mechanism for storing elements.
 *
 */
public class ArrayList extends AbstractList {
    /** The initial capacity if none specified. */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /** The initial capacity to use. */
    private final int _initialCapacity;

    /** The underlying array. */
    private Object[] _array;

    /** The size of the list independent of the length of the array. */
    private int _size;

    /**
     * Default constructor.
     */
    public ArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructor.
     *
     * @param initialCapacity The initial capacity of the underlying array.
     */
    public ArrayList(int initialCapacity) {
        assert initialCapacity > 0 : "initialCapacity must be > 0";

        _initialCapacity = initialCapacity;
        clear();
    }

    /**
     * Constructor.
     *
     * @param array The array from which this list will be initialised.
     */
    public ArrayList(Object[] array) {
        assert array != null : "array can't be null";

        _initialCapacity = array.length;
        clear();

        System.arraycopy(array, 0, _array, 0, array.length);
        _size = array.length;
    }

    public void insert(int index, Object value) throws IndexOutOfBoundsException {
        assert value != null : "value can't be null";

        if (index < 0 || index > _size) {
            throw new IndexOutOfBoundsException();
        }

        ensureCapacity(_size + 1);
        System.arraycopy(_array, index, _array, index + 1, _size - index);
        _array[index] = value;
        ++_size;
    }

    public Object delete(int index) throws IndexOutOfBoundsException {
        checkOutOfBounds(index);
        Object value = _array[index];
        int copyFromIndex = index + 1;
        if (copyFromIndex < _size) {
            System.arraycopy(_array, copyFromIndex, _array, index, _size - copyFromIndex);
        }
        _array[--_size] = null;
        return value;
    }

    public boolean delete(Object value) {
        int index = indexOf(value);
        if (index != -1) {
            delete(index);
            return true;
        }
        return false;
    }

    public void add(Object value) {
        insert(size(), value);
    }

    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void clear() {
        _array = new Object[_initialCapacity];
        _size = 0;
    }

    public Object set(int index, Object value) throws IndexOutOfBoundsException {
        assert value != null : "value can't be null";
        checkOutOfBounds(index);
        Object oldValue = _array[index];
        _array[index] = value;
        return oldValue;
    }

    public Object get(int index) throws IndexOutOfBoundsException {
        checkOutOfBounds(index);
        return _array[index];
    }

    public int indexOf(Object value) {
        assert value != null : "value can't be null";

        for (int i = 0; i < _size; ++i) {
            if (value.equals(_array[i])) {
                return i;
            }
        }

        return -1;
    }

    public AosIterator iterator() {
        return new ArrayIterator(_array, 0, _size);
    }

    public int size() {
        return _size;
    }

    /**
     * Ensures the internal array is large enough to accomodate a specific number of elements.
     *
     * @param capacity The number of elements to support.
     */
    private void ensureCapacity(int capacity) {
        assert capacity > 0 : "capacity must be > 0";

        if (_array.length < capacity) {
            Object[] copy = new Object[capacity + capacity / 2];
            System.arraycopy(_array, 0, copy, 0, _size);
            _array = copy;
        }
    }

    /**
     * Checks if a specified position is outside the bounds of the list.
     *
     * @param index The index to check.
     * @throws IndexOutOfBoundsException if the specified index is outside the bounds of the list.
     */
    private void checkOutOfBounds(int index) {
        if (isOutOfBounds(index)) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Determines if the specified index is outside the bounds of the list.
     *
     * @param index The index to check.
     * @return <code>true</code> if outside the bounds; otherwise <code>false</code>.
     */
    private boolean isOutOfBounds(int index) {
        return index < 0 || index >= size();
    }
}
