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

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;

/**
 * A {@link Set} that uses a {@link LinkedList}.
 *
 */
public class ListSet implements Set {
    /** The underlying list of values. */
    private final List _values = new LinkedList();

    public boolean contains(Object value) {
        return _values.contains(value);
    }

    public boolean add(Object value) {
        if (contains(value)) {
            return false;
        }

        _values.add(value);
        return true;
    }

    public boolean delete(Object value) {
        return _values.delete(value);
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

    public AosIterator iterator() {
        return _values.iterator();
    }
}
