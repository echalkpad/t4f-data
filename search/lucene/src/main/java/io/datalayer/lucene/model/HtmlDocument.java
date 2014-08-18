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
package io.datalayer.lucene.model;

import io.datalayer.lucene.demo.parser.HTMLParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;

/**
 * A utility for making Lucene Documents for HTML documents.
 */
public class HtmlDocument {

    private static char DIR_SEPARATOR = System.getProperty("file.separator").charAt(0);

    private HtmlDocument() {
    }

    public static String uid(File f) {
        // Append path and date into a string in such a way that lexicographic
        // sorting gives the same results as a walk of the file hierarchy. Thus
        // null (\u0000) is used both to separate directory components and to
        // separate the path from the date.
        return f.getPath().replace(DIR_SEPARATOR, '\u0000') + "\u0000"
                + DateTools.timeToString(f.lastModified(), DateTools.Resolution.SECOND);
    }

    public static String uid2url(String uid) {
        String url = uid.replace('\u0000', '/'); // replace nulls with slashes
        return url.substring(0, url.lastIndexOf('/')); // remove date from end
    }

    public static Document Document(File f) throws IOException, InterruptedException {
        // make a new, empty document
        Document doc = new Document();

        // Add the url as a field named "path". Use a field that is
        // indexed (i.e. searchable), but don't tokenize the field into words.
        // doc.add(new Field("path", f.getPath().replace(dirSep, '/'),
        // Field.Store.YES, Field.Index.NOT_ANALYZED));

        // Add the last modified date of the file a field named "modified".
        // Use a field that is indexed (i.e. searchable), but don't tokenize
        // the field into words.
        // doc.add(new Field("modified",
        // DateTools.timeToString(f.lastModified(),
        // DateTools.Resolution.MINUTE),
        // Field.Store.YES, Field.Index.NOT_ANALYZED));

        // Add the uid as a field, so that index can be incrementally
        // maintained.
        // This field is not stored with document, it is indexed, but it is not
        // tokenized prior to indexing.
        // doc.add(new Field("uid", uid(f), Field.Store.NO,
        // Field.Index.NOT_ANALYZED));

        FileInputStream fis = new FileInputStream(f);
        HTMLParser parser = new HTMLParser(fis);

        // Add the tag-stripped contents as a Reader-valued Text field so it
        // will
        // get tokenized and indexed.
        // doc.add(new Field("contents", parser.getReader()));

        // Add the summary as a field that is stored and returned with
        // hit documents for display.
        // doc.add(new Field("summary", parser.getSummary(), Field.Store.YES,
        // Field.Index.NO));

        // Add the title as a field that it can be searched and that is stored.
        // doc.add(new Field("title", parser.getTitle(), Field.Store.YES,
        // Field.Index.ANALYZED));

        // return the document
        return doc;
    }

}
