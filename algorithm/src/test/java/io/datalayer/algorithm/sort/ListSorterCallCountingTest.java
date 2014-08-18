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

import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.algorithm.sort.bubble.BubblesortListSorter;
import io.datalayer.algorithm.sort.insertion.InsertionSortListSorter;
import io.datalayer.algorithm.sort.merge.MergesortListSorter;
import io.datalayer.algorithm.sort.quick.QuicksortListSorter;
import io.datalayer.algorithm.sort.selection.SelectionSortListSorter;
import io.datalayer.algorithm.sort.shell.ShellsortListSorter;
import io.datalayer.data.comparator.CallCountingComparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * A test case to drive the various {@link ListSorter} implementations
 * in order to compare their efficiency.
 *
 */
public class ListSorterCallCountingTest extends TestCase {
    private static final int TEST_SIZE = 1000;

    private final List _sortedArrayList = new ArrayList(TEST_SIZE);
    private final List _reverseArrayList = new ArrayList(TEST_SIZE);
    private final List _randomArrayList = new ArrayList(TEST_SIZE);

    private CallCountingComparator _comparator;

    protected void setUp() throws Exception {
        super.setUp();
        _comparator = new CallCountingComparator(NaturalComparator.INSTANCE);

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

    public void testWorstCaseBubblesort() {
        new BubblesortListSorter(_comparator).sort(_reverseArrayList);
        reportCalls();
    }

    public void testWorstCaseSelectionSort() {
        new SelectionSortListSorter(_comparator).sort(_reverseArrayList);
        reportCalls();
    }

    public void testWorstCaseInsertionSort() {
        new InsertionSortListSorter(_comparator).sort(_reverseArrayList);
        reportCalls();
    }

    public void testWorstCaseShellsort() {
        new ShellsortListSorter(_comparator).sort(_reverseArrayList);
        reportCalls();
    }

    public void testWorstCaseQuicksort() {
        new QuicksortListSorter(_comparator).sort(_reverseArrayList);
        reportCalls();
    }

    public void testWorstCaseMergesort() {
        new MergesortListSorter(_comparator).sort(_reverseArrayList);
        reportCalls();
    }

    public void testBestCaseBubblesort() {
        new BubblesortListSorter(_comparator).sort(_sortedArrayList);
        reportCalls();
    }

    public void testBestCaseSelectionSort() {
        new SelectionSortListSorter(_comparator).sort(_sortedArrayList);
        reportCalls();
    }

    public void testBestCaseInsertionSort() {
        new InsertionSortListSorter(_comparator).sort(_sortedArrayList);
        reportCalls();
    }

    public void testBestCaseShellsort() {
        new ShellsortListSorter(_comparator).sort(_sortedArrayList);
        reportCalls();
    }

    public void testBestCaseQuicksort() {
        new QuicksortListSorter(_comparator).sort(_sortedArrayList);
        reportCalls();
    }

    public void testBestCaseMergesort() {
        new MergesortListSorter(_comparator).sort(_sortedArrayList);
        reportCalls();
    }

    public void testAverageCaseBubblesort() {
        new BubblesortListSorter(_comparator).sort(_randomArrayList);
        reportCalls();
    }

    public void testAverageCaseSelectionSort() {
        new SelectionSortListSorter(_comparator).sort(_randomArrayList);
        reportCalls();
    }

    public void testAverageCaseInsertionSort() {
        new InsertionSortListSorter(_comparator).sort(_randomArrayList);
        reportCalls();
    }

    public void testAverageCaseShellsort() {
        new ShellsortListSorter(_comparator).sort(_randomArrayList);
        reportCalls();
    }

    public void testAverageCaseQuicksort() {
        new QuicksortListSorter(_comparator).sort(_randomArrayList);
        reportCalls();
    }

    public void testAverageCaseMergeSort() {
        new MergesortListSorter(_comparator).sort(_randomArrayList);
        reportCalls();
    }

    private void reportCalls() {
        System.out.println(getName() + ": " + _comparator.getCallCount() + " calls");
    }
}
