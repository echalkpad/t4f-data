package io.datalayer.elasticsearch;

import static org.junit.Assert.assertNotNull;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will manage an elasticsearch cluster.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchClusterTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClusterTest.class);
    
    @Test
    public void testHealth() throws IOException {
        
        client().prepareIndex(indexName(), typeName()) //
                .setSource("{ \"field\": \"value\" }") //
                .execute() //
                .actionGet();
        
        refresh();
        
        // Don't do this, a request via the JAVA API is not a JSON...
//        ClusterHealthRequest clusterHealthRequest = Requests.clusterHealthRequest(indexName());
//        BytesStreamOutput bso = new BytesStreamOutput();
//        clusterHealthRequest.writeTo(bso);
//        bso.close();
//        LOGGER.info("Request=" + new String(bso.bytes().toBytes()));

        ClusterHealthResponse clusterHealthResponse = client().admin() //
            .cluster() //
            .prepareHealth() //
//            .setWaitForEvents(Priority.LANGUID) //
//            .setWaitForGreenStatus() //
//            .setWaitForYellowStatus() //
            .execute() //
            .actionGet();
        
        assertNotNull(clusterHealthResponse.getStatus());
        
    }

}
