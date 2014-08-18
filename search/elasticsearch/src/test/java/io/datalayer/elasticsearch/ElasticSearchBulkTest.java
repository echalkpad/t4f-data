package io.datalayer.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Requests;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will bulk-upload an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchBulkTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchBulkTest.class);
    
    @Test
    public void testBulk() throws IOException {
        
        indexName("bulk");
        typeName("bulk");

        for (int i = 0; i < NUMBER_OF_ACTIONS; i++) {

            BulkRequestBuilder bulkRequest = client().prepareBulk();
            
            for (int j = 0; j < BATCH_SIZE; j++) {
        
                // Either use client#prepare, or use Requests# to directly build index/delete requests
//                bulkRequest.add(client().prepareIndex(indexName() + "-" + i, typeName(), "1") //
//                        .setSource("field", "value"));

                bulkRequest.add(Requests.indexRequest(indexName()) //
                        .type(typeName()) //
                        .source(jsonBuilder() //
                                .startObject() //
                                .field("user", "eric1", "eric2", "eric3") //
                                .field("birth", ThreadLocalRandom.current().nextLong()) //
//                                .field("location", "30, -70") //
                                .startObject("location") //
                                .field("lat", 40.7143528) //
                                .field("lon", -74.0059731) //
                                .endObject() //
                                .field("postDate", new Date()) //
                                .field("message", "trying out Elastic Search" + j) //
                                .endObject() //
                        ));
            }

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse: bulkResponse.getItems()) {
                    if (bulkItemResponse.getFailure() != null) {
                        LOGGER.error(bulkItemResponse.getFailureMessage());
                    }
                }
            }

        }

    }

}
