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
package io.datalayer.algorithm.sort.set;

import io.datalayer.algorithm.bsearch.IterativeBinaryListSearcher;
import io.datalayer.algorithm.list.ListSearcher;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import io.datalayer.data.set.Set;

/**
 * A {@link Set} that uses a {@link List} as the underlying storage mechanism and a {@link ListSearcher} to keep the
 * values in sorted order.
 *
 */
public class SortedListSet implements Set {
    /** The underlying list of sorted values. */
    private final List _values = new ArrayList();

    /** The list searcher to use. */
    private final ListSearcher _searcher;

    /**
     * Default constructor. Assumes all values implement {@link Comparable}.
     */
    public SortedListSet() {
        this(NaturalComparator.INSTANCE);
    }

    /**
     * Constructor.
     *
     * @param comparator The strategy to use for value comparison.
     */
    public SortedListSet(Comparator comparator) {
        _searcher = new IterativeBinaryListSearcher(comparator);
    }

    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    public boolean add(Object value) {
        int index = indexOf(value);
        if (index < 0) {
            _values.insert(-(index + 1), value);
            return true;
        }

        _values.set(index, value);
        return false;
    }

    public boolean delete(Object value) {
        int index = indexOf(value);
        if (index >= 0) {
            _values.delete(index);
            return true;
        }

        return false;
    }

    public AosIterator iterator() {
        return _values.iterator();
    }

    public void clear() {
        _values.clear();
    }

    public int size() {
        return _values.size();
    }

    public boolean isEmpty() {
        return _values.isEmpty();
    }

    /**
     * Searches a list for a specified value.
     *
     * @param value The value for which to search.
     * @return The position (0, 1, 2...) of the value if found; otherwise <code>-(insertion point + 1)</code>.
     */
    private int indexOf(Object value) {
        return _searcher.search(_values, value);
    }
}
