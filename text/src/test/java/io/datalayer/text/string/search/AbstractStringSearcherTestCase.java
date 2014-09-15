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
package io.datalayer.text.string.search;

import junit.framework.TestCase;

/**
 * Abstract base class for testing implementations of {@link StringMatch}.
 *
 */
public abstract class AbstractStringSearcherTestCase extends TestCase {
    protected abstract StringSearcher createSearcher(CharSequence pattern);

    public void testNotFoundInAnEmptyText() {
        StringSearcher searcher = createSearcher("NOT FOUND");

        assertNull(searcher.search("", 0));
    }

    public void testFindAtTheStart() {
        String text = "Find me at the start";
        String pattern = "Find";

        StringSearcher searcher = createSearcher(pattern);

        StringMatch match = searcher.search(text, 0);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(0, match.getIndex());

        assertNull(searcher.search(text, match.getIndex() + 1));
    }

    public void testFindAtTheEnd() {
        String text = "Find me at the end";
        String pattern = "end";

        StringSearcher searcher = createSearcher(pattern);

        StringMatch match = searcher.search(text, 0);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(15, match.getIndex());

        assertNull(searcher.search(text, match.getIndex() + 1));
    }

    public void testFindInTheMiddle() {
        String text = "Find me in the middle of the text";
        String pattern = "middle";

        StringSearcher searcher = createSearcher(pattern);

        StringMatch match = searcher.search(text, 0);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(15, match.getIndex());

        assertNull(searcher.search(text, match.getIndex() + 1));
    }

    public void testFindOverlapping() {
        String text = "abcdefffff-fedcba";
        String pattern = "fff";

        StringSearcher searcher = createSearcher(pattern);

        StringMatch match = searcher.search(text, 0);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(5, match.getIndex());

        match = searcher.search(text, match.getIndex() + 1);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(6, match.getIndex());

        match = searcher.search(text, match.getIndex() + 1);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(7, match.getIndex());

        assertNull(searcher.search(text, match.getIndex() + 1));
    }
}
