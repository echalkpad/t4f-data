package io.datalayer.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.optimize.OptimizeRequest;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexMissingException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will manage an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticSearchManageIndexTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchManageIndexTest.class);
    
    @Test
    public void test1DeleteIndex() throws IOException {
        try {
            DeleteIndexResponse deleteIndexResponse = client().admin() //
                    .indices() //
                    .delete(new DeleteIndexRequest(indexName())) //
                    .actionGet();
            assertTrue(deleteIndexResponse.isAcknowledged());
        }
        catch (IndexMissingException e) {
            // Do nothing, the index was not present...
        }
    }

    @Test
    public void test2CreateMappedIndex() throws IOException {
        
        String mapping = XContentFactory.jsonBuilder() //
                .startObject() //
                .startObject("type1") //
                .startObject("properties") //
                .startObject("location") //
                .field("type", "geo_point") //
                .field("lat_lon", true) //
                .endObject() //
                .endObject() //
                .endObject() //
                .endObject() //
                .string();
        LOGGER.info("Mapping=" + mapping);

        CreateIndexResponse createIndexResponse = client().admin() //
                .indices() //
                .prepareCreate(indexName()) //
                .addMapping(typeName(), mapping) //
                .execute() //
                .actionGet();
        
        assertTrue(createIndexResponse.isAcknowledged());

    }
    
    @Test
    public void test3CreateMappedIndex() throws IOException {
        
        indexName("mapping-1");
        typeName("mapping-1");
        
        Settings indexSettings =
                ImmutableSettings.settingsBuilder()
                                 .loadFromClasspath("aos.elasticsearch.settings-1.json").build();
        
        CreateIndexResponse createIndexResponse = client().admin() //
                .indices() //
                .prepareCreate(indexName()) //
                .addMapping(typeName(), resourceAsString("aos.elasticsearch.mappings-1.json")) //
                .setSettings(indexSettings) //
                .execute() //
                .actionGet();
        
        assertTrue(createIndexResponse.isAcknowledged());

    }

    @Test
    public void test4OptimizedIndex() throws IOException {
        
        OptimizeRequest optimizeRequest = new OptimizeRequest();
        optimizeRequest.indices(indexName());
        optimizeRequest.maxNumSegments(1);
        
        OptimizeResponse optimizeResponse = client().admin() //
                .indices() //
                .optimize(optimizeRequest) //
                .actionGet();
        
        assertEquals(0, optimizeResponse.getFailedShards());
        
    }
    
    @Test
    public void test5DeleteIndex() {
        deleteIndex();
    }

}
