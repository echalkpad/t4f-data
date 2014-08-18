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
import io.datalayer.algorithm.bsearch.RecursiveBinaryListSearcher;
import io.datalayer.algorithm.list.LinearListSearcher;
import io.datalayer.algorithm.list.ListSearcher;
import io.datalayer.data.comparator.CallCountingComparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * Compares the performance of binary searching versus linear searching.
 *
 */
public class BinarySearchCallCountingTest extends TestCase {
    private static final int TEST_SIZE = 1021;

    private List _sortedList;
    private CallCountingComparator _comparator;

    protected void setUp() throws Exception {
        super.setUp();

        _sortedList = new ArrayList(TEST_SIZE);

        for (int i = 0; i < TEST_SIZE; ++i) {
            _sortedList.add(new Integer(i));
        }

        _comparator = new CallCountingComparator(NaturalComparator.INSTANCE);
    }

    public void testRecursiveBinarySearch() {
        performInOrderSearch(new RecursiveBinaryListSearcher(_comparator));
    }

    public void testIterativeBinarySearch() {
        performInOrderSearch(new IterativeBinaryListSearcher(_comparator));
    }

    public void testLinearSearch() {
        performInOrderSearch(new LinearListSearcher(_comparator));
    }

    public void testRandomRecursiveBinarySearch() {
        performRandomSearch(new RecursiveBinaryListSearcher(_comparator));
    }

    public void testRandomIterativeBinarySearch() {
        performRandomSearch(new IterativeBinaryListSearcher(_comparator));
    }

    public void testRandomLinearSearch() {
        performRandomSearch(new LinearListSearcher(_comparator));
    }

    private void performInOrderSearch(ListSearcher searcher) {
        for (int i = 0; i < TEST_SIZE; ++i) {
            searcher.search(_sortedList, new Integer(i));
        }

        reportCalls();
    }

    private void performRandomSearch(ListSearcher searcher) {
        for (int i = 0; i < TEST_SIZE; ++i) {
            searcher.search(_sortedList, new Integer((int) (TEST_SIZE * Math.random())));
        }

        reportCalls();
    }

    private void reportCalls() {
        System.out.println(getName() + ": " + _comparator.getCallCount() + " calls");
    }
}
