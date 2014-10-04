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

/**
 * A {@link StringSearcher} that uses a simplified Boyer-Moore algorithm.
 *
 */
public class BoyerMooreStringSearcher implements StringSearcher {
    /** The supported character set size (ASCII). */
    private static final int CHARSET_SIZE = 256;

    /** The pattern for which to search. */
    private final CharSequence _pattern;

    /** The position (0, 1, 2...) of the last occurrence of each character within the pattern. */
    private final short[] _lastOccurrence;

    /**
     * Constructor.
     *
     * @param pattern The pattern for which to search.
     */
    public BoyerMooreStringSearcher(CharSequence pattern) {
        assert pattern != null : "pattern can't be null";
        assert pattern.length() > 0 : "pattern can't be empty";

        _pattern = pattern;
        _lastOccurrence = computeLastOccurrence(pattern);
    }

    public StringMatch search(CharSequence text, int from) {
        assert text != null : "text can't be null";
        assert from >= 0 : "from can't be < 0";

        int s = from;

        while (s <= text.length() - _pattern.length()) {
            int i = _pattern.length() - 1;

            char c = 0;
            while (i >= 0 && _pattern.charAt(i) == (c = text.charAt(s + i))) {
                --i;
            }

            if (i < 0) {
                return new StringMatch(_pattern, text, s);
            }

            s += Math.max(i - _lastOccurrence[c], 1);
        }

        return null;
    }

    /**
     * Builds a table holding the position (0, 1, 2...) of the last occurrence of each character within a pattern. All
     * other characters are set to <code>-1</code>.
     *
     * @param pattern The pattern.
     * @return The table.
     */
    private static short[] computeLastOccurrence(CharSequence pattern) {
        short[] lastOccurrence = new short[CHARSET_SIZE];

        for (int i = 0; i < lastOccurrence.length; ++i) {
            lastOccurrence[i] = -1;
        }

        for (int i = 0; i < pattern.length(); ++i) {
            lastOccurrence[pattern.charAt(i)] = (short) i;
        }

        return lastOccurrence;
    }
}
