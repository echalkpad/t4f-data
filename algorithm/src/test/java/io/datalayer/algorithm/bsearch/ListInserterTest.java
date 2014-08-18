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
package io.datalayer.algorithm.bsearch;

import io.datalayer.algorithm.bsearch.IterativeBinaryListSearcher;
import io.datalayer.algorithm.list.ListInserter;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * Test cases for {@link ListInserter}.
 *
 */
public class ListInserterTest extends TestCase {
    private static final int TEST_SIZE = 1023;

    private ListInserter _inserter;
    private List _list;

    protected void setUp() throws Exception {
        super.setUp();

        _inserter = new ListInserter(new IterativeBinaryListSearcher(NaturalComparator.INSTANCE));
        _list = new ArrayList(TEST_SIZE);
    }

    public void testAscendingInOrderInsertion() {
        for (int i = 0; i < TEST_SIZE; ++i) {
            assertEquals(i, _inserter.insert(_list, new Integer(i)));
        }

        verify();
    }

    public void testDescendingInOrderInsertion() {
        for (int i = TEST_SIZE - 1; i >= 0; --i) {
            assertEquals(0, _inserter.insert(_list, new Integer(i)));
        }

        verify();
    }

    public void testRandomInsertion() {
        for (int i = 0; i < TEST_SIZE; ++i) {
            _inserter.insert(_list, new Integer((int) (TEST_SIZE * Math.random())));
        }

        verify();
    }

    private void verify() {
        int previousValue = Integer.MIN_VALUE;
        AosIterator i = _list.iterator();

        for (i.first(); !i.isDone(); i.next()) {
            int currentValue = ((Integer) i.current()).intValue();
            assertTrue(currentValue >= previousValue);
            previousValue = currentValue;
        }
    }
}
