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
import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.algorithm.sort.merge.MergesortListSorter;
import io.datalayer.algorithm.sort.quick.QuicksortListSorter;
import io.datalayer.algorithm.sort.shell.ShellsortListSorter;
import io.datalayer.data.comparator.CallCountingComparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * Compares the performance of binary insertion versus sorting.
 *
 */
public class BinaryInsertCallCountingTest extends TestCase {
    private static final int TEST_SIZE = 4091;

    private List _list;
    private CallCountingComparator _comparator;

    protected void setUp() throws Exception {
        super.setUp();

        _list = new ArrayList(TEST_SIZE);
        _comparator = new CallCountingComparator(NaturalComparator.INSTANCE);
    }

    public void testBinaryInsert() {
        ListInserter inserter = new ListInserter(new IterativeBinaryListSearcher(_comparator));

        for (int i = 0; i < TEST_SIZE; ++i) {
            inserter.insert(_list, new Integer((int) (TEST_SIZE * Math.random())));
        }

        reportCalls();
    }

    public void testMergeSort() {
        populateAndSort(new MergesortListSorter(_comparator));
    }

    public void testShellsort() {
        populateAndSort(new ShellsortListSorter(_comparator));
    }

    public void testQuicksort() {
        populateAndSort(new QuicksortListSorter(_comparator));
    }

    private void populateAndSort(ListSorter sorter) {
        for (int i = 0; i < TEST_SIZE; ++i) {
            _list.add(new Integer((int) (TEST_SIZE * Math.random())));
        }

        _list = sorter.sort(_list);

        reportCalls();
    }

    private void reportCalls() {
        System.out.println(getName() + ": " + _comparator.getCallCount() + " calls");
    }
}
