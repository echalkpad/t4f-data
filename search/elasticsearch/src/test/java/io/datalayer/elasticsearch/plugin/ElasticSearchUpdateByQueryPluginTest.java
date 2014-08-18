package io.datalayer.elasticsearch.plugin;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.updatebyquery.BulkResponseOption;
import org.elasticsearch.action.updatebyquery.UpdateByQueryResponse;
import org.elasticsearch.client.UpdateByQueryClientWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will update by query entries in an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchUpdateByQueryPluginTest extends ElasticSearchBaseTest {
    private static UpdateByQueryClientWrapper updateByQueryClientWrapper;
    
    @BeforeClass
    public static void beforeClass() throws IOException {
        ElasticSearchBaseTest.beforeClass();
        updateByQueryClientWrapper = new UpdateByQueryClientWrapper(client());
    }
    
    @Test
    public void testUpdateByQuery() throws IOException {

        indexName("update-by-query");
        typeName("update-by-query");
        int numDocs = 100;
        index(numDocs);

        Map<String, Object> scriptParams = new HashMap<String, Object>();
        UpdateByQueryResponse response = updateByQueryClientWrapper.prepareUpdateByQuery()
                .setIndices(indexName())
                .setTypes(typeName())
                .setIncludeBulkResponses(BulkResponseOption.ALL)
                .setScript("ctx._source.uid += 1").setScriptParams(scriptParams)
                .setQuery(matchAllQuery())
                .execute()
                .actionGet();

        assertThat(response, notNullValue());
        assertThat(response.mainFailures().length, equalTo(0));
        assertThat(response.totalHits(), equalTo((long) numDocs));
        assertThat(response.updated(), equalTo((long) numDocs));
        assertThat(response.indexResponses().length, equalTo(1));
        assertThat(response.indexResponses()[0].countShardResponses(), equalTo((long) numDocs));

    }

}
