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

import io.datalayer.data.iterable.ReverseIterator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.iterator.IteratorOutOfBoundsException;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;
import io.datalayer.data.map.DefaultEntry;
import io.datalayer.data.map.Map;
import junit.framework.TestCase;

/**
 * Abstract base class for testing implementations of {@link Map}.
 *
 */
public abstract class AbstractMapTestCase extends TestCase {
    private static final Map.Entry A = new DefaultEntry("akey", "avalue");
    private static final Map.Entry B = new DefaultEntry("bkey", "bvalue");
    private static final Map.Entry C = new DefaultEntry("ckey", "cvalue");
    private static final Map.Entry D = new DefaultEntry("dkey", "dvalue");
    private static final Map.Entry E = new DefaultEntry("ekey", "evalue");
    private static final Map.Entry F = new DefaultEntry("fkey", "fvalue");

    private Map _map;

    protected void setUp() throws Exception {
        super.setUp();

        _map = createMap();

        _map.set(C.getKey(), C.getValue());
        _map.set(A.getKey(), A.getValue());
        _map.set(B.getKey(), B.getValue());
        _map.set(D.getKey(), D.getValue());
    }

    protected abstract Map createMap();

    public void testContainsExisting() {
        assertTrue(_map.contains(A.getKey()));
        assertTrue(_map.contains(B.getKey()));
        assertTrue(_map.contains(C.getKey()));
        assertTrue(_map.contains(D.getKey()));
    }

    public void testContainsNonExisting() {
        assertFalse(_map.contains(E.getKey()));
        assertFalse(_map.contains(F.getKey()));
    }

    public void testGetExisting() {
        assertEquals(A.getValue(), _map.get(A.getKey()));
        assertEquals(B.getValue(), _map.get(B.getKey()));
        assertEquals(C.getValue(), _map.get(C.getKey()));
        assertEquals(D.getValue(), _map.get(D.getKey()));
    }

    public void testGetNonExisting() {
        assertNull(_map.get(E.getKey()));
        assertNull(_map.get(F.getKey()));
    }

    public void testSetNewKey() {
        assertEquals(4, _map.size());

        assertNull(_map.set(E.getKey(), E.getValue()));
        assertEquals(E.getValue(), _map.get(E.getKey()));
        assertEquals(5, _map.size());

        assertNull(_map.set(F.getKey(), F.getValue()));
        assertEquals(F.getValue(), _map.get(F.getKey()));
        assertEquals(6, _map.size());
    }

    public void testSetExistingKey() {
        assertEquals(4, _map.size());
        assertEquals(C.getValue(), _map.set(C.getKey(), "cvalue2"));
        assertEquals("cvalue2", _map.get(C.getKey()));
        assertEquals(4, _map.size());
    }

    public void testDeleteExisting() {
        assertEquals(4, _map.size());

        assertEquals(B.getValue(), _map.delete(B.getKey()));
        assertFalse(_map.contains(B.getKey()));
        assertEquals(3, _map.size());

        assertEquals(A.getValue(), _map.delete(A.getKey()));
        assertFalse(_map.contains(A.getKey()));
        assertEquals(2, _map.size());

        assertEquals(C.getValue(), _map.delete(C.getKey()));
        assertFalse(_map.contains(C.getKey()));
        assertEquals(1, _map.size());

        assertEquals(D.getValue(), _map.delete(D.getKey()));
        assertFalse(_map.contains(D.getKey()));
        assertEquals(0, _map.size());
    }

    public void testDeleteNonExisting() {
        assertEquals(4, _map.size());
        assertNull(_map.delete(E.getKey()));
        assertEquals(4, _map.size());
        assertNull(_map.delete(F.getKey()));
        assertEquals(4, _map.size());
    }

    public void testClear() {
        assertEquals(4, _map.size());
        assertFalse(_map.isEmpty());

        _map.clear();

        assertEquals(0, _map.size());
        assertTrue(_map.isEmpty());

        assertFalse(_map.contains(A.getKey()));
        assertFalse(_map.contains(B.getKey()));
        assertFalse(_map.contains(C.getKey()));
        assertFalse(_map.contains(D.getKey()));
    }

    public void testIteratorForwards() {
        checkIterator(_map.iterator());
    }

    public void testIteratorBackwards() {
        checkIterator(new ReverseIterator(_map.iterator()));
    }

    private void checkIterator(AosIterator i) {
        List entries = new LinkedList();

        for (i.first(); !i.isDone(); i.next()) {
            Map.Entry entry = (Map.Entry) i.current();
            entries.add(new DefaultEntry(entry.getKey(), entry.getValue()));
        }

        try {
            i.current();
            fail();
        } catch (IteratorOutOfBoundsException e) {
            // expected
        }

        assertEquals(4, entries.size());
        assertTrue(entries.contains(A));
        assertTrue(entries.contains(B));
        assertTrue(entries.contains(C));
        assertTrue(entries.contains(D));
    }
}
