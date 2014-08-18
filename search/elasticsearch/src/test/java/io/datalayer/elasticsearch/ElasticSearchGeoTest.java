package io.datalayer.elasticsearch;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.geoBoundingBoxFilterBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.geoBoundingBoxFilterBuilder2;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.geoDistanceFilterBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.geoDistanceFilterBuilder2;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.matchAllQueryBuilder;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.FilterBuilders.geoBoundingBoxFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchGeoTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchGeoTest.class);
    
    @Before
    public void before() throws IOException {
        super.before();
    }
    
    @Test
    public void testGeoQuery() throws IOException {
        
        indexName("geo");
        typeName("geo");
        int numDocs = 100;
        deleteIndex();

        // --- Define index
        
        CreateIndexResponse createIndexResponse = client() //
                .admin() //
                .indices() //
                .prepareCreate(indexName()) //
                .addMapping(typeName(), resourceAsString("aos.elasticsearch.mappings-geo.json")) //
                .execute() //
                .actionGet();

        assertTrue(createIndexResponse.isAcknowledged());

        // --- Put
        
        XContentBuilder builder = jsonBuilder() //
                .startObject() //
                .field("name", "Somewhere...")
                .startObject("location") //
//                .field("lat", 40.718266) //
//                .field("lon", -74.007819) //
                .field("lat", 0.0) //
                .field("lon", 0.0) //
                .endObject() //
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

        // --- Query geoDistanceFilterBuilder
        
        SearchRequestBuilder searchRequestBuilder1 = client() //
                .prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(filteredQuery(matchAllQueryBuilder, geoBoundingBoxFilterBuilder)) //
                .setSize(numDocs);
        LOGGER.info(searchRequestBuilder1.toString());
        
        SearchResponse searchResponse1 = searchRequestBuilder1 //
                .execute() //
                .actionGet();
        assertEquals(1, searchResponse1.getHits().getTotalHits());
        printSearchHits(searchResponse1);

        // --- Query geoDistanceFilterBuilder2
        
        SearchRequestBuilder searchRequestBuilder2 = client() //
                .prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(filteredQuery(matchAllQueryBuilder, geoBoundingBoxFilterBuilder2)) //
                .setSize(numDocs);
        LOGGER.info(searchRequestBuilder2.toString());
        
        SearchResponse searchResponse2 = searchRequestBuilder2 //
                .execute() //
                .actionGet();
        assertEquals(0, searchResponse2.getHits().getTotalHits());
        printSearchHits(searchResponse2);

        // --- Query geoBoundingBoxFilterBuilder
        
        SearchRequestBuilder searchRequestBuilder3 = client() //
                .prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(filteredQuery(matchAllQueryBuilder, geoDistanceFilterBuilder)) //
                .setSize(numDocs);
        LOGGER.info(searchRequestBuilder3.toString());
        
        SearchResponse searchResponse3 = searchRequestBuilder3 //
                .execute() //
                .actionGet();
        assertEquals(1, searchResponse3.getHits().getTotalHits());
        printSearchHits(searchResponse3);

        // --- Query geoBoundingBoxFilterBuilder2
        
        SearchRequestBuilder searchRequestBuilder4 = client() //
                .prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setQuery(filteredQuery(matchAllQueryBuilder, geoDistanceFilterBuilder2)) //
                .setSize(numDocs);
        LOGGER.info(searchRequestBuilder4.toString());
        
        SearchResponse searchResponse4 = searchRequestBuilder4 //
                .execute() //
                .actionGet();
        assertEquals(0, searchResponse4.getHits().getTotalHits());
        printSearchHits(searchResponse4);

    }
    
    @Test
    public void simpleBoundingBoxTest() throws Exception {
        try {
            client().admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
            // ignore
        }
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties").startObject("location").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();
        client().admin().indices().prepareCreate("test").addMapping("type1", mapping).execute().actionGet();
//        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .field("name", "New York")
                .startObject("location").field("lat", 40.7143528).field("lon", -74.0059731).endObject()
                .endObject()).execute().actionGet();

        // to NY: 5.286 km
        client().prepareIndex("test", "type1", "2").setSource(jsonBuilder().startObject()
                .field("name", "Times Square")
                .startObject("location").field("lat", 40.759011).field("lon", -73.9844722).endObject()
                .endObject()).execute().actionGet();

        // to NY: 0.4621 km
        client().prepareIndex("test", "type1", "3").setSource(jsonBuilder().startObject()
                .field("name", "Tribeca")
                .startObject("location").field("lat", 40.718266).field("lon", -74.007819).endObject()
                .endObject()).execute().actionGet();

        // to NY: 1.055 km
        client().prepareIndex("test", "type1", "4").setSource(jsonBuilder().startObject()
                .field("name", "Wall Street")
                .startObject("location").field("lat", 40.7051157).field("lon", -74.0088305).endObject()
                .endObject()).execute().actionGet();

        // to NY: 1.258 km
        client().prepareIndex("test", "type1", "5").setSource(jsonBuilder().startObject()
                .field("name", "Soho")
                .startObject("location").field("lat", 40.7247222).field("lon", -74).endObject()
                .endObject()).execute().actionGet();

        // to NY: 2.029 km
        client().prepareIndex("test", "type1", "6").setSource(jsonBuilder().startObject()
                .field("name", "Greenwich Village")
                .startObject("location").field("lat", 40.731033).field("lon", -73.9962255).endObject()
                .endObject()).execute().actionGet();

        // to NY: 8.572 km
        client().prepareIndex("test", "type1", "7").setSource(jsonBuilder().startObject()
                .field("name", "Brooklyn")
                .startObject("location").field("lat", 40.65).field("lon", -73.95).endObject()
                .endObject()).execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SearchRequestBuilder searchRequestBuilder = client().prepareSearch() // from NY
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(40.73, -74.1).bottomRight(40.717, -73.99)));
        
        LOGGER.info(searchRequestBuilder.toString());
        
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(2l));
        assertThat(searchResponse.getHits().hits().length, equalTo(2));
        for (SearchHit hit : searchResponse.getHits()) {
            LOGGER.info(hit.getId());
            assertThat(hit.id(), anyOf(equalTo("1"), equalTo("3"), equalTo("5")));
        }

        searchResponse = client().prepareSearch() // from NY
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(40.73, -74.1).bottomRight(40.717, -73.99).type("indexed")))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(3l));
        assertThat(searchResponse.getHits().hits().length, equalTo(3));
        for (SearchHit hit : searchResponse.getHits()) {
            LOGGER.info(hit.getId());
            assertThat(hit.id(), anyOf(equalTo("1"), equalTo("3"), equalTo("5")));
        }

    }

    @Test
    public void limitsBoundingBoxTest() throws Exception {
        try {
            client().admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
            // ignore
        }
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties").startObject("location").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();
        client().admin().indices().prepareCreate("test").addMapping("type1", mapping).setSettings(settingsBuilder().put("index.number_of_shards", "1")).execute().actionGet();
//        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 40).field("lon", -20).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "2").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 40).field("lon", -10).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "3").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 40).field("lon", 10).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "4").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 40).field("lon", 20).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "5").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 10).field("lon", -170).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "6").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 0).field("lon", -170).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "7").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", -10).field("lon", -170).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "8").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 10).field("lon", 170).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "9").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", 0).field("lon", 170).endObject()
                .endObject()).execute().actionGet();

        client().prepareIndex("test", "type1", "10").setSource(jsonBuilder().startObject()
                .startObject("location").field("lat", -10).field("lon", 170).endObject()
                .endObject()).execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(41, -11).bottomRight(40, 9)))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("2"));
        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(41, -11).bottomRight(40, 9).type("indexed")))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("2"));

        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(41, -9).bottomRight(40, 11)))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("3"));
        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(41, -9).bottomRight(40, 11).type("indexed")))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("3"));

        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(11, 171).bottomRight(1, -169)))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("5"));
        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(11, 171).bottomRight(1, -169).type("indexed")))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("5"));

        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(9, 169).bottomRight(-1, -171)))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("9"));
        searchResponse = client().prepareSearch()
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(9, 169).bottomRight(-1, -171).type("indexed")))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(1l));
        assertThat(searchResponse.getHits().hits().length, equalTo(1));
        assertThat(searchResponse.getHits().getAt(0).id(), equalTo("9"));
    }

    @Test
    public void limit2BoundingBoxTest() throws Exception {
        try {
            client().admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
            // ignore
        }
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties").startObject("location").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();
        client().admin().indices().prepareCreate("test").addMapping("type1", mapping).setSettings(settingsBuilder().put("index.number_of_shards", "1")).execute().actionGet();
//        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .field("userid", 880)
                .field("title", "Place in Stockholm")
                .startObject("location").field("lat", 59.328355000000002).field("lon", 18.036842).endObject()
                .endObject())
                .setRefresh(true)
                .execute().actionGet();

        client().prepareIndex("test", "type1", "2").setSource(jsonBuilder().startObject()
                .field("userid", 534)
                .field("title", "Place in Montreal")
                .startObject("location").field("lat", 45.509526999999999).field("lon", -73.570986000000005).endObject()
                .endObject())
                .setRefresh(true)
                .execute().actionGet();

        SearchResponse searchResponse = client().prepareSearch()
                .setQuery(
                        filteredQuery(termQuery("userid", 880),
                                geoBoundingBoxFilter("location").topLeft(74.579421999999994, 143.5).bottomRight(-66.668903999999998, 113.96875))
                ).execute().actionGet();
        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));
        searchResponse = client().prepareSearch()
                .setQuery(
                        filteredQuery(termQuery("userid", 880),
                                geoBoundingBoxFilter("location").topLeft(74.579421999999994, 143.5).bottomRight(-66.668903999999998, 113.96875).type("indexed"))
                ).execute().actionGet();
        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));

        searchResponse = client().prepareSearch()
                .setQuery(
                        filteredQuery(termQuery("userid", 534),
                                geoBoundingBoxFilter("location").topLeft(74.579421999999994, 143.5).bottomRight(-66.668903999999998, 113.96875))
                ).execute().actionGet();
        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));
        searchResponse = client().prepareSearch()
                .setQuery(
                        filteredQuery(termQuery("userid", 534),
                                geoBoundingBoxFilter("location").topLeft(74.579421999999994, 143.5).bottomRight(-66.668903999999998, 113.96875).type("indexed"))
                ).execute().actionGet();
        assertThat(searchResponse.getHits().totalHits(), equalTo(1l));
        
    }
    
    public void testNearest() throws IOException {
        
        try {
            client().admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
            // ignore
        }
        String mapping = XContentFactory.jsonBuilder().startObject().startObject("type1")
                .startObject("properties").startObject("location").field("type", "geo_point").field("lat_lon", true).endObject().endObject()
                .endObject().endObject().string();
        client().admin().indices().prepareCreate("test").addMapping("type1", mapping).execute().actionGet();
//        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource(jsonBuilder().startObject()
                .field("name", "New York")
                .startObject("location").field("lat", 40.7143528).field("lon", -74.0059731).endObject()
                .endObject()).execute().actionGet();

        // to NY: 5.286 km
        client().prepareIndex("test", "type1", "2").setSource(jsonBuilder().startObject()
                .field("name", "Times Square")
                .startObject("location").field("lat", 40.759011).field("lon", -73.9844722).endObject()
                .endObject()).execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        SortBuilder sortBuilder = SortBuilders.geoDistanceSort("location");
        
        SearchRequestBuilder searchRequestBuilder = client().prepareSearch() // from NY
                .setQuery(matchAllQuery())
                .addSort(sortBuilder);
        
        LOGGER.info(searchRequestBuilder.toString());
        
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(2l));
        assertThat(searchResponse.getHits().hits().length, equalTo(2));
        for (SearchHit hit : searchResponse.getHits()) {
            LOGGER.info(hit.getId());
            assertThat(hit.id(), anyOf(equalTo("1"), equalTo("3"), equalTo("5")));
        }

        searchResponse = client().prepareSearch() // from NY
                .setQuery(filteredQuery(matchAllQuery(), geoBoundingBoxFilter("location").topLeft(40.73, -74.1).bottomRight(40.717, -73.99).type("indexed")))
                .execute().actionGet();
        assertThat(searchResponse.getHits().getTotalHits(), equalTo(3l));
        assertThat(searchResponse.getHits().hits().length, equalTo(3));
        for (SearchHit hit : searchResponse.getHits()) {
            LOGGER.info(hit.getId());
            assertThat(hit.id(), anyOf(equalTo("1"), equalTo("3"), equalTo("5")));
        }

    }

}
