package io.datalayer.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will percolate an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchPercolateTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchPercolateTest.class);
    
    @Test
    public void test10Percolate() throws IOException {

        // This is the query we're registering in the Percolator.
        QueryBuilder qb = termQuery("content", "amazing");

        // Index the query = register it in the Percolator.
        client().prepareIndex(indexName(), typeName(), "1") //
                .setSource(qb.buildAsBytes()) //
                .setRefresh(true) // Needed when the query shall be available immediately.
                .execute().actionGet();

        // Build a document to check against the Percolator.
        XContentBuilder docBuilder = jsonBuilder().startObject();
        docBuilder.field("doc").startObject(); // This is needed to designate the document.
        docBuilder.field("content", "This is amazing!");
        docBuilder.endObject(); // End of the doc field.
        docBuilder.endObject(); // End of the JSON root object.

        // Percolate.
        PercolateResponse response = client().preparePercolate() //
                .setIndices(indexName()) //
                .setSource(docBuilder) //
                .execute() //
                .actionGet();
        // Iterate over the results.
        for (PercolateResponse.Match match : response) {
            LOGGER.info("Result=" + match.getIndex().string());
        }

    }

}
