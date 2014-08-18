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
package aos.lucene.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import io.aos.lucene.intro.Indexer;

public class TikaIndexer extends Indexer {

    private final boolean DEBUG = false;

    static Set<String> textualMetadataFields = new HashSet<String>();
    static {
        textualMetadataFields.add(Metadata.TITLE);
        textualMetadataFields.add(Metadata.AUTHOR);
        textualMetadataFields.add(Metadata.COMMENTS);
        textualMetadataFields.add(Metadata.KEYWORDS);
        textualMetadataFields.add(Metadata.DESCRIPTION);
        textualMetadataFields.add(Metadata.SUBJECT);
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java " + TikaIndexer.class.getName() + " <index dir> <data dir>");
        }

        TikaConfig config = TikaConfig.getDefaultConfig();
        List<String> parsers = new ArrayList<String>(config.getParsers().keySet());
        Collections.sort(parsers);
        Iterator<String> it = parsers.iterator();
        LOGGER.info("Mime type parsers:");
        while (it.hasNext()) {
            LOGGER.info("  " + it.next());
        }
        LOGGER.info();

        String indexDir = args[0];
        String dataDir = args[1];

        long start = new Date().getTime();
        TikaIndexer indexer = new TikaIndexer(indexDir);
        int numIndexed = indexer.index(dataDir, null);
        indexer.close();
        long end = new Date().getTime();

        LOGGER.info("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
    }

    public TikaIndexer(String indexDir) throws IOException {
        super(indexDir);
    }

    @Override
    protected Document getDocument(File f) throws Exception {

        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName()); 

        // If you know content type (eg because this document
        // was loaded from an HTTP server), then you should also
        // set Metadata.CONTENT_TYPE

        // If you know content encoding (eg because this
        // document was loaded from an HTTP server), then you
        // should also set Metadata.CONTENT_ENCODING

        InputStream is = new FileInputStream(f); 
        Parser parser = new AutoDetectParser(); 
        ContentHandler handler = new BodyContentHandler(); 
        ParseContext context = new ParseContext(); 
        context.set(Parser.class, parser); 

        try {
            parser.parse(is, handler, metadata, 
                    new ParseContext()); 
        }
        finally {
            is.close();
        }

        Document doc = new Document();

        doc.add(new Field("contents", handler.toString(), 0
                Field.Store.NO, Field.Index.ANALYZED)); 0

        if (DEBUG) {
            LOGGER.info("  all text: " + handler.toString());
        }

        for (String name : metadata.names()) { 1
            String value = metadata.get(name);

            if (textualMetadataFields.contains(name)) {
                doc.add(new Field("contents", value, 2
                        Field.Store.NO, Field.Index.ANALYZED));
            }

            doc.add(new Field(name, value, Field.Store.YES, Field.Index.NO)); 3

            if (DEBUG) {
                LOGGER.info("  " + name + ": " + value);
            }
        }

        if (DEBUG) {
            LOGGER.info();
        }

        doc.add(new Field("filename", f.getCanonicalPath(), 4
                Field.Store.YES, Field.Index.NOT_ANALYZED));

        return doc;
    }
}

/*
 * #1 Change to true to see all text #2 Which metadata fields are textual #3
 * List all mime types handled by Tika #4 Create Metadata for the file #5 Open
 * the file #6 Automatically determines file type #7 Extracts metadata and body
 * text #8 Setup ParseContext #9 Does all the work! #10 Index body content #11
 * Index metadata fields #12 Append to contents field #13 Separately store
 * metadata fields #14 Index file path
 */
