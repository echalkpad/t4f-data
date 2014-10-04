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
 * An {@link AosIterator} over a {@link StringSearcher}. Successfull calls to {@link #current} will return a
 * {@link StringMatch}.
 */
public class StringMatchIterator implements AosIterator {
    /** The underlying searcher to use. */
    private final StringSearcher _searcher;

    /** The text to search. */
    private final CharSequence _text;

    /** The current match; or <code>null</code> if none. */
    private StringMatch _current;

    /**
     * Constructor.
     *
     * @param searcher The underlying searcher to use.
     */
    public StringMatchIterator(StringSearcher searcher, CharSequence text) {
        assert searcher != null : "searcher can't be null";
        assert text != null : "text can't be null";

        _searcher = searcher;
        _text = text;
    }

    public void first() {
        _current = _searcher.search(_text, 0);
    }

    public void last() {
        throw new UnsupportedOperationException();
    }

    public boolean isDone() {
        return _current == null;
    }

    public void next() {
        if (!isDone()) {
            _current = _searcher.search(_text, _current.getIndex() + 1);
        }
    }

    public void previous() {
        throw new UnsupportedOperationException();
    }

    public Object current() throws IteratorOutOfBoundsException {
        if (isDone()) {
            throw new IteratorOutOfBoundsException();
        }
        return _current;
    }
}
