package io.datalayer.elasticsearch._base;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch.server.util.AosElasticSearchClient;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.highlight.HighlightField;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchBaseTest.class);

    private static final boolean USE_REMOTE_SERVER = true;
    
    protected static final String ES_CLUSTER_HOST= "localhost";
    protected static final int ES_CLUSTER_PORT = 9300;
    protected static final String ES_CLUSTER_NAME = "AosElasticSearchCluster";
    
    private static String indexName = "test-index";
    private static String typeName = "test-type";
    
    protected static final int NUMBER_OF_ACTIONS = 10;
    protected static final int BATCH_SIZE = 300;

    private static Client client;

    @BeforeClass
    public static void beforeClass() throws IOException {
        deleteIndex();
    }

    @Before
    public void before() throws IOException {
//        deleteIndex();
    }

    @AfterClass
    public static void afterClass() {
        flush();
//        deleteIndex();
        client().close();
        AosElasticSearchClient.close();
    }
    
    public static String indexName() {
        return indexName;
    }

    public static void indexName(String indexName) {
        ElasticSearchBaseTest.indexName = indexName + "-index";
    }

    public static String typeName() {
        return typeName;
    }

    public static void typeName(String typeName) {
        ElasticSearchBaseTest.typeName = typeName + "-type";
    }

    protected static boolean useRemoteServer() {
        return USE_REMOTE_SERVER;
    }

    protected static Client client() {
        
        if (client == null) {
            
            if (useRemoteServer()) {
                
                Settings settings = ImmutableSettings.settingsBuilder() //
                        .put("cluster.name", ES_CLUSTER_NAME) //
                        .put("client.transport.sniff", false) //
                        .put("client.transport.ignore_cluster_name", true) //
                        .put("org.elasticsearch.client.transport", true) //
                        .build();

                client = new TransportClient(settings) //
                    .addTransportAddress(new InetSocketTransportAddress(ES_CLUSTER_HOST, ES_CLUSTER_PORT));

            }
            
            else {
                
                client = AosElasticSearchClient.client(true);
                
            }

        }
        
        return client;
        
    }
    
    protected static void flush() {
        FlushRequest flushRequest = new FlushRequest(indexName());
        FlushResponse flushResponse = client() //
                .admin() //
                .indices() //
                .flush(flushRequest) //
                .actionGet();
        assertEquals(0, flushResponse.getFailedShards());
    }
        
    protected static void refresh() {
        client().admin().indices().prepareRefresh(indexName()).execute().actionGet();
    }

    protected static void deleteIndex() {
        try {
            DeleteIndexResponse deleteIndexResponse = client().admin() //
                    .indices() //
                    .delete(new DeleteIndexRequest(indexName)) //
                    .actionGet();
            assertTrue(deleteIndexResponse.isAcknowledged());
        }
        catch (IndexMissingException e) {
            // Do nothing, the index was not present...
        }
    }
    
    protected String resourceAsString(String resourceName) {
        try {
            return new Scanner(ClassLoader.getSystemResourceAsStream(resourceName)).useDelimiter("\\Z").next();
        }
        catch (Exception e) {
            throw new RuntimeException("Exception while reading resource:" + resourceName, e);
        }
    }
    
    protected static void index(int numDocs) throws IOException {
        
        for (int i = 0; i < numDocs; i++) {
            client().prepareIndex(indexName(), typeName(), Integer.toString(i)) //
                    .setSource(jsonBuilder() //
                            .startObject() //
                            .field("user", "eric") //
                            .field("postDate", i) //
                            .field("time", i)
                            .field("url", "http://aos.io/" + i)
                            .field("uid", i)
                            .field("location") //
                                .startObject() //
                                    .field("lat", (double) i / 100D) //
                                    .field("lng", (double) i / 100D) //
                                .endObject() //
                            .endObject()) //
                    .execute() //
                    .actionGet();
        }

        refresh();

    }
    
    protected void printSearchHits(SearchResponse searchResponse) {
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            LOGGER.info("Found hit with id=" + hit.getId() + ", source=" + hit.getSourceAsString());
            final Iterator<SearchHitField> iterator = hit.iterator() ;
            while(iterator.hasNext()) {
                final SearchHitField hitField = iterator.next() ;
                LOGGER.info("   field:" + hitField.getName() + " value-count:" + hitField.getValues().size());
            }
            if (hit.getExplanation() != null) {
                LOGGER.info("Explanation=" + hit.getExplanation().getDescription());
            }
            if (hit.getHighlightFields() != null) {
                for (HighlightField h: hit.getHighlightFields().values()) {
                    if (h.getFragments() != null) {
                        for (Text t: h.getFragments()) {
                            LOGGER.info("   highlight-field=" + h.getName() + " text=" + t.string());
                        }
                    }
                }
            }
        }
    }

}
