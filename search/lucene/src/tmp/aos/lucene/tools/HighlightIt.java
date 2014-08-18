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
package aos.lucene.tools;

import java.io.FileWriter;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;

public class HighlightIt {

    private static final String text = "In this section we'll show you how to make the simplest " //
            + "programmatic query, searching for a single term, and then " //
            + "we'll see how to use QueryParser to accept textual queries. " //
            + "In the sections that follow, we’ll take this simple example " //
            + "further by detailing all the query types built into Lucene. " //
            + "We begin with the simplest search of all: searching for all " //
            + "documents that contain a single term.";

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: HighlightIt <filename-out>");
            System.exit(-1);
        }

        String filename = args[0];

        String searchText = "term"; //
        QueryParser parser = new QueryParser(Version.LUCENE_46, //
                "f", //
                new StandardAnalyzer(Version.LUCENE_46));// #1
        Query query = parser.parse(searchText); //

        SimpleHTMLFormatter formatter = //
        new SimpleHTMLFormatter("<span class=\"highlight\">", //
                "</span>"); //

        TokenStream tokens = new StandardAnalyzer(Version.LUCENE_46) //
                .tokenStream("f", new StringReader(text)); //

        QueryScorer scorer = new QueryScorer(query, "f"); //

        Highlighter highlighter = new Highlighter(formatter, scorer); //
        highlighter.setTextFragmenter( //
                new SimpleSpanFragmenter(scorer)); //

        String result = //
        highlighter.getBestFragments(tokens, text, 3, "..."); //

        FileWriter writer = new FileWriter(filename); //
        writer.write("<html>"); //
        writer.write("<style>\n" + //
                ".highlight {\n" + //
                " background: yellow;\n" + //
                "}\n" + //
                "</style>"); //
        writer.write("<body>"); //
        writer.write(result); //
        writer.write("</body></html>"); //
        writer.close(); //
    }
}

/*
 * #1 Create the query #2 Customize surrounding tags #3 Tokenize text #4 Create
 * QueryScorer #5 Create highlighter #6 Use SimpleSpanFragmenter to fragment #7
 * Highlight best 3 fragments #8 Write highlighted HTML
 */
