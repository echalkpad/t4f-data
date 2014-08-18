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
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;
import junit.framework.TestCase;

/**
 * An example answer to exercise 6-2.
 * Prove that the basic sorting algorithms are stable.
 *
 */
public class ListSorterStabilityTest extends TestCase {
    private static final int TEST_SIZE = 1000;

    private final List _list = new ArrayList(TEST_SIZE);
    private final Comparator _comparator = new FractionComparator();

    /**
     * We create a list of fractions with monotonically increasing denominators
     * and repeating numerators in the range 0..19. We will then use our FractionComparator
     * below to sort by the numerator and we should find that the denominators are in increasing
     * order within the same numerator sorted group.
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        for (int i = 1; i < TEST_SIZE; ++i) {
            _list.add(new Fraction(i % 20, i));
        }
    }

    public void testStabilityOfBubblesort() {
        ListSorter listSorter = new BubblesortListSorter(_comparator);
        List result = listSorter.sort(_list);
        assertStableSorted(result);
    }

    public void testStabilityOfInsertionsort() {
        ListSorter listSorter = new InsertionSortListSorter(_comparator);
        List result = listSorter.sort(_list);
        assertStableSorted(result);
    }

    private void assertStableSorted(List list) {
        for (int i = 1; i < list.size(); i++) {
            Fraction f1 = (Fraction) list.get(i - 1);
            Fraction f2 = (Fraction) list.get(i);
            if(!(f1.getNumerator() < f2.getNumerator()
                    || f1.getDenominator() < f2.getDenominator())) {
                fail("what?!");
            }
        }
    }

    private static class Fraction {
        private final int _numerator;
        private final int _denominator;

        public Fraction(int numerator, int denominator) {
            _numerator = numerator;
            _denominator = denominator;
        }

        public int getNumerator() {
            return _numerator;
        }

        public int getDenominator() {
            return _denominator;
        }
    }

    private static class FractionComparator implements Comparator {
        public int compare(Object left, Object right) throws ClassCastException {
            return compare((Fraction) left, (Fraction) right);
        }

        /**
         * We only take into account the numerator during the sort.
         * We deliberately ignore the denominator to allow the algorithm to sort
         * within the numerator group as it sees fit.
         */
        private int compare(Fraction l, Fraction r) throws ClassCastException {
            return l.getNumerator() - r.getNumerator();
        }
    }
}
