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
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * TODO: Document the use of List#equals.
 */
public abstract class AbstractListSorterTestCase extends TestCase {
    private List _unsortedList;
    private List _sortedList;

    protected void setUp() throws Exception {
        _unsortedList = new LinkedList();

        _unsortedList.add("test");
        _unsortedList.add("driven");
        _unsortedList.add("development");
        _unsortedList.add("is");
        _unsortedList.add("one");
        _unsortedList.add("small");
        _unsortedList.add("step");
        _unsortedList.add("for");
        _unsortedList.add("a");
        _unsortedList.add("programmer");
        _unsortedList.add("but");
        _unsortedList.add("it's");
        _unsortedList.add("one");
        _unsortedList.add("giant");
        _unsortedList.add("leap");
        _unsortedList.add("for");
        _unsortedList.add("programming");

        _sortedList = new LinkedList();

        _sortedList.add("a");
        _sortedList.add("but");
        _sortedList.add("development");
        _sortedList.add("driven");
        _sortedList.add("for");
        _sortedList.add("for");
        _sortedList.add("giant");
        _sortedList.add("is");
        _sortedList.add("it's");
        _sortedList.add("leap");
        _sortedList.add("one");
        _sortedList.add("one");
        _sortedList.add("programmer");
        _sortedList.add("programming");
        _sortedList.add("small");
        _sortedList.add("step");
        _sortedList.add("test");
    }

    protected void tearDown() throws Exception {
        _sortedList = null;
        _unsortedList = null;
    }

    /**
     * Test cases for specific algorithms should implement this factory
     * method to instantiate the given algorithm implementation.
     * @param comparator the comparator to control the order of sorted items.
     * @return the sorting implementation.
     */
    protected abstract ListSorter createListSorter(Comparator comparator);

    public void testListSorterCanSortSampleList() {
        ListSorter sorter = createListSorter(NaturalComparator.INSTANCE);
        assertEquals(_sortedList, sorter.sort(_unsortedList));
    }
}
