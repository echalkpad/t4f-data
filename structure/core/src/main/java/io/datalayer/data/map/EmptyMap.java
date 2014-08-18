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

import io.datalayer.data.iterable.EmptyIterator;
import io.datalayer.data.iterator.AosIterator;

/**
 * A {@link Map} that is always empty.
 *
 */
public final class EmptyMap implements Map {
    /** The single instance of the class. */
    public static final EmptyMap INSTANCE = new EmptyMap();

    /**
     * Constructor marked private to prevent instantiation.
     */
    private EmptyMap() {
    }

    public Object get(Object key) {
        return null;
    }

    public Object set(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object delete(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object key) {
        return false;
    }

    public void clear() {
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public AosIterator iterator() {
        return EmptyIterator.INSTANCE;
    }
}
