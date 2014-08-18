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
package io.datalayer.data.map;

import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;

/**
 * A {@link Map} that uses a binary search tree.
 *
 */
public class TreeMap implements Map {
    /** The strategy to use for value comparison. */
    private final Comparator _comparator;

    /** The root node; or <code>null</code> if the tree is empty. */
    private Node _root;

    /** The number of values in the set. */
    private int _size;

    /**
     * Default constructor. Assumes keys implement {@link Comparable}.
     */
    public TreeMap() {
        this(NaturalComparator.INSTANCE);
    }

    /**
     * Constructor.
     *
     * @param comparator The strategy to use for key comparison.
     */
    public TreeMap(Comparator comparator) {
        assert comparator != null : "comparator can't be null";
        _comparator = comparator;
    }

    public boolean contains(Object key) {
        return search(key) != null;
    }

    public Object get(Object key) {
        Node node = search(key);
        return node != null ? node.getValue() : null;
    }

    public Object set(Object key, Object value) {
        Node parent = null;
        Node node = _root;
        int cmp = 0;

        while (node != null) {
            parent = node;
            cmp = _comparator.compare(key, node.getKey());
            if (cmp == 0) {
                return node.setValue(value);
            }

            node = cmp < 0 ? node.getSmaller() : node.getLarger();
        }

        Node inserted = new Node(parent, key, value);

        if (parent == null) {
            _root = inserted;
        } else if (cmp < 0) {
            parent.setSmaller(inserted);
        } else {
            parent.setLarger(inserted);
        }

        ++_size;
        return null;
    }

    public Object delete(Object key) {
        Node node = search(key);
        if (node == null) {
            return null;
        }

        Node deleted = node.getSmaller() != null && node.getLarger() != null ? node.successor() : node;
        assert deleted != null : "deleted can't be null";

        Node replacement = deleted.getSmaller() != null ? deleted.getSmaller() : deleted.getLarger();
        if (replacement != null) {
            replacement.setParent(deleted.getParent());
        }

        if (deleted == _root) {
            _root = replacement;
        } else if (deleted.isSmaller()) {
            deleted.getParent().setSmaller(replacement);
        } else {
            deleted.getParent().setLarger(replacement);
        }

        if (deleted != node) {
            Object deletedValue = node.getValue();
            node.setKey(deleted.getKey());
            node.setValue(deleted.getValue());
            deleted.setValue(deletedValue);
        }

        --_size;
        return deleted.getValue();
    }

    public AosIterator iterator() {
        return new EntryIterator();
    }

    public void clear() {
        _root = null;
        _size = 0;
    }

    public int size() {
        return _size;
    }

    public boolean isEmpty() {
        return _root == null;
    }

    /**
     * Searches for a value in the tree.
     *
     * @param value The value for which to search.
     * @return The node; or <code>null</code> if not found.
     */
    private Node search(Object value) {
        assert value != null : "value can't be null";

        Node node = _root;

        while (node != null) {
            int cmp = _comparator.compare(value, node.getKey());
            if (cmp == 0) {
                break;
            }

            node = cmp < 0 ? node.getSmaller() : node.getLarger();
        }

        return node;
    }

    /**
     * A node in the tree.
     */
    private static final class Node implements Map.Entry {
        /** The key. */
        private Object _key;

        /** The value. */
        private Object _value;

        /** The parent; or <code>null</code>. */
        private Node _parent;

        /** The smaller child; or <code>null</code>. */
        private Node _smaller;

        /** The larger child; or <code>null</code>. */
        private Node _larger;

        /**
         * Constructor. Sets the smaller and larger nodes to <code>null</code>.
         *
         * @param parent The parent.
         * @param key The key.
         * @param value The value.
         */
        public Node(Node parent, Object key, Object value) {
            setKey(key);
            setValue(value);
            setParent(parent);
        }

        /**
         * Obtains the key.
         *
         * @return The key.
         */
        public Object getKey() {
            return _key;
        }

        /**
         * Sets the key.
         *
         * @param key The key.
         */
        public void setKey(Object key) {
            assert key != null : "key can't be null";
            _key = key;
        }

