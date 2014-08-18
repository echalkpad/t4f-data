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
package io.datalayer.data.list;

import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * Abstract base class for testing implementations of {@link List}.
 *
 */
public abstract class AbstractListTestCase extends TestCase {
    protected static final Object VALUE_A = "A";
    protected static final Object VALUE_B = "B";
    protected static final Object VALUE_C = "C";

    private List _list;

    protected abstract List createList();

    protected void setUp() throws Exception {
        super.setUp();

        _list = createList();
    }

    public void testInsertIntoEmptyList() {
        assertEquals(0, _list.size());
        assertTrue(_list.isEmpty());

        _list.insert(0, VALUE_A);

        assertEquals(1, _list.size());
        assertFalse(_list.isEmpty());
        assertSame(VALUE_A, _list.get(0));
    }

    public void testInsertBetweenElements() {
        _list.insert(0, VALUE_A);
        _list.insert(1, VALUE_B);
        _list.insert(1, VALUE_C);
        assertEquals(3, _list.size());

        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_C, _list.get(1));
        assertSame(VALUE_B, _list.get(2));
    }

    public void testInsertBeforeFirstElement() {
        _list.insert(0, VALUE_A);
        _list.insert(0, VALUE_B);

        assertEquals(2, _list.size());
        assertSame(VALUE_B, _list.get(0));
        assertSame(VALUE_A, _list.get(1));
    }

    public void testInsertAfterLastElement() {
        _list.insert(0, VALUE_A);
        _list.insert(1, VALUE_B);

        assertEquals(2, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_B, _list.get(1));
    }

    public void testInsertOutOfBounds() {
        try {
            _list.insert(-1, VALUE_A);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            _list.insert(1, VALUE_B);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testAdd() {
        _list.add(VALUE_A);
        _list.add(VALUE_C);
        _list.add(VALUE_B);

        assertEquals(3, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_C, _list.get(1));
        assertSame(VALUE_B, _list.get(2));
    }

    public void testGetOutOfBounds() {
        try {
            _list.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            _list.get(0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        _list.add(VALUE_A);

        try {
            _list.get(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testDeleteMiddleElement() {
        _list.add(VALUE_A);
        _list.add(VALUE_C);
        _list.add(VALUE_B);

        assertEquals(3, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_C, _list.get(1));
        assertSame(VALUE_B, _list.get(2));

        assertSame(VALUE_C, _list.delete(1));

        assertEquals(2, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_B, _list.get(1));
    }

    public void testDeleteOnlyElement() {
        _list.add(VALUE_A);

        assertEquals(1, _list.size());
        assertSame(VALUE_A, _list.get(0));

        assertSame(VALUE_A, _list.delete(0));

        assertEquals(0, _list.size());
    }

    public void testDeleteFirstElement() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_C);

        assertEquals(3, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_B, _list.get(1));
        assertSame(VALUE_C, _list.get(2));

        assertSame(VALUE_A, _list.delete(0));

        assertEquals(2, _list.size());
        assertSame(VALUE_B, _list.get(0));
        assertSame(VALUE_C, _list.get(1));
    }

    public void testDeleteLastElement() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_C);

        assertEquals(3, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_B, _list.get(1));
        assertSame(VALUE_C, _list.get(2));

        assertSame(VALUE_C, _list.delete(2));

        assertEquals(2, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_B, _list.get(1));
    }

    public void testDeleteByValue() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_A);

        assertEquals(3, _list.size());
        assertSame(VALUE_A, _list.get(0));
        assertSame(VALUE_B, _list.get(1));
        assertSame(VALUE_A, _list.get(2));

        assertTrue(_list.delete(VALUE_A));

        assertEquals(2, _list.size());
        assertSame(VALUE_B, _list.get(0));
        assertSame(VALUE_A, _list.get(1));

        assertTrue(_list.delete(VALUE_A));

        assertEquals(1, _list.size());
        assertSame(VALUE_B, _list.get(0));

        assertFalse(_list.delete(VALUE_C));

        assertEquals(1, _list.size());
        assertSame(VALUE_B, _list.get(0));

        assertTrue(_list.delete(VALUE_B));

        assertEquals(0, _list.size());
    }

    public void testDeleteOutOfBounds() {
        try {
            _list.delete(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            _list.delete(0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testSet() {
        _list.insert(0, VALUE_A);
        assertSame(VALUE_A, _list.get(0));

        assertSame(VALUE_A, _list.set(0, VALUE_B));
        assertSame(VALUE_B, _list.get(0));
    }

    public void testSetOutOfBounds() {
        try {
            _list.set(-1, VALUE_A);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            _list.set(0, VALUE_B);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        _list.insert(0, VALUE_C);

        try {
            _list.set(1, VALUE_C);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    public void testEmptyIteration() {
        AosIterator iterator = _list.iterator();

        assertTrue(iterator.isDone());

        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }
    }

    public void testForwardIteration() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_C);

        AosIterator iterator = _list.iterator();

        iterator.first();
        assertFalse(iterator.isDone());
        assertSame(VALUE_A, iterator.current());

        iterator.next();
        assertFalse(iterator.isDone());
        assertSame(VALUE_B, iterator.current());

        iterator.next();
        assertFalse(iterator.isDone());
        assertSame(VALUE_C, iterator.current());

        iterator.next();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // exepected
        }
    }

    public void testReverseIteration() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_C);

        AosIterator iterator = _list.iterator();

        iterator.last();
        assertFalse(iterator.isDone());
        assertSame(VALUE_C, iterator.current());

        iterator.previous();
        assertFalse(iterator.isDone());
        assertSame(VALUE_B, iterator.current());

        iterator.previous();
        assertFalse(iterator.isDone());
        assertSame(VALUE_A, iterator.current());

        iterator.previous();
        assertTrue(iterator.isDone());
        try {
            iterator.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // exepected
        }
    }

    public void testIndexOf() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_A);

        assertEquals(0, _list.indexOf(VALUE_A));
        assertEquals(1, _list.indexOf(VALUE_B));
        assertEquals(-1, _list.indexOf(VALUE_C));
    }

    public void testIndexOfUsesEqualsForEquality() {
        Integer one = new Integer(1);
        Integer anotherOne = new Integer(1);
        Integer two = new Integer(2);

        _list.add(one);

        assertEquals(0, _list.indexOf(anotherOne));
        assertEquals(-1, _list.indexOf(two));
    }

    public void testContains() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_A);

        assertTrue(_list.contains(VALUE_A));
        assertTrue(_list.contains(VALUE_B));
        assertFalse(_list.contains(VALUE_C));
    }

    public void testClear() {
        _list.add(VALUE_A);
        _list.add(VALUE_B);
        _list.add(VALUE_C);

        assertFalse(_list.isEmpty());

        _list.clear();

        assertTrue(_list.isEmpty());
    }
}
