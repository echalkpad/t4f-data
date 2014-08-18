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
package io.datalayer.data.comparator;

import io.datalayer.data.comparator.CompoundComparator;
import io.datalayer.data.comparator.NaturalComparator;
import junit.framework.TestCase;

/**
 * Test cases for {@link NaturalComparator}.
 *
 */
public class CompoundComparatorTest extends TestCase {
    public void testComparisonContinuesWhileEqual() {
        CompoundComparator comparator = new CompoundComparator();
        comparator.addComparator(new FixedComparator(0));
        comparator.addComparator(new FixedComparator(0));
        comparator.addComparator(new FixedComparator(0));

        assertTrue(comparator.compare("IGNORED", "IGNORED") == 0);
    }

    public void testComparisonStopsWhenLessThan() {
        CompoundComparator comparator = new CompoundComparator();
        comparator.addComparator(new FixedComparator(0));
        comparator.addComparator(new FixedComparator(0));
        comparator.addComparator(new FixedComparator(-57));
        comparator.addComparator(new FixedComparator(91));

        assertTrue(comparator.compare("IGNORED", "IGNORED") < 0);
    }

    public void testComparisonStopsWhenGreaterThan() {
        CompoundComparator comparator = new CompoundComparator();
        comparator.addComparator(new FixedComparator(0));
        comparator.addComparator(new FixedComparator(0));
        comparator.addComparator(new FixedComparator(91));
        comparator.addComparator(new FixedComparator(-57));

        assertTrue(comparator.compare("IGNORED", "IGNORED") > 0);
    }
}
