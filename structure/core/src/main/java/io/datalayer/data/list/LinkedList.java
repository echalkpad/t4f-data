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

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;

/**
 * A {@link List} implementation that stores elements in a doubly linked list.
 *
 */
public class LinkedList extends AbstractList {
    /** The sentinel pointing to the first and last elements of the list. */
    private final Element _headAndTail = new Element(null);

    /** The size of the list. */
    private int _size;

    /**
     * Default constructor.
     */
    public LinkedList() {
        clear();
    }

    public void insert(int index, Object value) throws IndexOutOfBoundsException {
        assert value != null : "value can't be null";

        if (index < 0 || index > _size) {
            throw new IndexOutOfBoundsException();
        }

        Element element = new Element(value);
        element.attachBefore(getElement(index));
        ++_size;
    }

    public Object delete(int index) throws IndexOutOfBoundsException {
        checkOutOfBounds(index);
        Element element = getElement(index);
        element.detach();
        --_size;
        return element.getValue();
    }

    public boolean delete(Object value) {
        assert value != null : "value can't be null";

        for (Element e = _headAndTail.getNext(); e != _headAndTail; e = e.getNext()) {
            if (value.equals(e.getValue())) {
                e.detach();
                --_size;
                return true;
            }
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
        _headAndTail.setPrevious(_headAndTail);
        _headAndTail.setNext(_headAndTail);
        _size = 0;
    }

    public Object set(int index, Object value) throws IndexOutOfBoundsException {
        assert value != null : "value can't be null";
        checkOutOfBounds(index);
        Element element = getElement(index);
        Object oldValue = element.getValue();
        element.setValue(value);
        return oldValue;
    }

    public Object get(int index) throws IndexOutOfBoundsException {
        checkOutOfBounds(index);
        return getElement(index).getValue();
    }

    public AosIterator iterator() {
        return new ValueIterator();
    }

    public int indexOf(Object value) {
        assert value != null : "value can't be null";

        int index = 0;

        for (Element e = _headAndTail.getNext(); e != _headAndTail; e = e.getNext()) {
            if (value.equals(e.getValue())) {
                return index;
            }

            ++index;
        }

        return -1;
    }

    public int size() {
        return _size;
    }

    /**
     * Obtains the element for a specified position.
     *
     * @param index The position (0, 1, 2...) for the desired element.
     * @return The element corresponding to the specified position.
     */
    private Element getElement(int index) {
        if (index < _size / 2) {
            return getElementForwards(index);
        } else {
            return getElementBackwards(index);
        }
    }

    /**
     * Obtains the element for a specified position starting at the first element and working forwards.
     *
     * @param index The position (0, 1, 2...) for the desired element.
     * @return The element corresponding to the specified position.
     */
    private Element getElementForwards(int index) {
        Element element = _headAndTail.getNext();

        for (int i = index; i > 0; --i) {
            element = element.getNext();
        }

        return element;
    }

    /**
     * Obtains the element for a specified position starting at the last element and working backwards.
     *
     * @param index The position (0, 1, 2...) for the desired element.
     * @return The element corresponding to the specified position.
     */
    private Element getElementBackwards(int index) {
        Element element = _headAndTail;

        for (int i = _size - index; i > 0; --i) {
            element = element.getPrevious();
        }

        return element;
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

    /**
     * Holds each contained value plus a reference to the next and previous elements.
     */
    private static final class Element {
        private Object _value;
        private Element _previous;
        private Element _next;

        public Element(Object value) {
            setValue(value);
        }

        public void setValue(Object value) {
            _value = value;
        }

        public Object getValue() {
            return _value;
        }

        public Element getPrevious() {
            return _previous;
        }

        public void setPrevious(Element previous) {
            assert previous != null : "previous can't be null";
            _previous = previous;
        }

        public Element getNext() {
            return _next;
        }

        public void setNext(Element next) {
            assert next != null : "next can't be null";
            _next = next;
        }

        public void attachBefore(Element next) {
            assert next != null : "next can't be null";

            Element previous = next.getPrevious();

            setNext(next);
            setPrevious(previous);

            next.setPrevious(this);
            previous.setNext(this);
        }

        public void detach() {
            _previous.setNext(_next);
            _next.setPrevious(_previous);
        }
    }

    /**
     * Iterator over the values in the list.
     */
    private final class ValueIterator implements AosIterator {
        private Element _current = _headAndTail;

        public void first() {
            _current = _headAndTail.getNext();
        }

        public void last() {
            _current = _headAndTail.getPrevious();
        }

        public boolean isDone() {
            return _current == _headAndTail;
        }

        public void next() {
            _current = _current.getNext();
        }

        public void previous() {
            _current = _current.getPrevious();
        }

        public Object current() throws IteratorOutOfBoundsException {
            if (isDone()) {
                throw new IteratorOutOfBoundsException();
            }
            return _current.getValue();
        }
    }
}
