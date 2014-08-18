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
import io.datalayer.data.iterator.IteratorOutOfBoundsException;

/**
 * An iterator over all the keys in a {@link Map}.
 *
 */
public class MapKeyIterator implements AosIterator {
    /** The underlying iterator over the map entries. */
    private final AosIterator _entries;

    /**
     * Constructor.
     *
     * @param entries The underlying iterator over the map entries.
     */
    public MapKeyIterator(AosIterator entries) {
        assert entries != null : "entries can't be null";
        _entries = entries;
    }

    public void first() {
        _entries.first();
    }

    public void last() {
        _entries.last();
    }

    public boolean isDone() {
        return _entries.isDone();
    }

    public void next() {
        _entries.next();
    }

    public void previous() {
        _entries.previous();
    }

    public Object current() throws IteratorOutOfBoundsException {
        return ((Map.Entry) _entries.current()).getKey();
    }
}
