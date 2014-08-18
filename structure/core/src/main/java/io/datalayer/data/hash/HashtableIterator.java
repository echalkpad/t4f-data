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
package io.datalayer.data.hash;

import io.datalayer.data.iterable.EmptyIterator;
import io.datalayer.data.iterable.Iterable;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;

/**
 */
public class HashtableIterator implements AosIterator {
    private final AosIterator _buckets;
    private AosIterator _values = EmptyIterator.INSTANCE;

    public HashtableIterator(AosIterator buckets) {
        assert buckets != null : "buckets can't be null";
        _buckets = buckets;
    }

    public void first() {
        _buckets.first();
        _values = EmptyIterator.INSTANCE;
        next();
    }

    public void last() {
        _buckets.last();
        _values = EmptyIterator.INSTANCE;
        previous();
    }

    public boolean isDone() {
        return _values.isDone() && _buckets.isDone();
    }

    public void next() {
        for (_values.next(); _values.isDone() && !_buckets.isDone(); _buckets.next()) {
            Iterable bucket = (Iterable) _buckets.current();
            if (bucket != null) {
                _values = bucket.iterator();
                _values.first();
            }
        }
    }

    public void previous() {
        for (_values.previous(); _values.isDone() && !_buckets.isDone(); _buckets.previous()) {
            Iterable bucket = (Iterable) _buckets.current();
            if (bucket != null) {
                _values = bucket.iterator();
                _values.last();
            }
        }
    }

    public Object current() throws IteratorOutOfBoundsException {
        if (isDone()) {
            throw new IteratorOutOfBoundsException();
        }
        return _values.current();
    }
}
