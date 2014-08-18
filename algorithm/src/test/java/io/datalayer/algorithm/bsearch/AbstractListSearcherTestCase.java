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

import io.datalayer.algorithm.list.ListSearcher;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * Abstract base class for testing implementations of {@link ListSearcher}.
 *
 */
public abstract class AbstractListSearcherTestCase extends TestCase {
    private static final Object[] VALUES = {"B", "C", "D", "F", "H", "I", "J", "K", "L", "M", "P", "Q"};

    private ListSearcher _searcher;
    private List _list;

    protected abstract ListSearcher createSearcher(Comparator comparator);

    protected void setUp() throws Exception {
        super.setUp();

        _searcher = createSearcher(NaturalComparator.INSTANCE);
        _list = new ArrayList(VALUES);
    }

    public void testSearchForExistingValues() {
        for (int i = 0; i < _list.size(); ++i) {
            assertEquals(i, _searcher.search(_list, _list.get(i)));
        }
    }

    public void testSearchForNonExistingValueLessThanFirstItem() {
        assertEquals(-1, _searcher.search(_list, "A"));
    }

    public void testSearchForNonExistingValueGreaterThanLastItem() {
        assertEquals(-13, _searcher.search(_list, "Z"));
    }

    public void testSearchForArbitraryNonExistingValue() {
        assertEquals(-4, _searcher.search(_list, "E"));
    }
}
