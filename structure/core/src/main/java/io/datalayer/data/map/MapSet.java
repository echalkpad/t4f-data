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

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.set.Set;

/**
 * A {@link Set} that uses a {@link Map} as the underlying storage mechanism.
 *
 */
public class MapSet implements Set {
    /** Special value when adding to the underlying map. */
    private static final Object PRESENT = new Object();

    /** The underlying map. */
    private final Map _map;

    /**
     * Constructor.
     *
     * @param map The underlying map.
     */
    public MapSet(Map map) {
        assert map != null : "map can't be null";
        _map = map;
    }

    public boolean contains(Object value) {
        return _map.contains(value);
    }

    public boolean add(Object value) {
        return _map.set(value, PRESENT) == null;
    }

    public boolean delete(Object value) {
        return _map.delete(value) == PRESENT;
    }

    public AosIterator iterator() {
        return new MapKeyIterator(_map.iterator());
    }

    public void clear() {
        _map.clear();
    }

    public int size() {
        return _map.size();
    }

    public boolean isEmpty() {
        return _map.isEmpty();
    }
}
