package io.datalayer.elasticsearch.fixture;

import static io.datalayer.elasticsearch._base.ElasticSearchBaseTest.typeName;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchQueries.stringQueryBuilder;
import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.existsFilter;
import static org.elasticsearch.index.query.FilterBuilders.geoBoundingBoxFilter;
import static org.elasticsearch.index.query.FilterBuilders.geoDistanceFilter;
import static org.elasticsearch.index.query.FilterBuilders.geoDistanceRangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.geoPolygonFilter;
import static org.elasticsearch.index.query.FilterBuilders.geoShapeFilter;
import static org.elasticsearch.index.query.FilterBuilders.hasChildFilter;
import static org.elasticsearch.index.query.FilterBuilders.hasParentFilter;
import static org.elasticsearch.index.query.FilterBuilders.idsFilter;
import static org.elasticsearch.index.query.FilterBuilders.limitFilter;
import static org.elasticsearch.index.query.FilterBuilders.matchAllFilter;
import static org.elasticsearch.index.query.FilterBuilders.missingFilter;
import static org.elasticsearch.index.query.FilterBuilders.nestedFilter;
import static org.elasticsearch.index.query.FilterBuilders.notFilter;
import static org.elasticsearch.index.query.FilterBuilders.numericRangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;
import static org.elasticsearch.index.query.FilterBuilders.prefixFilter;
import static org.elasticsearch.index.query.FilterBuilders.queryFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.scriptFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.FilterBuilders.termsFilter;
import static org.elasticsearch.index.query.FilterBuilders.typeFilter;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilder;

public class AosElasticSearchFilters {

    public static final FilterBuilder matchAllFilterBuilder = matchAllFilter();

    public static final FilterBuilder queryFilterBuilder = queryFilter(stringQueryBuilder);

    public static final FilterBuilder missingFilterBuilder = missingFilter("user")//
            .existence(true) //
            .nullValue(true);

    public static final FilterBuilder prefixFilterBuilder = prefixFilter("user", "eric");

    public static final FilterBuilder rangeFilterBuilder = rangeFilter("time") //
            .from("0") //
            .to("5") //
            .includeLower(true) //
            .includeUpper(false);

    // A simplified form using [gte, gt, lt or lte].
    public static final FilterBuilder rangeFilterGltBuilder = rangeFilter("time") //
            .gte("5") //
            .lt("10") //
            .includeLower(true) //
            .includeUpper(false);

    public static final FilterBuilder numericRangeFilterBuilder = numericRangeFilter("time") //
            .from(10) //
            .to(20) //
            .includeLower(true) //
            .includeUpper(false);

    public static final FilterBuilder termFilterBuilder = termFilter("user", "eric");

    // Optional, can be also ["bool", "and" or "or" or "bool_nocache", "and_nocache" or "or_nocache"].
    public static final FilterBuilder termsFilterBuilder = termsFilter("user", "eric", "elasticsearch").execution("plain");

    public static final FilterBuilder nestedFilterBuilder = nestedFilter("obj1",
            boolQuery() //
              .must(matchQuery("obj1.name", "blue")) //
              .must(rangeQuery("obj1.count").gt(5)));

    public static final FilterBuilder boolFilterBuilder = boolFilter() //
            .must(termFilter("tag", "wow")) //
            .mustNot(rangeFilter("time").from("10").to("20")) //
            .should(termFilter("tag", "sometag")) //
            .should(termFilter("tag", "sometagtag"));

    public static final FilterBuilder andFilterBuilder = andFilter(rangeFilter("postDate") //
            .from("2010-03-01") //
            .to("2010-04-01"), //
//          .cache(true)
            prefixFilterBuilder);

    public static final FilterBuilder orFilterBuilder = orFilter(termFilterBuilder, //
    termFilter("name.nick", "eric"));

    public static final FilterBuilder notFilterBuilder = notFilter(rangeFilterBuilder);

    public static final FilterBuilder existsFilterBuilder = existsFilter("user");

    public static final FilterBuilder limitFilterBuilder = limitFilter(100);

    public static final FilterBuilder idsFilterBuilder = idsFilter(typeName()) //
            .addIds("1", "4", "100");
    
    public static final FilterBuilder idsFilterBuilderForType = idsFilter(typeName()).addIds("1", "4", "100");

    public static final FilterBuilder typeFilterBuilder = typeFilter(typeName());

    public static final FilterBuilder geoBoundingBoxFilterBuilder = geoBoundingBoxFilter("location") //
            .topLeft(90.0, -180.0) //
            .bottomRight(-90, 179.9999999);

    public static final FilterBuilder geoBoundingBoxFilterBuilder2 = geoBoundingBoxFilter("location") //
            .topLeft(20.1, -70.9) //
            .bottomRight(20.0, -70.8);

    public static final FilterBuilder geoDistanceFilterBuilder = geoDistanceFilter("location") //
          .point(0.0, 0.0) //
          .distance(2, DistanceUnit.KILOMETERS) //
          .optimizeBbox("memory") // Can be also "indexed" or "none".
          .geoDistance(GeoDistance.ARC); // Or GeoDistance.PLANE
  
    public static final FilterBuilder geoDistanceFilterBuilder2 = geoDistanceFilter("location") //
          .point(40.718266, -74.007819) //
          .distance(2, DistanceUnit.KILOMETERS) //
          .optimizeBbox("memory") // Can be also "indexed" or "none".
          .geoDistance(GeoDistance.ARC); // Or GeoDistance.PLANE
  
    public static final FilterBuilder geoDistanceRangeFilterBuilder = geoDistanceRangeFilter("location") //
            .point(0, 0) //
            .from("200km") //
            .to("400km") //
            .includeLower(true) //
            .includeUpper(false) //
            .optimizeBbox("memory") // Can be also "indexed" or "none".
            .geoDistance(GeoDistance.ARC); // Or GeoDistance.PLANE

    public static final FilterBuilder geoPolygonFilterBuilder = geoPolygonFilter("location") //
            .addPoint(40, -70) //
            .addPoint(30, -80) //
            .addPoint(20, -90);

    // Using pre-indexed shapes.
    public static final FilterBuilder geoShapeFilterBuilder = geoShapeFilter("location", "New Zealand", "countries",
            ShapeRelation.DISJOINT);

    // Shape within another.
//    public static final FilterBuilder geoShapeFilterBuilder2 = geoShapeFilter("location", //
//            new PolygonBuilder(), //
//            ShapeRelation.WITHIN);

    // Intersect shapes.
//    public static final FilterBuilder geoShapeFilterBuilder3 = geoShapeFilter("location", //
//            new PolygonBuilder(), //
//            ShapeRelation.INTERSECTS);

    // Has Parent.
    public static final FilterBuilder hasParentFilterBuilder = hasParentFilter("blog", termQuery("tag", "something"));

    // Has Child.
    public static final FilterBuilder hasChildFilterBuilder = hasChildFilter("blog_tag", termQuery("tag", "something"));

    public static final FilterBuilder scriptFilterBuilder = scriptFilter("doc['postDate'].value > param1") //
            .addParam("param1", 90);

}
