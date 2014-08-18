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
package io.datalayer.algorithm.tstrees;


import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.LinkedList;
import io.datalayer.data.list.List;
import io.datalayer.data.tstree.TernarySearchTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads a word list into a {@link TernarySearchTree} and searches for a pattern.
 *
 */
public final class CrosswordHelper {
    /**
     * Constructor marked private to prevent instantiation.
     */
    private CrosswordHelper() {
    }

    public static void main(String... args) throws IOException {
        assert args != null : "args can't be null";

        if (args.length < 2) {
            System.out.println("Usage CrosswordHelper <word-list> <pattern> [repetitions]");
            System.exit(-1);
        }

        int repetitions = 1;
        if (args.length > 2) {
            repetitions = Integer.parseInt(args[2]);
        }

        searchForPattern(loadWords(args[0]), args[1], repetitions);
    }

    private static void searchForPattern(TernarySearchTree tree, String pattern, int repetitions) {
        assert tree != null : "tree can't be null";

        System.out.println("Searching for pattern '" + pattern + "'..." + repetitions + " times");

        List words = null;

        for (int i = 0; i < repetitions; ++i) {
            words = new LinkedList();
            tree.patternMatch(pattern, words);
        }

        AosIterator iterator = words.iterator();

        for (iterator.first(); !iterator.isDone(); iterator.next()) {
            System.out.println(iterator.current());
        }
    }

    private static TernarySearchTree loadWords(String fileName) throws IOException {
        TernarySearchTree tree = new TernarySearchTree();

        System.out.println("Loading words from '" + fileName + "'...");

        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        try {
            String word;

            while ((word = reader.readLine()) != null) {
                tree.add(word);
            }
        } finally {
            reader.close();
        }

        return tree;
    }
}
