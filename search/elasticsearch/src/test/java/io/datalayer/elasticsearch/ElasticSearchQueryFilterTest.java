package io.datalayer.elasticsearch;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.rangeFilterBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.scriptFilterBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.matchAllQueryBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.termQueryBuilder;
import static org.junit.Assert.assertEquals;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will search with filtered queries an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchQueryFilterTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchQueryFilterTest.class);
    
    @Before
    public void before() throws IOException {
        super.before();
    }
    
    @Test
    public void testRangeFilter1() throws IOException, InterruptedException {
    
        indexName("filter-range-1");
        typeName("filter-range-1");
        int numDocs = 100;
        index(numDocs);
        
        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(matchAllQueryBuilder) //
                .setSize(numDocs) //
                .setPostFilter(rangeFilterBuilder) //
                .execute() //
                .actionGet();
        
        assertEquals(5, searchResponse.getHits().getHits().length);

        printSearchHits(searchResponse);

    }

    @Test
    public void testRangeFilter2() throws IOException {
    
        indexName("filter-range-2");
        typeName("filter-range-2");
        int numDocs = 100;
        index(numDocs);
        
        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) //
                .setQuery(termQueryBuilder) // Query
                .setPostFilter(rangeFilterBuilder) // Filter
                .setFrom(0) //
                .setSize(60) //
                .setExplain(true) //
                .execute() //
                .actionGet();
        
        printSearchHits(searchResponse);
    
    }

    @Test
    public void testScriptFilter() throws IOException {
    
        indexName("script-filter");
        typeName("script-filter");
        int numDocs = 100;
        index(numDocs);
        
        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(matchAllQueryBuilder) // Query
                .setPostFilter(scriptFilterBuilder) // Filter
                .setSize(100)
                .execute() //
                .actionGet();
        
        printSearchHits(searchResponse);
    
    }

}
