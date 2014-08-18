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
package io.datalayer.lucene.precision;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.TrecJudge;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SimpleQQParser;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * #1 Read TREC topics as QualityQuery[]
 * 
 * #2 Create Judge from TREC Qrel file
 * 
 * #3 Verify query and Judge match
 * 
 * #4 Create parser to translate queries into Lucene queries
 * 
 * #5 Run benchmark
 * 
 * #6 Print precision and recall measures
 */
public class PrecisionRecallMain {

    public static void main(String[] args) throws Throwable {

        File topicsFile = new File("aos/lucene/precision/topics.txt");
        File qrelsFile = new File("aos/lucene/precision/qrels.txt");
        Directory dir = FSDirectory.open(new File("indexes/MeetLucene"));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String docNameField = "filename";

        PrintWriter LOGGER = new PrintWriter(System.out, true);

        TrecTopicsReader qReader = new TrecTopicsReader();
        QualityQuery qqs[] = qReader.readQueries(new BufferedReader(new FileReader(topicsFile)));

        Judge judge = new TrecJudge(new BufferedReader(new FileReader(qrelsFile)));

        judge.validateData(qqs, LOGGER);

        QualityQueryParser qqParser = new SimpleQQParser("title", "contents");

        QualityBenchmark qrun = new QualityBenchmark(qqs, qqParser, searcher, docNameField);
        SubmissionReport submitLog = null;
        QualityStats stats[] = qrun.execute(judge, submitLog, LOGGER);

        QualityStats avg = QualityStats.average(stats);
        avg.log("SUMMARY", 2, LOGGER, "  ");

        dir.close();

    }

}
