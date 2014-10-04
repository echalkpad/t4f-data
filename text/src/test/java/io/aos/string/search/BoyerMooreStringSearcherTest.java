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
package io.aos.string.search;

import io.aos.string.search.BoyerMooreStringSearcher;
import io.aos.string.search.StringMatch;
import io.aos.string.search.StringSearcher;

/**
 * Concrete test class for {@link BoyerMooreStringSearcher}.
 */
public class BoyerMooreStringSearcherTest extends AbstractStringSearcherTestCase {

    protected StringSearcher createSearcher(CharSequence pattern) {
        return new BoyerMooreStringSearcher(pattern);
    }

    public void testShiftsDontErroneouslyIgnoreMatches() {
        String text = "aababaa";
        String pattern = "baba";

        StringSearcher searcher = createSearcher(pattern);

        StringMatch match = searcher.search(text, 0);
        assertNotNull(match);
        assertEquals(text, match.getText());
        assertEquals(pattern, match.getPattern());
        assertEquals(2, match.getIndex());

        assertNull(searcher.search(text, match.getIndex() + 1));
    }
}
