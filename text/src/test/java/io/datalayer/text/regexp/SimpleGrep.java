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
package io.datalayer.text.regexp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple implementation of the ubiquitous grep command. First argument is the
 * regular expression to search for (remember to quote and/or escape as
 * appropriate). All following arguments are filenames to read and search for
 * the regular expression.
 */
public class SimpleGrep {

    public static void main(String[] argv) throws Exception {
        if (argv.length < 2) {
            System.out.println("Usage: regex file [ ... ]");
            return;
        }

        Pattern pattern = Pattern.compile(argv[0]);
        Matcher matcher = pattern.matcher("");

        for (int i = 1; i < argv.length; i++) {
            String file = argv[i];
            BufferedReader br = null;
            String line;

            try {
                br = new BufferedReader(new FileReader(file));
            } catch (IOException e) {
                System.err.println("Cannot read '" + file + "': " + e.getMessage());
                continue;
            }

            while ((line = br.readLine()) != null) {
                matcher.reset(line);

                if (matcher.find()) {
                    System.out.println(file + ": " + line);
                }
            }

            br.close();
        }
    }

}
