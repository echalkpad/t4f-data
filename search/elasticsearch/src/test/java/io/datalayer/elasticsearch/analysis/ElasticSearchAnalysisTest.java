package io.datalayer.elasticsearch.analysis;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.matchAllQueryBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.optimize.OptimizeRequest;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.IndexMissingException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticSearchAnalysisTest extends ElasticSearchBaseTest {
    
    private static final int numDocs = 100;

    @BeforeClass
    public static void beforeClass() throws IOException {
        ElasticSearchBaseTest.beforeClass();
        indexName("analysis-1");
        typeName("analysis-1");
    }
    
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
    public void test2CreateAnalysisIndex() throws IOException {
        
        Settings indexSettings =
                ImmutableSettings.settingsBuilder()
                                 .loadFromClasspath("aos.elasticsearch.settings-analysis.json").build();
        
        CreateIndexResponse createIndexResponse = client().admin() //
                .indices() //
                .prepareCreate(indexName()) //
                .addMapping(typeName(), resourceAsString("aos.elasticsearch.mappings-analysis.json")) //
                .setSettings(indexSettings) //
                .execute() //
                .actionGet();
        
        assertTrue(createIndexResponse.isAcknowledged());

        int numDocs = 100;
        index(numDocs);

    }

    @Test
    public void test3OptimizedIndex() throws IOException {
        
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
    public void test4Index() throws IOException, InterruptedException {

        index(numDocs);

    }
    
    @Test
    public void test5MatchAll() throws IOException, InterruptedException {
    
        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(matchAllQueryBuilder) //
                .setSize(100) //
                .execute() //
                .actionGet();

        assertEquals(numDocs, searchResponse.getHits().getHits().length);

        printSearchHits(searchResponse);

    }

}
