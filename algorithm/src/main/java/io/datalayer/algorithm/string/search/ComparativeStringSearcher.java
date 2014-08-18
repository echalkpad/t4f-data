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


import io.datalayer.data.iterator.AosIterator;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Compares the performance of several {@link StringSearcher}s by searching a file for a pattern.
 *
 */
public final class ComparativeStringSearcher {
    /** The number of expected command-line arguments. */
    private static final int NUMBER_OF_ARGS = 2;

    /** The character encoding to use. */
    private static final String CHARSET_NAME = "8859_1";

    /** The name of the file to search. */
    private final String _filename;

    /** The pattern to search for. */
    private final String _pattern;

    /**
     * Constructor.
     *
     * @param filename The name of the file to search.
     * @param pattern The pattern to search for.
     */
    public ComparativeStringSearcher(String filename, String pattern) {
        assert filename != null : "filename can't be null";
        assert pattern != null : "pattern can't be null";

        _filename = filename;
        _pattern = pattern;
    }

    public static void main(String... args) throws IOException {
        assert args != null : "args can't be null";

        if (args.length < NUMBER_OF_ARGS) {
            System.err.println("Usage: ComparativeStringSearcher <file> <pattern>");
            System.exit(-1);
        }

        ComparativeStringSearcher searcher = new ComparativeStringSearcher(args[0], args[1]);

        searcher.run();
    }

    public void run() throws IOException {
        FileChannel fc = new FileInputStream(_filename).getChannel();
        try {
            ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());

            CharBuffer file = Charset.forName(CHARSET_NAME).newDecoder().decode(bbuf);

            System.out.println("Searching '" + _filename + "' (" + file.length() + ") for '" + _pattern + "'...");

            search(new BruteForceStringSearcher(_pattern), file);
            search(new BoyerMooreStringSearcher(_pattern), file);
        } finally {
            fc.close();
        }
    }

    /**
     * Performs an exhaustive search over some specified text to calculate and then print the number of occurrences and
     * comparisons for a given search algorithm.
     *
     * @param searcher The searcher to use.
     * @param file The file to search.
     */
    private void search(StringSearcher searcher, CharSequence file) {
        CallCountingCharSequence text = new CallCountingCharSequence(file);
        AosIterator i = new StringMatchIterator(searcher, text);

        int occurrence = 0;

        long startTime = System.currentTimeMillis();

        for (i.first(); !i.isDone(); i.next()) {
            ++occurrence;
        }

        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println(searcher.getClass().getName()
                + ": occurrences: " + occurrence
                + ", comparisons: " + text.getCallCount()
                + ", time: " + elapsedTime);
    }
}
