package io.datalayer.elasticsearch;

import static org.junit.Assert.assertEquals;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.Test;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will delete entries in an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchDeleteTest extends ElasticSearchBaseTest {
    
    @Test
    public void testDelete() throws IOException {

        indexName("delete");
        typeName("delete");
        int numDocs = 100;
        index(numDocs);

        DeleteResponse deleteResponse1 = client().prepareDelete(indexName(), typeName(), "1") //
                .execute() //
                .actionGet();
        assertEquals("1", deleteResponse1.getId());

        DeleteResponse deleteResponse2 = client().prepareDelete(indexName(), typeName(), "2") //
                .setOperationThreaded(false) //
                .execute() //
                .actionGet();
        assertEquals("2", deleteResponse2.getId());

    }

}
