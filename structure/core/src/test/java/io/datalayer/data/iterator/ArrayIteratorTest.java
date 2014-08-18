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
import io.datalayer.data.iterator.IteratorOutOfBoundsException;
import junit.framework.TestCase;

/**
 * Test cases for {@link ArrayIterator}.
 *
 */
public class ArrayIteratorTest extends TestCase {
    public void testIterationRespectsBounds() {
        Object[] array = new Object[] {"A", "B", "C", "D", "E", "F"};
        ArrayIterator iterator = new ArrayIterator(array, 1, 3);

        iterator.first();
        assertFalse(iterator.isDone());
        assertSame(array[1], iterator.current());

        iterator.next();
        assertFalse(iterator.isDone());
        assertSame(array[2], iterator.current());

        iterator.next();
        assertFalse(iterator.isDone());
        assertSame(array[3], iterator.current());

        iterator.next();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }
    }

    public void testBackwardsIteration() {
        Object[] array = new Object[] {"A", "B", "C"};
        ArrayIterator iterator = new ArrayIterator(array);

        iterator.last();
        assertFalse(iterator.isDone());
        assertSame(array[2], iterator.current());

        iterator.previous();
        assertFalse(iterator.isDone());
        assertSame(array[1], iterator.current());

        iterator.previous();
        assertFalse(iterator.isDone());
        assertSame(array[0], iterator.current());

        iterator.previous();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }
    }
}
