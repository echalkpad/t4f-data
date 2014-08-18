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
package io.datalayer.algorithm.sort;

import io.datalayer.algorithm.sort.quick.QuicksortListSorter;
import io.datalayer.algorithm.sort.shell.ShellsortListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import io.datalayer.data.stack.CallCountingList;
import junit.framework.TestCase;

/**
 * A sample answer to exercise 7-3.
 * This program counts how many objects are moved during sorting.
 *
 */
public class AdvancedListSorterCallCountingListTest extends TestCase {
    private static final int TEST_SIZE = 1000;

    private final List _sortedArrayList = new ArrayList(TEST_SIZE);
    private final List _reverseArrayList = new ArrayList(TEST_SIZE);
    private final List _randomArrayList = new ArrayList(TEST_SIZE);

    private Comparator _comparator = NaturalComparator.INSTANCE;

    protected void setUp() throws Exception {
        super.setUp();

        for (int i = 1; i < TEST_SIZE; ++i) {
            _sortedArrayList.add(new Integer(i));
        }

        for (int i = TEST_SIZE; i > 0; --i) {
            _reverseArrayList.add(new Integer(i));
        }

        for (int i = 1; i < TEST_SIZE; ++i) {
            _randomArrayList.add(new Integer((int)(TEST_SIZE * Math.random())));
        }
    }

    public void testWorstCaseQuicksort() {
        List list = new CallCountingList(_reverseArrayList);
        new QuicksortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testWorstCaseShellSort() {
        List list = new CallCountingList(_reverseArrayList);
        new ShellsortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testBestCaseQuicksort() {
        List list = new CallCountingList(_sortedArrayList);
        new QuicksortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testBestCaseShellSort() {
        List list = new CallCountingList(_sortedArrayList);
        new ShellsortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testAverageCaseQuicksort() {
        List list = new CallCountingList(_randomArrayList);
        new QuicksortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testAverageCaseShellSort() {
        List list = new CallCountingList(_randomArrayList);
        new ShellsortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    private void reportCalls(List list) {
        System.out.println(getName() + ": " + list);
    }
}
