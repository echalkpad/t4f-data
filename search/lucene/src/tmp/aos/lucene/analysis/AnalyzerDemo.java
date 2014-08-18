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
package aos.lucene.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

// From chapter 4

/**
 * Adapted from code which first appeared in a java.net article written by Erik
 */
// #A Analyze command-line strings, if specified
// #B Real work done in here
public class AnalyzerDemo {
    private static final String[] examples = { "The quick brown fox jumped over the lazy dog",
            "XY&Z Corporation - xyz@example.com" };

    private static final Analyzer[] analyzers = new Analyzer[] { new WhitespaceAnalyzer(Version.LUCENE_46),
            new SimpleAnalyzer(), new StopAnalyzer(Version.LUCENE_46), new StandardAnalyzer(Version.LUCENE_46) };

    public static void main(String[] args) throws IOException {

        String[] strings = examples;

        if (args.length > 0) {
            strings = args;
        }

        for (String text : strings) {
            analyze(text);
        }

    }

    private static void analyze(String text) throws IOException {

        LOGGER.info("Analyzing \"" + text + "\"");

        for (Analyzer analyzer : analyzers) {
            String name = analyzer.getClass().getSimpleName();
            LOGGER.info("  " + name + ":");
            System.out.print("    ");
            AnalyzerUtils.displayTokens(analyzer, text);
            LOGGER.info("\n");
        }

    }

}
