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
package io.datalayer.data.iterator;

import io.datalayer.data.predicate.Predicate;

/**
 * An {@link AosIterator} that filters the results according to some {@link Predicate}.
 *
 */
public class FilterIterator implements AosIterator {
    /** The underlying iterator. */
    private final AosIterator _iterator;

    /** The filter to apply. */
    private final Predicate _predicate;

    /**
     * Constructor.
     *
     * @param iterator The underlying iterator.
     * @param predicate The filter to apply.
     */
    public FilterIterator(AosIterator iterator, Predicate predicate) {
        assert iterator != null : "iterator can't be null";
        assert predicate != null : "predicate can't be null";
        _iterator = iterator;
        _predicate = predicate;
    }

    public void first() {
        _iterator.first();
        filterForwards();
    }

    public void last() {
        _iterator.last();
        filterBackwards();
    }

    public boolean isDone() {
        return _iterator.isDone();
    }

    public void next() {
        _iterator.next();
        filterForwards();
    }

    public void previous() {
        _iterator.previous();
        filterBackwards();
    }

    public Object current() throws IteratorOutOfBoundsException {
        return _iterator.current();
    }

    /**
     * Applies the filter, calling {@link AosIterator#next()} on the underlying iterator until either a match is found; or
     * the iterator is done.
     */
    private void filterForwards() {
        while (!_iterator.isDone() && !_predicate.evaluate(_iterator.current())) {
            _iterator.next();
        }
    }

    /**
     * Applies the filter, calling {@link AosIterator#previous()} on the underlying iterator until either a match is found;
     * or the iterator is done.
     */
    private void filterBackwards() {
        while (!_iterator.isDone() && !_predicate.evaluate(_iterator.current())) {
            _iterator.previous();
        }
    }
}
