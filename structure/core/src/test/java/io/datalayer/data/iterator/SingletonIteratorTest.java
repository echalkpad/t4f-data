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

import io.datalayer.data.iterable.SingletonIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;
import junit.framework.TestCase;

/**
 * Test cases for {@link SingletonIterator}.
 *
 */
public class SingletonIteratorTest extends TestCase {
    public void testForwardsIteration() {
        String value = "X";
        SingletonIterator iterator = new SingletonIterator(value);

        iterator.first();
        assertFalse(iterator.isDone());
        assertSame(value, iterator.current());

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
        String value = "X";
        SingletonIterator iterator = new SingletonIterator(value);

        iterator.last();
        assertFalse(iterator.isDone());
        assertSame(value, iterator.current());

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
