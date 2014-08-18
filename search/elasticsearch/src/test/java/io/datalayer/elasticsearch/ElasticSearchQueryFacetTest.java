package io.datalayer.elasticsearch;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.dateHistogramFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.filterFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.filteredTermsFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.geoDistanceFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.histogramFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.queryFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.rangeFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.statisticalFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.termFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.termsFacetBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFacets.termsStatsFacetBuilder;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.filter.FilterFacet;
import org.elasticsearch.search.facet.geodistance.GeoDistanceFacet;
import org.elasticsearch.search.facet.histogram.HistogramFacet;
import org.elasticsearch.search.facet.query.QueryFacet;
import org.elasticsearch.search.facet.range.RangeFacet;
import org.elasticsearch.search.facet.statistical.StatisticalFacet;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will search with faceted queries an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
public class ElasticSearchQueryFacetTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchQueryFacetTest.class);
    
    private static SearchResponse searchResponse;
    
    @BeforeClass
    public static void beforeClass() throws IOException {
        
        ElasticSearchBaseTest.beforeClass();

        indexName("facets");
        typeName("facets");
        
        ElasticSearchBaseTest.beforeClass();
        
        String mapping = XContentFactory.jsonBuilder() //
                .startObject() //
                .startObject(typeName()) //
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

        int numDocs = 100;
        index(numDocs);

        searchResponse = client().prepareSearch(indexName()) //
                .setTypes(typeName()) //
                .setSize(100) //
                .setQuery(QueryBuilders.matchAllQuery()) //
                .addFacet(queryFacetBuilder) //
                .addFacet(filterFacetBuilder) //
                .addFacet(filteredTermsFacetBuilder) //
                .addFacet(rangeFacetBuilder) //
                .addFacet(termFacetBuilder) //
                .addFacet(termsFacetBuilder) //
                .addFacet(histogramFacetBuilder) //
                .addFacet(dateHistogramFacetBuilder) //
                .addFacet(statisticalFacetBuilder) //
                .addFacet(termsStatsFacetBuilder) //
                .addFacet(geoDistanceFacetBuilder) //
                .execute() //
                .actionGet();
        
    }

    @Test
    public void testQueryFacet() throws IOException {
        
        QueryFacet queryFacet = (QueryFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("query_facet");
        
        LOGGER.info("Count: " + queryFacet.getCount()); // Number of docs that matched.
        assertTrue(queryFacet.getCount() > 0);

    }

    @Test
    public void testFilterFacet() throws IOException {
        
        FilterFacet filterFacet = (FilterFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("filter_facet");
        
        LOGGER.info("Count: " + filterFacet.getCount()); // Number of docs that matched.
        assertTrue(filterFacet.getCount() > 0);

    }

    @Test
    public void testRangeFacet() throws IOException {
            
        RangeFacet rangeFacet = (RangeFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("range_facet");
        
        for (RangeFacet.Entry entry : rangeFacet) {
            LOGGER.info("From: " + entry.getFrom()); // Range from requested.
            LOGGER.info("To: " + entry.getTo()); // Range to requested.
            LOGGER.info("Count: " + entry.getCount()); // Doc count.
            LOGGER.info("Min: " + entry.getMin()); // Min value.
            LOGGER.info("Max: " + entry.getMax()); // Max value.
            LOGGER.info("Mean: " + entry.getMean()); // Mean.
            LOGGER.info("Total: " + entry.getTotal()); // Sum of values.
        }
    
    }

    @Test
    public void testTermFacet() throws IOException {
        
        TermsFacet termFacet = (TermsFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("term_facet");
        
        for (TermsFacet.Entry entry : termFacet) {
            LOGGER.info("Term: " + entry.getTerm()); // Term.
            LOGGER.info("Count: " + entry.getCount()); // Doc count.
        }

    }

    @Test
    public void testTermsFacet() throws IOException {
            
        TermsFacet termsFacet = (TermsFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("terms_facet");
        
        LOGGER.info("Total Count: " + termsFacet.getTotalCount()); // Total terms doc count.
        LOGGER.info("Other Count: " + termsFacet.getOtherCount()); // Not shown terms doc count.
        LOGGER.info("Missing Count: " + termsFacet.getMissingCount()); // Without term doc count.
        for (TermsFacet.Entry entry : termsFacet) {
            LOGGER.info("Term: " + entry.getTerm() + " - Count: " + entry.getCount()); // Term and Doc count.
        }

    }

    @Test
    public void testHistogramFacet() throws IOException {
            
        HistogramFacet histogramFacet = (HistogramFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("histogram_facet");
        
        for (HistogramFacet.Entry entry : histogramFacet) {
            LOGGER.info("Key: " + entry.getKey()); // Key (X-Axis)
            LOGGER.info("Max: " + entry.getMax()); // Max.
            LOGGER.info("Mean: " + entry.getMean()); // Mean.
            LOGGER.info("Min: " + entry.getMin()); // Min.
            LOGGER.info("Count: " + entry.getCount());  // Doc count (Y-Axis)
            LOGGER.info("Total: " + entry.getTotal()); 
            LOGGER.info("Total Count: " + entry.getTotalCount());
            LOGGER.info("---");
        }

    }

    @Test
    public void testDateHistogramFacet() throws IOException {
            
        DateHistogramFacet dateHistogramFacet = (DateHistogramFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("date_histogram_facet");
        
        for (DateHistogramFacet.Entry entry : dateHistogramFacet) {
            LOGGER.info("Date: " + new Date(entry.getTime())); // Date in ms since epoch (X-Axis).
            LOGGER.info("Max: " + entry.getMax()); // Max.
            LOGGER.info("Mean: " + entry.getMean()); // Mean.
            LOGGER.info("Min: " + entry.getMin()); // Min.
            LOGGER.info("Count: " + entry.getCount());  // Doc count (Y-Axis)
            LOGGER.info("Total: " + entry.getTotal()); 
            LOGGER.info("Total Count: " + entry.getTotalCount());
            LOGGER.info("---");
        }

    }

    @Test
    public void testStatisticalFacet() throws IOException {
            
        StatisticalFacet statisticalFacet = (StatisticalFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("statistical_facet");
        
        LOGGER.info("Count: " + statisticalFacet.getCount()); // Doc count
        LOGGER.info("Min: " + statisticalFacet.getMin()); // Min value
        LOGGER.info("Max: " + statisticalFacet.getMax()); // Max value
        LOGGER.info("Mean: " + statisticalFacet.getMean()); // Mean
        LOGGER.info("Total: " + statisticalFacet.getTotal()); // Sum of values
        LOGGER.info("Std Deviation: " + statisticalFacet.getStdDeviation()); // Standard Deviation
        LOGGER.info("Sum of Squares: " + statisticalFacet.getSumOfSquares()); // Sum of Squares
        LOGGER.info("Variance: " + statisticalFacet.getVariance()); // Variance

    }

    @Test
    public void testTermsStatsFacet() throws IOException {
            
        TermsStatsFacet termsStatsFacet = (TermsStatsFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("terms_stats_facet");
        
        termsStatsFacet.getMissingCount(); // Without term doc count
        
        for (TermsStatsFacet.Entry entry : termsStatsFacet) {
            LOGGER.info("Term:  " + entry.getTerm()); // Term
            LOGGER.info("Count: " + entry.getCount()); // Doc count
            LOGGER.info("Min: " + entry.getMin()); // Min value
            LOGGER.info("Max: " + entry.getMax()); // Max value
            LOGGER.info("Mean: " + entry.getMean()); // Mean
            LOGGER.info("Total: " + entry.getTotal()); // Sum of values
        }

    }

    @Test
    public void testGeoDistanceFacet() throws IOException {
            
        GeoDistanceFacet geoDistanceFacet = (GeoDistanceFacet) searchResponse.getFacets() //
                .facetsAsMap() //
                .get("geo_distance_facet");
        
        for (GeoDistanceFacet.Entry entry : geoDistanceFacet) {
            LOGGER.info("From: " + entry.getFrom()); // Distance from requested
            LOGGER.info("To: " + entry.getTo()); // Distance to requested
            LOGGER.info("Count: " + entry.getCount()); // Doc count
            LOGGER.info("Min: " + entry.getMin()); // Min value
            LOGGER.info("Max: " + entry.getMax()); // Max value
            LOGGER.info("Total: " + entry.getTotal()); // Sum of values
            LOGGER.info("Mean: " + entry.getMean()); // Mean
        }

    }

}
