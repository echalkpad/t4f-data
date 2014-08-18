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
package io.datalayer.data.bstree;

import io.datalayer.data.bstree.BinarySearchTree;
import io.datalayer.data.comparator.CallCountingComparator;
import io.datalayer.data.comparator.NaturalComparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

import org.junit.Ignore;

/**
 */
@Ignore
public class BinarySearchTreeCallCountingTest extends TestCase {
    private static final int TEST_SIZE = 1000;

    private CallCountingComparator _comparator;
    private BinarySearchTree _tree;

    protected void setUp() throws Exception {
        super.setUp();

        _comparator = new CallCountingComparator(NaturalComparator.INSTANCE);
        _tree = new BinarySearchTree(_comparator);
    }

    public void testRandomInsertion() {
        for (int i = 0; i < TEST_SIZE; ++i) {
            _tree.insert(new Integer((int) (Math.random() * TEST_SIZE)));
        }

        reportCalls();
    }

    public void testInOrderInsertion() {
        for (int i = 0; i < TEST_SIZE; ++i) {
            _tree.insert(new Integer(i));
        }

        reportCalls();
    }

    public void testPreOrderInsertion() {
        List list = new ArrayList(TEST_SIZE);

        for (int i = 0; i < TEST_SIZE; ++i) {
            list.add(new Integer(i));
        }

        preOrderInsert(list, 0, list.size() - 1);

        reportCalls();
    }

    private void preOrderInsert(List list, int lowerIndex, int upperIndex) {
        if (lowerIndex > upperIndex) {
            return;
        }

        int index = lowerIndex + (upperIndex - lowerIndex) / 2;

        _tree.insert(list.get(index));
        preOrderInsert(list, lowerIndex, index - 1);
        preOrderInsert(list, index + 1, upperIndex);
    }

    private void reportCalls() {
        System.out.println(getName() + ": " + _comparator.getCallCount() + " calls");
    }
}
