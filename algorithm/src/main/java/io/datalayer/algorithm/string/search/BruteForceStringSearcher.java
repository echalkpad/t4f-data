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
package io.datalayer.algorithm.string.search;

/**
 * A {@link StringSearcher} that uses a brute-force algorithm.
 *
 */
public class BruteForceStringSearcher implements StringSearcher {
    /** The pattern for which to search. */
    private final CharSequence _pattern;

    /**
     * Constructor.
     *
     * @param pattern The pattern for which to search.
     */
    public BruteForceStringSearcher(CharSequence pattern) {
        assert pattern != null : "pattern can't be null";
        assert pattern.length() > 0 : "pattern can't be empty";

        _pattern = pattern;
    }

    public StringMatch search(CharSequence text, int from) {
        assert text != null : "text can't be null";
        assert from >= 0 : "from can't be < 0";

        int s = from;

        while (s <= text.length() - _pattern.length()) {
            int i = 0;

            while (i < _pattern.length() && _pattern.charAt(i) == text.charAt(s + i)) {
                ++i;
            }

            if (i == _pattern.length()) {
                return new StringMatch(_pattern, text, s);
            }

            ++s;
        }

        return null;
    }
}
