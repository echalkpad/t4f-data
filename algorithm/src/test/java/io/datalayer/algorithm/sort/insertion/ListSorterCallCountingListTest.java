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
package io.datalayer.algorithm.sort.insertion;

import io.datalayer.algorithm.sort.bubble.BubblesortListSorter;
import io.datalayer.algorithm.sort.insertion.InsertionSortListSorter;
import io.datalayer.algorithm.sort.selection.SelectionSortListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import io.datalayer.data.stack.CallCountingList;
import junit.framework.TestCase;

/**
 * A sample answer to exercise 6-4.
 * This program counts how many objects are moved during sorting.
 *
 */
public class ListSorterCallCountingListTest extends TestCase {
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

    public void testWorstCaseBubblesort() {
        List list = new CallCountingList(_reverseArrayList);
        new BubblesortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testWorstCaseSelectionSort() {
        List list = new CallCountingList(_reverseArrayList);
        new SelectionSortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testWorstCaseInsertionSort() {
        List list = _reverseArrayList;
        List result = new CallCountingList(new ArrayList());
        new InsertionSortListSorter(_comparator).sort(list, result);
        reportCalls(result);
    }

    public void testBestCaseBubblesort() {
        List list = new CallCountingList(_sortedArrayList);
        new BubblesortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testBestCaseSelectionSort() {
        List list = new CallCountingList(_sortedArrayList);
        new SelectionSortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testBestCaseInsertionSort() {
        List list = _sortedArrayList;
        List result = new CallCountingList(new ArrayList());
        new InsertionSortListSorter(_comparator).sort(list, result);
        reportCalls(result);
    }

    public void testAverageCaseBubblesort() {
        List list = new CallCountingList(_randomArrayList);
        new BubblesortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testAverageCaseSelectionSort() {
        List list = new CallCountingList(_randomArrayList);
        new SelectionSortListSorter(_comparator).sort(list);
        reportCalls(list);
    }

    public void testAverageCaseInsertionSort() {
        List list = _randomArrayList;
        List result = new CallCountingList(new ArrayList());
        new InsertionSortListSorter(_comparator).sort(list, result);
        reportCalls(result);
    }

    private void reportCalls(List list) {
        System.out.println(getName() + ": " + list);
    }
}
