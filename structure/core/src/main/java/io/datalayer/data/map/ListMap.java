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
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;

/**
 * A {@link Map} that uses a {@link LinkedList}.
 *
 */
public class ListMap implements Map {
    /** The underlying list of entries. */
    private final List _entries = new LinkedList();

    public Object get(Object key) {
        DefaultEntry entry = entryFor(key);
        return entry != null ? entry.getValue() : null;
    }

    public Object set(Object key, Object value) {
        DefaultEntry entry = entryFor(key);
        if (entry != null) {
            return entry.setValue(value);
        }

        _entries.add(new DefaultEntry(key, value));
        return null;
    }

    public Object delete(Object key) {
        DefaultEntry entry = entryFor(key);
        if (entry == null) {
            return null;
        }

        _entries.delete(entry);
        return entry.getValue();
    }

    public boolean contains(Object key) {
        return entryFor(key) != null;
    }

    public void clear() {
        _entries.clear();
    }

    public int size() {
        return _entries.size();
    }

    public boolean isEmpty() {
        return _entries.isEmpty();
    }

    public AosIterator iterator() {
        return _entries.iterator();
    }

    /**
     * Obtains the entry (if any) for a specified key.
     *
     * @param key The key for which an entry is required.
     * @return The entry; or <code>null</code> if the key was not found.
     */
    private DefaultEntry entryFor(Object key) {
        AosIterator i = iterator();
        for (i.first(); !i.isDone(); i.next()) {
            DefaultEntry entry = (DefaultEntry) i.current();
            if (entry.getKey().equals(key)) {
                return entry;
            }
        }

        return null;
    }
}
