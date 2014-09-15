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
package io.datalayer.text.string.match;

import junit.framework.TestCase;

/**
 * Test cases for {@link LevenshteinWordDistanceCalculator}.
 *
 */
public class LevenshteinWordDistanceCalculatorTest extends TestCase {
    private LevenshteinWordDistanceCalculator _calculator;

    protected void setUp() throws Exception {
        super.setUp();

        _calculator = LevenshteinWordDistanceCalculator.DEFAULT;
    }

    public void testEmptyToEmpty() {
        assertDistance(0, "", "");
    }

    public void testEmptyToNonEmpty() {
        String target = "any";
        assertDistance(target.length(), "", target);
    }

    public void testSamePrefix() {
        assertDistance(3, "unzip", "undo");
    }

    public void testSameSuffix() {
        assertDistance(4, "eating", "running");
    }

    public void testArbitrary() {
        assertDistance(3, "msteak", "mistake");
        assertDistance(3, "necassery", "neccessary");
        assertDistance(5, "donkey", "mule");
    }

    private void assertDistance(int distance, String source, String target) {
        assertEquals(distance, _calculator.calculate(source, target));
        assertEquals(distance, _calculator.calculate(target, source));
    }
}
