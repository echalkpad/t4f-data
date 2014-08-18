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
package io.datalayer.data.iterable;

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;

/**
 * An {@link AosIterator} that reverses the iteration direction of another.
 *
 */
public class ReverseIterator implements AosIterator {
    /** The underlying iterator. */
    private final AosIterator _iterator;

    /**
     * Constructor.
     *
     * @param iterator The underlying iterator.
     */
    public ReverseIterator(AosIterator iterator) {
        assert iterator != null : "iterator can't be null";
        _iterator = iterator;
    }

    public void first() {
        _iterator.last();
    }

    public void last() {
        _iterator.first();
    }

    public boolean isDone() {
        return _iterator.isDone();
    }

    public void next() {
        _iterator.previous();
    }

    public void previous() {
        _iterator.next();
    }

    public Object current() throws IteratorOutOfBoundsException {
        return _iterator.current();
    }
}
