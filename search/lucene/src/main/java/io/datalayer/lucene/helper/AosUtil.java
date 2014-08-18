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
package io.datalayer.lucene.helper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class AosUtil {

    public static final String COMPANY_DOMAIN = "example.com";
    public static final String BAD_DOMAIN = "yucky-domain.com";

    public static void indexNumbersMethod() {
        new StoredField("size", 4096);
        new StoredField("price", 10.99);
        new StoredField("author", "Arthur C. Clark");
    }

    public void ramDirExample() throws Exception {
        Directory ramDir = new RAMDirectory();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER);
        IndexWriter writer = new IndexWriter(ramDir, conf);
    }

    public void dirCopy() throws Exception {
        Directory otherDir = null;
        Directory ramDir = new RAMDirectory(otherDir, new IOContext());
    }

    public void addIndexes() throws Exception {
        Directory otherDir = null;
        Directory ramDir = null;
        IndexWriter writer = new IndexWriter(otherDir, new IndexWriterConfig(Version.LUCENE_46, new SimpleAnalyzer(
                Version.LUCENE_46)));
        writer.addIndexes(new Directory[] { ramDir });
    }

    /**
     * #1 Good domain boost factor: 1.5
     * 
     * #2 Bad domain boost factor: 0.1
     */
    public void docBoostMethod() throws IOException {

        Directory dir = new RAMDirectory();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER);
        IndexWriter writer = new IndexWriter(dir, conf);

        Document doc = new Document();
        String senderEmail = getSenderEmail();
        String senderName = getSenderName();
        String subject = getSubject();
        String body = getBody();
        doc.add(new StoredField("senderEmail", senderEmail));
        doc.add(new Field("senderName", senderName, AosFieldType.INDEXED_STORED_TERMVECTORS));
        doc.add(new Field("subject", subject, AosFieldType.INDEXED_STORED_TERMVECTORS));
        doc.add(new Field("body", body, AosFieldType.INDEXED_STORED_TERMVECTORS));
        String lowerDomain = getSenderDomain().toLowerCase();
        if (isImportant(lowerDomain)) {
            // doc.setBoost(1.5F);
        }
        else if (isUnimportant(lowerDomain)) {
            // doc.setBoost(0.1F);
        }

        writer.addDocument(doc);

        writer.close();

    }

    public void fieldBoostMethod() throws IOException {
        Field subjectField = new Field("subject", getSubject(), AosFieldType.INDEXED_STORED_TERMVECTORS);
        subjectField.setBoost(1.2F);
    }

    public void numberField() {
        Document doc = new Document();
        doc.add(new StoredField("price", 19.99));
    }

    public void numberTimestamp() {
        Document doc = new Document();
        doc.add(new StoredField("timestamp", new Date().getTime()));
        doc.add(new StoredField("day", (int) (new Date().getTime() / 24 / 3600)));
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        doc.add(new StoredField("dayOfMonth", cal.get(Calendar.DAY_OF_MONTH)));
    }

    public void setInfoStream() throws Exception {
        Directory dir = null;
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46,
                AosAnalyser.NO_LIMIT_TOKEN_COUNT_SIMPLE_ANALYSER);
        conf.setInfoStream(System.out);
        IndexWriter writer = new IndexWriter(dir, conf);
    }

    public void dateField() {
        Document doc = new Document();
        doc.add(new StoredField("indexDate", DateTools.dateToString(new Date(), DateTools.Resolution.DAY)));
    }

    public void numericField() throws Exception {
        Document doc = new Document();
        StoredField price = new StoredField("price", 19.99);
        doc.add(price);

        StoredField timestamp = new StoredField("timestamp", new Date().getTime());
        doc.add(timestamp);

        Date b = new Date();
        String v = DateTools.dateToString(b, DateTools.Resolution.DAY);
        StoredField birthday = new StoredField("birthday", v);
        doc.add(birthday);
    }

    public void indexAuthors() throws Exception {
        String[] authors = new String[] { "lisa", "tom" };
        Document doc = new Document();
        for (String author : authors) {
            doc.add(new StoredField("author", author));
        }
    }

    private String getSenderEmail() {
        return "bob@smith.com";
    }

    private String getSenderName() {
        return "Bob Smith";
    }

    private String getSenderDomain() {
        return COMPANY_DOMAIN;
    }

    private String getSubject() {
        return "Hi there Lisa";
    }

    private String getBody() {
        return "I don't have much to say";
    }

    private boolean isImportant(String lowerDomain) {
        return lowerDomain.endsWith(COMPANY_DOMAIN);
    }

    private boolean isUnimportant(String lowerDomain) {
        return lowerDomain.endsWith(BAD_DOMAIN);
    }

}
