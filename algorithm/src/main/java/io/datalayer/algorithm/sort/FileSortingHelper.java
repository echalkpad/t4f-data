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
package io.datalayer.algorithm.sort;


import io.datalayer.algorithm.list.ListSorter;
import io.datalayer.algorithm.sort.shell.ShellsortListSorter;
import io.datalayer.data.comparator.Comparator;
import io.datalayer.data.comparator.ReverseStringComparator;
import io.datalayer.data.iterator.AosIterator;
import io.datalayer.data.list.ArrayList;
import io.datalayer.data.list.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Loads a word list into a {@link List} and sort it.
 *
 */
public final class FileSortingHelper {
    /**
     * Constructor marked private to prevent instantiation.
     */
    private FileSortingHelper() {
    }

    /**
     * The program entry point.
     * @param args
     * @throws IOException if input cannot be read.
     */
    public static void main(String... args) throws IOException {
        sort(loadWords());

        System.err.println("Finished...press CTRL-C to exit");

        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            // ignore it
        }
    }

    private static void sort(List wordList) {
        assert wordList != null : "tree can't be null";

        System.out.println("Starting sort...");

        Comparator comparator = ReverseStringComparator.INSTANCE;
        ListSorter sorter = new ShellsortListSorter(comparator);

        List sorted = sorter.sort(wordList);

        AosIterator i = sorted.iterator();
        i.first();
        while (!i.isDone()) {
            System.out.println(i.current());
            i.next();
        }
    }

    private static List loadWords() throws IOException {
        List result = new ArrayList();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String word;

            while ((word = reader.readLine()) != null) {
                result.add(word);
            }
        } finally {
            reader.close();
        }

        return result;
    }
}
