package io.datalayer.elasticsearch.fixture;

import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.termFilterBuilder;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.matchQueryBuilder;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;

public class AosElasticSearchFacets {

    public static FacetBuilder queryFacetBuilder = FacetBuilders.queryFacet("query_facet", matchQueryBuilder);

    public static FacetBuilder rangeFacetBuilder = FacetBuilders.rangeFacet("range_facet") //
            .field("postDate") // Field to compute on.
            .addUnboundedFrom(3) // from -infinity to 3 (excluded).
            .addRange(3, 6) // from 3 to 6 (excluded).
            .addUnboundedTo(6); // from 6 to +infinity.

    public static FacetBuilder termFacetBuilder = FacetBuilders.termsFacet("term_facet") //
            .field("user") //
//            .global(true) //
            .size(10);

    public static FacetBuilder termsFacetBuilder = FacetBuilders.termsFacet("terms_facet") //
            .fields("user", "postDate", "fakeField") //
            .size(200);

    public static FacetBuilder filteredTermsFacetBuilder = FacetBuilders.termsFacet("filtered_terms_facet") //
            .field("user") //
            .facetFilter(AosElasticSearchFilters.termFilterBuilder); // We apply a filter to the facet.

    // Don't confuse this filterFacet with the facetFilter() method...
    public static FacetBuilder filterFacetBuilder = FacetBuilders.filterFacet("filter_facet", //
            termFilterBuilder); // Your Filter here

    public static FacetBuilder histogramFacetBuilder = FacetBuilders.histogramFacet("histogram_facet") //
            .field("postDate") // Your key field.
            .keyField("postDate") // Optional...
            .valueField("time") // Optional...
//            .valueField("postDate") // Optional...
            .interval(1);
    
    // Upon "year", you can also use "quarter", "month", "week", "day", "hour" and "minute" or notation like "1.5h" or "2w".
    public static FacetBuilder dateHistogramFacetBuilder = FacetBuilders.dateHistogramFacet("date_histogram_facet") //
            .field("postDate") // Your date field.
            .keyField("postDate") // Optional...
            .valueField("time") // Optional...
//            .valueField("postDate") // Optional...
            .interval("day");
    
    public static FacetBuilder geoDistanceFacetBuilder = FacetBuilders.geoDistanceFacet("geo_distance_facet") //
            .field("location") // Field containing coordinates we want to compare with.
            .point(40, -70) // Point from where we start (0).
            .addUnboundedFrom(10) // 0 to 10 km (excluded).
            .addRange(10, 20) // 10 to 20 km (excluded).
            .addRange(20, 100) // 20 to 100 km (excluded).
            .addUnboundedTo(100) // from 100 km to infinity (and beyond ;-) ).
            .unit(DistanceUnit.KILOMETERS); // All distances are in kilometers. Can be MILES.

    public static FacetBuilder statisticalFacetBuilder = FacetBuilders.statisticalFacet("statistical_facet") //
            .field("postDate");

    public static FacetBuilder termsStatsFacetBuilder = FacetBuilders.termsStatsFacet("terms_stats_facet") //
            .keyField("user") //
            .valueField("postDate");

}
