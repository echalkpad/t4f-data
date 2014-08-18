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
package aos.lucene.analysis.queryparser;

import junit.framework.TestCase;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

// From chapter 4
public class AnalysisParalysisTest extends TestCase {
  public void testAnalyzer() throws Exception {
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
    String queryString = "category:/philosophy/eastern";

    Query query = new QueryParser(Version.LUCENE_46,
                                  "contents",
                                  analyzer).parse(queryString);
    assertEquals("path got split, yikes!",
                 "category:\"philosophy eastern\"",
                 query.toString("contents"));

    PerFieldAnalyzerWrapper perFieldAnalyzer =
                            new PerFieldAnalyzerWrapper(analyzer);
    perFieldAnalyzer.addAnalyzer("category",
                                       new WhitespaceAnalyzer(Version.LUCENE_46));
    query = new QueryParser(Version.LUCENE_46,
                            "contents",
                            perFieldAnalyzer).parse(queryString);
    assertEquals("leave category field alone",
                 "category:/philosophy/eastern",
                 query.toString("contents"));
  }
}