        /**
         * Obtains the value.
         *
         * @return The value.
         */
        public Object getValue() {
            return _value;
        }

        /**
         * Sets the value.
         *
         * @param value The new value.
         * @return The old value.
         */
        public Object setValue(Object value) {
            Object oldValue = _value;
            _value = value;
            return oldValue;
        }

        /**
         * Obtains the parent.
         *
         * @return the parent; or <code>null</code>.
         */
        public Node getParent() {
            return _parent;
        }

        /**
         * Sets the parent.
         *
         * @param parent The parent; or <code>null</code>.
         */
        public void setParent(Node parent) {
            _parent = parent;
        }

        /**
         * Obtains the smaller child.
         *
         * @return The smaller child; or <code>null</code>.
         */
        public Node getSmaller() {
            return _smaller;
        }

        /**
         * Sets the smaller child. Updates the new smaller child to reflect that this is now its parent. Also updates
         * any exisiting smaller child to reflect that this is no longer its parent.
         *
         * @param node The new smaller child.
         */
        public void setSmaller(Node node) {
            assert node != getLarger() : "smaller can't be the same as larger";
            _smaller = node;
        }

        /**
         * Obtains the larger child.
         *
         * @return the larger child; or <code>null</code>.
         */
        public Node getLarger() {
            return _larger;
        }


        /**
         * Sets the larger node. Updates the new larger node to reflect that this is now its parent. Also updates any
         * exisiting larger node to reflect that this is no longer its parent.
         *
         * @param node The new larger child.
         */
        public void setLarger(Node node) {
            assert node != getSmaller() : "larger can't be the same as smaller";
            _larger = node;
        }

        /**
         * Determines if this is the smaller child of its parent.
         *
         * @return <code>true</code> if this is the smaller child of it's parent; otherwise <code>false</code>.
         */
        public boolean isSmaller() {
            return getParent() != null && this == getParent().getSmaller();
        }

        /**
         * Determines if this is the larger child of its parent.
         *
         * @return <code>true</code> if this is the larger child of it's parent; otherwise <code>false</code>.
         */
        public boolean isLarger() {
            return getParent() != null && this == getParent().getLarger();
        }

        /**
         * Obtains the node with the smallest value starting from this.
         *
         * @return The minimum.
         */
        public Node minimum() {
            Node node = this;

            while (node.getSmaller() != null) {
                node = node.getSmaller();
            }

            return node;
        }

        /**
         * Obtains the node with the largest value starting from this.
         *
         * @return The maximum.
         */
        public Node maximum() {
            Node node = this;

            while (node.getLarger() != null) {
                node = node.getLarger();
            }

            return node;
        }

        /**
         * Obtains the node with the next largest value starting from this.
         *
         * @return The successor; or <code>null</code>.
         */
        public Node successor() {
            if (getLarger() != null) {
                return getLarger().minimum();
            }

            Node node = this;

            while (node.isLarger()) {
                node = node.getParent();
            }

            return node.getParent();
        }

        /**
         * Obtains the node with the next smallest value starting from this.
         *
         * @return The predecessor; or <code>null</code>.
         */
        public Node predecessor() {
            if (getSmaller() != null) {
                return getSmaller().maximum();
            }

            Node node = this;

            while (node.isSmaller()) {
                node = node.getParent();
            }

            return node.getParent();
        }
    }

    /**
     * An {@link AosIterator} over the values in the set.
     */
    private final class EntryIterator implements AosIterator {
        /** The current node; or <code>null</code>. */
        private Node _current;

        public void first() {
            _current = _root != null ? _root.minimum() : null;
        }

        public void last() {
            _current = _root != null ? _root.maximum() : null;
        }

        public boolean isDone() {
            return _current == null;
        }

        public void next() {
            if (!isDone()) {
                _current = _current.successor();
            }
        }

        public void previous() {
            if (!isDone()) {
                _current = _current.predecessor();
            }
        }

        public Object current() throws IteratorOutOfBoundsException {
            if (isDone()) {
                throw new IteratorOutOfBoundsException();
            }
            return _current;
        }
    }
}
