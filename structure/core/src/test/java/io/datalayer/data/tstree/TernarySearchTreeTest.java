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
package io.datalayer.data.tstree;

import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;
import io.datalayer.data.tstree.TernarySearchTree;
import junit.framework.TestCase;

/**
 * Test cases for {@link TernarySearchTree}.
 *
 */
public class TernarySearchTreeTest extends TestCase {
    private TernarySearchTree _tree;

    protected void setUp() throws Exception {
        super.setUp();

        _tree = new TernarySearchTree();

        _tree.add("prefabricate");
        _tree.add("presume");
        _tree.add("prejudice");
        _tree.add("preliminary");
        _tree.add("apple");
        _tree.add("ape");
        _tree.add("appeal");
        _tree.add("car");
        _tree.add("dog");
        _tree.add("cat");
        _tree.add("mouse");
        _tree.add("mince");
        _tree.add("minty");
    }

    public void testContains() {
        assertTrue(_tree.contains("prefabricate"));
        assertTrue(_tree.contains("presume"));
        assertTrue(_tree.contains("prejudice"));
        assertTrue(_tree.contains("preliminary"));
        assertTrue(_tree.contains("apple"));
        assertTrue(_tree.contains("ape"));
        assertTrue(_tree.contains("appeal"));
        assertTrue(_tree.contains("car"));
        assertTrue(_tree.contains("dog"));
        assertTrue(_tree.contains("cat"));
        assertTrue(_tree.contains("mouse"));
        assertTrue(_tree.contains("mince"));
        assertTrue(_tree.contains("minty"));

        assertFalse(_tree.contains("pre"));
        assertFalse(_tree.contains("dogs"));
        assertFalse(_tree.contains("UNKNOWN"));
    }

    public void testPrefixSearch() {
        assertPrefixEquals(new String[] {"prefabricate", "prejudice", "preliminary", "presume"}, "pre");
        assertPrefixEquals(new String[] {"ape", "appeal", "apple"}, "ap");
    }

    public void testPatternMatch() {
        assertPatternEquals(new String[] {"mince", "mouse"}, "m???e");
        assertPatternEquals(new String[] {"car", "cat"}, "?a?");
    }

    private void assertPrefixEquals(String[] expected, String prefix) {
        List words = new LinkedList();

        _tree.prefixSearch(prefix, words);

        assertEquals(expected, words);
    }

    private void assertPatternEquals(String[] expected, String pattern) {
        List words = new LinkedList();

        _tree.patternMatch(pattern, words);

        assertEquals(expected, words);
    }

    private void assertEquals(String[] expected, List actual) {
        assertEquals(expected.length, actual.size());

        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual.get(i));
        }
    }
}
