package io.datalayer.elasticsearch;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.rangeFilterBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.boolQueryBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.matchQueryBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.stringQueryBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.junit.Assert.assertEquals;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will search with compound queries an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchQueryCompoundTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchQueryCompoundTest.class);
    
    @Before
    public void before() throws IOException {
        super.before();
    }
    
    @Test
    public void testHistogram() throws IOException, InterruptedException {
    
        indexName("boolean");
        typeName("boolean");
        
        int numDocs = 100;
        index(numDocs);

        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(boolQueryBuilder) //
                .setSize(numDocs) //
                .execute() //
                .actionGet();
        
        assertEquals(numDocs, searchResponse.getHits().getHits().length);

        printSearchHits(searchResponse);

    }

    @Test
    public void testComplex() {
    
        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) //
                .setQuery(QueryBuilders.termQuery("multi", "test")) // Query
                .setPostFilter(rangeFilterBuilder) // Filter
                .setFrom(0) //
                .setSize(60) //
                .setExplain(true) //
                .execute() //
                .actionGet();
        
        printSearchHits(searchResponse);
    
    }

    @Test
    public void testPaging() {
    
        QueryBuilder qb = termQuery("multi", "test");
    
        SearchResponse searchResponse = client().prepareSearch(indexName()) //
                .setSearchType(SearchType.SCAN) //
                .setScroll(new TimeValue(60000)) //
                .setQuery(qb) //
                .setSize(100) //
                .execute() //
                .actionGet();
        
        // 100 hits per shard will be returned for each scroll - Scroll until no hits are returned
        while (true) {
            searchResponse = client().prepareSearchScroll(searchResponse.getScrollId()) //
                    .setScroll(new TimeValue(600000)) //
                    .execute() //
                    .actionGet();
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                LOGGER.info("Found hit with id=" + hit.getId());
            }
            // Break condition: No hits are returned
            if (searchResponse.getHits().hits().length == 0) {
                break;
            }
        }
    
    }

    @Test
        public void testMultiValue() throws IOException {
    
            client().prepareIndex(indexName(), typeName(), "multi-value") //
                    .setSource(jsonBuilder() //
                            .startObject() //
                            .field("user", "eric") //
                            .field("message-multivalue", //
                                    "multivalue-message-1", //
                                    "multivalue-message-2", //
                                    "multivalue-message-3") //
                            .endObject()) //
                    .execute() //
                    .actionGet();
        
            refresh();

            GetResponse response = client().prepareGet(indexName(), typeName(), "multi-value") //
                   .setFields("message-multivalue") //
                   .execute() //
                   .actionGet();
          
            assertEquals("multivalue-message-1", response.getField("message-multivalue").getValue());
            assertEquals(Lists.newArrayList("multivalue-message-1", //
                    "multivalue-message-2", //
                    "multivalue-message-3"), 
                    response.getField("message-multivalue").getValues());
            
            SearchResponse searchResponse = client().prepareSearch(indexName()) //
                    .setTypes(typeName()) //
                    .setQuery(QueryBuilders.matchQuery("message-multivalue", "multi-value-message-3")) // Query
//                    .setQuery(QueryBuilders.termQuery("message", "multi-value-message-3")) // Query
//                    .setQuery(QueryBuilders.wildcardQuery("message ", "m*")) // Query
                    .execute() //
                    .actionGet();
            
            assertEquals(1, searchResponse.getHits().getHits().length);

            printSearchHits(searchResponse);
    
        }

    @Test
    public void testMultiSearch() {
    
        SearchRequestBuilder srb1 = client().prepareSearch() //
                .setQuery(stringQueryBuilder) //
                .setSize(1);
    
        SearchRequestBuilder srb2 = client().prepareSearch() //
                .setQuery(matchQueryBuilder) //
                .setSize(1);
    
        MultiSearchResponse multiSearchResponse = client().prepareMultiSearch() //
                .add(srb1) //
                .add(srb2) //
                .execute() //
                .actionGet();
    
        long nbHits = 0;
        for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
            SearchResponse response = item.getResponse();
            if ((response != null) && (response.getHits() != null)) {
                nbHits += response.getHits().totalHits();
            }
        }
        
        LOGGER.info("Number of hits=" + nbHits);
    
    }

}
