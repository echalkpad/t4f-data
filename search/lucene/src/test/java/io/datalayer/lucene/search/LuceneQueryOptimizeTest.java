package io.datalayer.lucene.search;

import io.datalayer.lucene.demo.parser.ParseException;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuceneQueryOptimizeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneQueryOptimizeTest.class);

    // String indexDir = "./aos.index.test/test2";
    // Directory dir = FSDirectory.open(new File(indexDir));

    //
    // IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, new
    // StandardAnalyzer(Version.LUCENE_46));
    // IndexWriter indexWriter = new IndexWriter(dir, config);
    // addDocuments(indexWriter);
    // writer.merge(writer.getNextMerge());
    // indexWriter.close();
    //
    // IndexReader reader = DirectoryReader.open(FSDirectory.open(new
    // File(indexDir)));
    // IndexSearcher indexSearcher = new IndexSearcher(reader);
    // String queryString = "lucene"; // the "title" arg specifies the default
    // // field to use - when no field is
    // // explicitly specified in the query.
    // Query query = new QueryParser(Version.LUCENE_46, "title",
    // analyzer).parse(queryString);
    // query(indexSearcher, query);
    // queryString = "tag4";
    // query = new QueryParser(Version.LUCENE_46, "tag",
    // analyzer).parse(queryString);
    // query(indexSearcher, query);
    //
    // LOGGER.info("\n==============================");
    //
    // config = new IndexWriterConfig(Version.LUCENE_46, new
    // StandardAnalyzer(Version.LUCENE_46));
    // indexWriter = new IndexWriter(dir, config);
    // addDocuments(indexWriter);
    // addDocuments(indexWriter);
    // writer.merge(writer.getNextMerge());
    // indexWriter.close();
    //
    // queryString = "lucene"; // the "title" arg specifies the default field
    // // to use - when no field is explicitly
    // // specified in the query.
    // query = new QueryParser(Version.LUCENE_46, "title",
    // analyzer).parse(queryString);
    // query(indexSearcher, query);
    // queryString = "tag4";
    // query = new QueryParser(Version.LUCENE_46, "tag",
    // analyzer).parse(queryString);
    // query(indexSearcher, query);
    //
    // // searcher can only be closed when there
    // // is no need to access the documents any more.
    // // indexSearcher.close();
    //
    // LOGGER.info("\n==============================");
    //
    // config = new IndexWriterConfig(Version.LUCENE_46, new
    // StandardAnalyzer(Version.LUCENE_46));
    // indexWriter = new IndexWriter(dir, config);
    // addDocuments(indexWriter);
    // addDocuments(indexWriter);
    // writer.merge(writer.getNextMerge());
    // indexWriter.close();
    //
    // indexSearcher = new IndexSearcher(reader);
    // queryString = "lucene"; // the "title" arg specifies the default field
    // // to use - when no field is explicitly
    // // specified in the query.
    // query = new QueryParser(Version.LUCENE_46, "title",
    // analyzer).parse(queryString);
    // query(indexSearcher, query);
    // queryString = "tag4";
    // query = new QueryParser(Version.LUCENE_46, "tag",
    // analyzer).parse(queryString);
    // query(indexSearcher, query);
    // // indexSearcher.close();
    //
    // LOGGER.info("\n==============================");
    //
    // IndexSearcher indexSearcher2 = new IndexSearcher(reader);
    // queryString = "lucene"; // the "title" arg specifies the default field
    // // to use - when no field is explicitly
    // // specified in the query.
    // query = new QueryParser(Version.LUCENE_46, "title",
    // analyzer).parse(queryString);
    // query(indexSearcher2, query);
    // queryString = "tag4";
    // query = new QueryParser(Version.LUCENE_46, "tag",
    // analyzer).parse(queryString);
    // query(indexSearcher2, query);
    // // indexSearcher2.close();
    //
    // }

    private static void addDocuments(IndexWriter indexWriter) throws IOException {
        addDocument(
                indexWriter,
                "Lucene in Action - Lucene in Action - Lucene in Action - Lucene in Action - Lucene in Action - Lucene in Action - ",
                new String[] { "tag1", "tag2" });
        addDocument(
                indexWriter,
                "Lucene for Dummies - Lucene for Dummies - Lucene for Dummies - Lucene for Dummies - Lucene for Dummies",
                new String[] { "tag1", "tag2" });
        addDocument(indexWriter, "Managing Gigabytes", new String[] { "tag1", "tag3" });
        addDocument(indexWriter, "The Art of Computer Science", new String[] { "tag1", "tag4" });
    }

    private static void addDocument(IndexWriter indexWriter, String title, String[] tags) throws IOException {
        Document doc = new Document();
        // doc.add(new Field("title", title, Field.Store.YES,
        // Field.Index.ANALYZED));
        for (String tag : tags) {
            // doc.add(new Field("tag", tag, Field.Store.YES,
            // Field.Index.NOT_ANALYZED));
        }
        indexWriter.addDocument(doc);
    }

    private static void query(IndexSearcher indexSearcher, Query q) throws IOException, ParseException {

        int hitsPerPage = 10;
        TopDocsCollector collector = TopScoreDocCollector.create(hitsPerPage, false);
        indexSearcher.search(q, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        LOGGER.info("Found " + hits.length + " hits.");

        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = indexSearcher.doc(docId);
            LOGGER.info((i + 1) + ". " + d.get("title"));
        }

    }

}
