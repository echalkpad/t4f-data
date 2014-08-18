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
package io.datalayer.algorithm.tree;

import io.datalayer.data.iterable.ArrayIterator;
import io.datalayer.data.iterator.AosIterator;

import java.io.File;

/**
 * Simple class to print out the contents of a directory tree.
 *
 */
public final class RecursiveDirectoryTreePrinter {
    /** The number of spaces to indent each recursion. */
    private static final String SPACES = "  ";

    /**
     * Constructor marked private to prevent instantiation.
     */
    private RecursiveDirectoryTreePrinter() {
    }

    /**
     * Program mainline.
     *
     * @param args Command-line arguments.
     */
    public static void main(String... args) {
        assert args != null : "args can't be null";

        if (args.length != 1) {
            System.err.println("Usage: RecursiveDirectoryTreePrinter <dir>");
            System.exit(-1);
        }

        System.out.println("Recursively printing directory tree for: " + args[0]);
        print(new File(args[0]), "");
    }

    /**
     * Prints a list of files/directories with the given indentation.
     *
     * @param files The files/directories to print.
     * @param indent The amount of indentation.
     */
    private static void print(AosIterator files, String indent) {
        assert files != null : "files can't be null";

        for (files.first(); !files.isDone(); files.next()) {
            print((File) files.current(), indent);
        }
    }

    /**
     * Prints a file or directory with the given indentation.
     *
     * @param file  The file or directory to print.
     * @param indent The amount of indentation.
     */
    private static void print(File file, String indent) {
        assert file != null : "file can't be null";
        assert indent != null : "indent can't be null";

        System.out.print(indent);
        System.out.println(file.getName());

        if (file.isDirectory()) {
            print(new ArrayIterator(file.listFiles()), indent + SPACES);
        }
    }
}
