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
 * Test cases for {@link StringMatchIterator}.
 */
public class StringMatchIteratorTest extends TestCase {
    public void test() {
        String pattern = "abba";
        String text = "abbabbaabba";

        AosIterator iterator = new StringMatchIterator(new BruteForceStringSearcher(pattern), text);

        iterator.first();
        assertFalse(iterator.isDone());
        StringMatch match = (StringMatch) iterator.current();
        assertNotNull(match);
        assertSame(pattern, match.getPattern());
        assertSame(text, match.getText());
        assertEquals(0, match.getIndex());

        iterator.next();
        assertFalse(iterator.isDone());
        match = (StringMatch) iterator.current();
        assertNotNull(match);
        assertSame(pattern, match.getPattern());
        assertSame(text, match.getText());
        assertEquals(3, match.getIndex());

        iterator.next();
        assertFalse(iterator.isDone());
        match = (StringMatch) iterator.current();
        assertNotNull(match);
        assertSame(pattern, match.getPattern());
        assertSame(text, match.getText());
        assertEquals(7, match.getIndex());

        iterator.next();
        assertTrue(iterator.isDone());

        try {
            iterator.current();
            fail();
        }
        catch (IteratorOutOfBoundsException e) {
            // expected
        }
    }
}
