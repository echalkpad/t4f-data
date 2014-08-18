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

import io.datalayer.data.iterable.ArrayIterator;
import io.datalayer.data.iterable.ReverseIterator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.FilterIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;
import io.datalayer.data.predicate.Predicate;
import junit.framework.TestCase;

/**
 * Test cases for {@link FilterIterator}.
 *
 */
public class FilterIteratorTest extends TestCase {
    private static final Object[] ARRAY = {"A", "B", "C"};

    public void testForwardsIterationIncludesItemsWhenPredicateReturnsTrue() {
        AosIterator expectedIterator = new ArrayIterator(ARRAY);
        AosIterator underlyingIterator = new ArrayIterator(ARRAY);

        AosIterator iterator = new FilterIterator(underlyingIterator, new DummyPredicate(true, expectedIterator));

        iterator.first();
        assertFalse(iterator.isDone());
        assertSame(ARRAY[0], iterator.current());

        iterator.next();
        assertFalse(iterator.isDone());
        assertSame(ARRAY[1], iterator.current());

        iterator.next();
        assertFalse(iterator.isDone());
        assertSame(ARRAY[2], iterator.current());

        iterator.next();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }

        assertTrue(expectedIterator.isDone());
        assertTrue(underlyingIterator.isDone());
    }

    public void testForwardsIterationExcludesItemsWhenPredicateReturnsFalse() {
        AosIterator expectedIterator = new ArrayIterator(ARRAY);
        AosIterator underlyingIterator = new ArrayIterator(ARRAY);

        AosIterator iterator = new FilterIterator(underlyingIterator, new DummyPredicate(false, expectedIterator));

        iterator.first();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }

        assertTrue(expectedIterator.isDone());
        assertTrue(underlyingIterator.isDone());
    }

    public void testBackwardssIterationIncludesItemsWhenPredicateReturnsTrue() {
        AosIterator expectedIterator = new ReverseIterator(new ArrayIterator(ARRAY));
        AosIterator underlyingIterator = new ArrayIterator(ARRAY);

        AosIterator iterator = new FilterIterator(underlyingIterator, new DummyPredicate(true, expectedIterator));

        iterator.last();
        assertFalse(iterator.isDone());
        assertSame(ARRAY[2], iterator.current());

        iterator.previous();
        assertFalse(iterator.isDone());
        assertSame(ARRAY[1], iterator.current());

        iterator.previous();
        assertFalse(iterator.isDone());
        assertSame(ARRAY[0], iterator.current());

        iterator.previous();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }

        assertTrue(expectedIterator.isDone());
        assertTrue(underlyingIterator.isDone());
    }

    public void testBackwardsIterationExcludesItemsWhenPredicateReturnsFalse() {
        AosIterator expectedIterator = new ReverseIterator(new ArrayIterator(ARRAY));
        AosIterator underlyingIterator = new ArrayIterator(ARRAY);

        AosIterator iterator = new FilterIterator(underlyingIterator, new DummyPredicate(false, expectedIterator));

        iterator.last();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }

        assertTrue(expectedIterator.isDone());
        assertTrue(underlyingIterator.isDone());
    }

    private static final class DummyPredicate implements Predicate {
        private final AosIterator _iterator;
        private final boolean _result;

        public DummyPredicate(boolean result, AosIterator iterator) {
            _iterator = iterator;
            _result = result;
            _iterator.first();
        }

        public boolean evaluate(Object object) {
            assertSame(_iterator.current(), object);
            _iterator.next();
            return _result;
        }
    }
}
