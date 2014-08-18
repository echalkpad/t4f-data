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
package io.datalayer.data.structure5;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * This class provides a way to connect text files to scanners,
 * without worrying about Exceptions.  
 * <p>
 * Typical usage:
 *
 * <pre>
 *     FileStream input = new FileStream("data.txt");
 *     Scanner scanner = new Scanner(input);
 *     while (scanner.hasNextLine()) {
 *         System.out.println(scanner.nextLine());
 *     }
 * </pre>
 * 
 * All the methods in this class are provided so that a Scanner can
 * read data from a file.  You won't need to use them if you use a
 * Scanner to read files (which is highly recommended).
 */
public class FileStream extends InputStream {

    private FileInputStream in;

    /**
     * Create a reader for the file with the given name.
     * <pre>
     *     FileStream input = new FileStream("data.txt");
     * </pre>
     *
     */
    public FileStream(String name) {
        try {
            in = new FileInputStream(name);
        } catch (Exception e) {
            throw new RuntimeException("Cannot access file " + name);
        }
    }


    public int available() {
        try {
            return in.available();      
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
        
    public void close() {
        try {
            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void mark(int readlimit) {
        try {
            in.mark(readlimit);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
        
    public boolean markSupported() {
        try {
            return in.markSupported();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
        
    public int read() {
        try {
            return in.read();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void reset() {
        try {
            in.reset();
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public long skip(long n) { 
        try {
            return in.skip(n);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static void main(String... args) {
        FileStream in = new FileStream(args[0]);
        Scanner s = new Scanner(in);
        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
    }

}
