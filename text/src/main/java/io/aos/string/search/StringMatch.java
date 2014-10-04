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
 * Represents a pattern match within some text.
 *
 */
public class StringMatch {
    /** The pattern that was searched for. */
    private final CharSequence _pattern;

    /** The text containing the match. */
    private final CharSequence _text;

    /** The position (0, 1, 2...) within the text at which tha pattern ocurred. */
    private final int _index;

    /**
     * Constructor.
     *
     * @param pattern The pattern that was searched for.
     * @param text The text containing the match.
     * @param index The position (0, 1, 2...) within the text at which tha pattern ocurred.
     */
    public StringMatch(CharSequence pattern, CharSequence text, int index) {
        assert text != null : "text can't be null";
        assert pattern != null : "pattern can't be null";
        assert index >= 0 : "index can't be < 0";

        _text = text;
        _pattern = pattern;
        _index = index;
    }

    public CharSequence getPattern() {
        return _pattern;
    }

    public CharSequence getText() {
        return _text;
    }

    public int getIndex() {
        return _index;
    }
}
