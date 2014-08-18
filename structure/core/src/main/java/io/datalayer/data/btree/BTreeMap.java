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
package io.datalayer.data.btree;

import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.EmptyList;
import io.datalayer.data.list.List;
import io.datalayer.data.map.DefaultEntry;
import io.datalayer.data.map.Map;

/**
 * A {@link Map} implementation that uses a B-Tree.
 *
 */
public class BTreeMap implements Map {
    private static final int MIN_KEYS_PER_NODE = 2;

    private final Comparator _comparator;
    private final int _maxKeysPerNode;
    private Node _root;
    private int _size;

    public BTreeMap(Comparator comparator, int maxKeysPerNode) {
        assert comparator != null : "comparator can't be null";
        assert maxKeysPerNode >= MIN_KEYS_PER_NODE : "maxKeysPerNode can't be < " + MIN_KEYS_PER_NODE;

        _comparator = comparator;
        _maxKeysPerNode = maxKeysPerNode;
        clear();
    }

    public Object get(Object key) {
        Entry entry = _root.search(key);
        return entry != null ? entry.getValue() : null;
    }

    public Object set(Object key, Object value) {
        Object oldValue = _root.set(key, value);

        if (_root.isFull()) {
            Node newRoot = new Node(false);
            _root.split(newRoot, 0);
            _root = newRoot;
        }

        return oldValue;
    }

    public Object delete(Object key) {
        Entry entry = _root.search(key);
        if (entry == null) {
            return null;
        }

        entry.setDeleted(true);
        --_size;

        return entry.setValue(null);
    }

    public boolean contains(Object key) {
        return _root.search(key) != null;
    }

    public void clear() {
        _root = new Node(true);
        _size = 0;
    }

    public int size() {
        return _size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public AosIterator iterator() {
        List list = new ArrayList(_size);

        _root.traverse(list);

        return list.iterator();
    }

    private final class Node {
        private final List _entries = new ArrayList(_maxKeysPerNode + 1);
        private final List _children;

        public Node(boolean leaf) {
            _children = !leaf ? new ArrayList(_maxKeysPerNode + 2) : (List) EmptyList.INSTANCE;
        }

        public boolean isFull() {
            return _entries.size() > _maxKeysPerNode;
        }

        public Entry search(Object key) {
            int index = indexOf(key);
            if (index >= 0) {
                Entry entry = (Entry) _entries.get(index);
                return !entry.isDeleted() ? entry : null;
            }

            return !isLeaf() ? ((Node) _children.get(-(index + 1))).search(key) : null;
        }

        public Object set(Object key, Object value) {
            int index = indexOf(key);
            if (index >= 0) {
                return ((Entry) _entries.get(index)).setValue(value);
            }

            return set(key, value, -(index + 1));
        }

        private Object set(Object key, Object value, int index) {
            if (isLeaf()) {
                _entries.insert(index, new Entry(key, value));
                ++_size;
                return null;
            }

            Node child = ((Node) _children.get(index));
            Object oldValue = child.set(key, value);

            if (child.isFull()) {
                child.split(this, index);
            }

            return oldValue;
        }

        private int indexOf(Object key) {
            int lowerIndex = 0;
            int upperIndex = _entries.size() - 1;

            while (lowerIndex <= upperIndex) {
                int index = lowerIndex + (upperIndex - lowerIndex) / 2;

                int cmp = _comparator.compare(key, ((Entry) _entries.get(index)).getKey());

                if (cmp == 0) {
                    return index;
                } else if (cmp < 0) {
                    upperIndex = index - 1;
                } else {
                    lowerIndex = index + 1;
                }
            }

            return -(lowerIndex + 1);
        }

        public void split(Node parent, int insertionPoint) {
            assert parent != null : "parent can't be null";

            Node sibling = new Node(isLeaf());

            int middle = _entries.size() / 2;

            move(_entries, middle + 1, sibling._entries);
            move(_children, middle + 1, sibling._children);

            parent._entries.insert(insertionPoint, _entries.delete(middle));

            if (parent._children.isEmpty()) {
                parent._children.insert(insertionPoint, this);
            }
            parent._children.insert(insertionPoint + 1, sibling);
        }

        public void traverse(List list) {
            assert list != null : "list can't be null";

            AosIterator children = _children.iterator();
            AosIterator entries = _entries.iterator();

            children.first();
            entries.first();

            while (!children.isDone() || !entries.isDone()) {
                if (!children.isDone()) {
                    ((Node) children.current()).traverse(list);
                    children.next();
                }

                if (!entries.isDone()) {
                    Entry entry = (Entry) entries.current();
                    if (!entry.isDeleted()) {
                        list.add(entry);
                    }
                    entries.next();
                }
            }
        }

        private void move(List source, int from, List target) {
            assert source != null : "source can't be null";
            assert target != null : "target can't be null";

            while (from < source.size()) {
                target.add(source.delete(from));
            }
        }

        private boolean isLeaf() {
            return _children == EmptyList.INSTANCE;
        }
    }

    private static final class Entry extends DefaultEntry {
        private boolean _deleted;

        public Entry(Object key, Object value) {
            super(key, value);
        }

        public boolean isDeleted() {
            return _deleted;
        }

        public void setDeleted(boolean deleted) {
            _deleted = deleted;
        }
    }
}
