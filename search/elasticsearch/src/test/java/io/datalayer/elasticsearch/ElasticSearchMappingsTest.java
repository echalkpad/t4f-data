package io.datalayer.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;
import java.util.Calendar;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchMappingsTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchMappingsTest.class);
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime();

    @Before
    public void before() throws IOException {
        super.before();
    }
    
    @Test
    public void testDateMappingy() throws IOException {
        
        indexName("date");
        typeName("date");
        int numDocs = 100;
        deleteIndex();

        // --- Define index
        
        CreateIndexResponse createIndexResponse = client() //
                .admin() //
                .indices() //
                .prepareCreate(indexName()) //
                .addMapping(typeName(), resourceAsString("aos.elasticsearch.mappings-date.json")) //
                .execute() //
                .actionGet();

        assertTrue(createIndexResponse.isAcknowledged());

        // --- Put
        
        XContentBuilder builder = jsonBuilder() //
                .startObject() //
                .field("name", "Somewhere...")
                .field("date", DATE_TIME_FORMATTER.print(Calendar.getInstance().getTimeInMillis()))
                .endObject();
        LOGGER.info("XContent=" + builder.string());
        
        IndexResponse putResponse = client() //
                .prepareIndex(indexName(), typeName(), "1") //
                .setSource(builder) //
                .execute() //
                .actionGet();
        assertEquals("1", putResponse.getId());
    
        client().admin().indices().prepareRefresh().execute().actionGet();

        // --- Get
        
        GetResponse getResponse = client() //
                .prepareGet(indexName(), typeName(), "1") //
                .setFields("_source") //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());

        // --- Query
        
//        SearchRequestBuilder searchRequestBuilder2 = client() //
//                .prepareSearch(indexName()) //
//                .setTypes(typeName()) //
//                .setQuery(filteredQuery(matchAllQueryBuilder, geoBoundingBoxFilterBuilder2)) //
//                .setSize(numDocs);
//        LOGGER.info(searchRequestBuilder2.toString());
//        
//        SearchResponse searchResponse2 = searchRequestBuilder2 //
//                .execute() //
//                .actionGet();
//        assertEquals(0, searchResponse2.getHits().getTotalHits());
//        printSearchHits(searchResponse2);

    }

}
