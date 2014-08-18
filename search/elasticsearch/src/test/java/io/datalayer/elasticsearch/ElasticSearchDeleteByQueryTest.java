package io.datalayer.elasticsearch;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.matchAllQueryBuilder;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.junit.Test;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will delete by query entries in an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchDeleteByQueryTest extends ElasticSearchBaseTest {
    
    @Test
    public void testDeleteByQuery() throws IOException {

        indexName("delete-by-query");
        typeName("delete-by-query");
        int numDocs = 100;
        index(numDocs);

        DeleteByQueryResponse deleteByQueryResponse = client().prepareDeleteByQuery(indexName())
                .setQuery(matchAllQueryBuilder) //
                .execute() //
                .actionGet();
//        assertEquals(RestStatus.OK, deleteByQueryResponse.status());

    }

}
