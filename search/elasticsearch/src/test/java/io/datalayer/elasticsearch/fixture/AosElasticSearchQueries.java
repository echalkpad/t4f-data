package io.datalayer.elasticsearch.fixture;

import static io.datalayer.elasticsearch._base.ElasticSearchBaseTest.indexName;
import static io.datalayer.elasticsearch.fixture.AosElasticSearchFilters.termFilterBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.boostingQuery;
import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.disMaxQuery;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyLikeThisFieldQuery;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyLikeThisQuery;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoShapeQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasChildQuery;
import static org.elasticsearch.index.query.QueryBuilders.hasParentQuery;
import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.indicesQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.moreLikeThisFieldQuery;
import static org.elasticsearch.index.query.QueryBuilders.moreLikeThisQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.spanFirstQuery;
import static org.elasticsearch.index.query.QueryBuilders.spanNearQuery;
import static org.elasticsearch.index.query.QueryBuilders.spanNotQuery;
import static org.elasticsearch.index.query.QueryBuilders.spanOrQuery;
import static org.elasticsearch.index.query.QueryBuilders.spanTermQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.index.query.QueryBuilders.topChildrenQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
 * 
 * Query DSL
 * =========
 * 
 * elasticsearch provides a full Query DSL based on JSON to define queries. In
 * general, there are basic queries such as term or prefix. There are also
 * compound queries like the bool query. Queries can also have filters
 * associated with them such as the filtered or constant_score queries, with
 * specific filter queries.
 * 
 * Think of the Query DSL as an AST of queries. Certain queries can contain
 * other queries (like the bool query), other can contain filters (like the
 * constant_score), and some can contain both a query and a filter (like the
 * filtered). Each of those can contain any query of the list of queries or any
 * filter from the list of filters, resulting in the ability to build quite
 * complex (and interesting) queries.
 * 
 * Both queries and filters can be used in different APIs. For example, within a
 * search query, or as a facet filter. This section explains the components
 * (queries and filters) that can form the AST one can use.
 * 
 * Filters are very handy since they perform an order of magnitude better than
 * plain queries since no scoring is performed and they are automatically
 * cached. Filters and Caching
 * 
 * Filters can be a great candidate for caching. Caching the result of a filter
 * does not require a lot of memory, and will cause other queries executing
 * against the same filter (same parameters) to be blazingly fast.
 * 
 * Some filters already produce a result that is easily cacheable, and the
 * difference between caching and not caching them is the act of placing the
 * result in the cache or not. These filters, which include the term, terms,
 * prefix, and range filters, are by default cached and are recommended to use
 * (compared to the equivalent query version) when the same filter (same
 * parameters) will be used across multiple different queries (for example, a
 * range filter with age higher than 10).
 * 
 * Other filters, usually already working with the field data loaded into
 * memory, are not cached by default. Those filters are already very fast, and
 * the process of caching them requires extra processing in order to allow the
 * filter result to be used with different queries than the one executed. These
 * filters, including the geo, numeric_range, and script filters are not cached
 * by default.
 * 
 * The last type of filters are those working with other filters. The and, not
 * and or filters are not cached as they basically just manipulate the internal
 * filters.
 * 
 * All filters allow to set _cache element on them to explicitly control
 * caching. They also allow to set _cache_key which will be used as the caching
 * key for that filter. This can be handy when using very large filters (like a
 * terms filter with many elements in it).
 * 
 * @see http://www.elasticsearch.org/guide/reference/query-dsl
 * 
 */
public class AosElasticSearchQueries {

    public static final QueryBuilder matchAllQueryBuilder = matchAllQuery();

    public static final QueryBuilder stringQueryBuilder = queryString("+eric -elasticsearch");

    public static final QueryBuilder stringQueryBuilderForField = queryString("+eric -dadoonet") //
            .field("user");

//    Remove the `field` and `text` queries.
//      The `text` query was replaced by the `match` query and has been
//      deprecated for quite a while.
//      The `field` query should be replaced by a `query_string` query with
//      the `default_field` specified.
//    Fixes #4033
//    public static final QueryBuilder fieldQueryBuilder = fieldQuery("user", "+eric -dadoonet");

    public static final QueryBuilder termQueryBuilder = termQuery("user", "eric");

    public static final QueryBuilder termsQueryBuilder = termsQuery("user", // field
            "eric", "ingrid") // values
            .minimumMatch(1); // How many terms must match

    public static final QueryBuilder prefixQueryBuilder = prefixQuery("brand", "heine");

    public static final QueryBuilder matchQueryBuilder = matchQuery("user", "eric elasticsearch");

    // Text you are looking for.
    public static final QueryBuilder multiMatchQueryBuilder = multiMatchQuery("eric elasticsearch", //
            "user", "message" // Fields you query on.
    );

    public static final QueryBuilder rangeQueryBuilder = rangeQuery("price") //
            .from(5) //
            .to(10) //
            .includeLower(true) //
            .includeUpper(false);

    public static final QueryBuilder idsQueryBuilder = idsQuery().ids("1", "2");

    public static final QueryBuilder wildcardQueryBuilder = wildcardQuery("user", "k?mc*");

    public static final QueryBuilder boolQueryBuilder = boolQuery() //
            .must(termQuery("content", "test1")) //
            .must(termQuery("content", "test4")) //
            .mustNot(termQuery("content", "test2")) //
            .should(termQuery("content", "test3"));

    public static final QueryBuilder nestedQueryBuilder = nestedQuery("obj1", // Path
            boolQuery() //
                    .must(matchQuery("obj1.name", "blue")) //
                    .must(rangeQuery("obj1.count").gt(5))) //
            .scoreMode("avg"); // max, total, avg or none

    public static final QueryBuilder boostingQueryBuilder = boostingQuery() //
            .positive(termQuery("user", "eric")) //
            .negative(termQuery("user", "dadoonet")) //
            .negativeBoost(0.2F);

    // Equivalent to Lucene FilteredQuery
    public static final QueryBuilder filteredQueryBuilder = filteredQuery(termQueryBuilder, termFilterBuilder);

    // Equivalent to Lucene ConstantScoreQuery
    public static final QueryBuilder queriedConstantScoreQuery = constantScoreQuery(termQueryBuilder);

    // Equivalent to Lucene ConstantScoreQuery
    public static final QueryBuilder filteredConstantScoreQuery = constantScoreQuery(termFilterBuilder);

   // Your query here.
//    public static final QueryBuilder customScoreQueryBuilder = customScoreQuery(matchAllQueryBuilder)
//            .script("_score * doc['price'].value"); // Your script here.

    // If the script have parameters, use the same script and provide parameters to it.
//    public static final QueryBuilder customScoreQueryBuidler2 = customScoreQuery(matchAllQueryBuilder)
//            .script("_score * doc['price'].value / pow(param1, param2)") //
//            .param("param1", 2) //
//            .param("param2", 3.1);

//    public static final QueryBuilder customBoostFactorQueryBuilder = customBoostFactorQuery(
//            matchAllQueryBuilder) // Your query.
//            .boostFactor(3.1F);

    public static final QueryBuilder constantScoreQueryWithQueryBuilder = constantScoreQuery(termQueryBuilder) //
            .boost(2.0F);

    public static final QueryBuilder constantScoreQueryWithFilter = constantScoreQuery(termFilterBuilder) //
            .boost(2.0F);

    public static final QueryBuilder disMaxQueryBuilder = disMaxQuery() //
            .add(termQueryBuilder) // Your queries.
//            .add(fieldQueryBuilder) // Your  queries.
            .boost(1.2F) //
            .tieBreaker(0.7F);

    public static final QueryBuilder fuzzyLikeThisQueryBuilder = fuzzyLikeThisQuery("name.first", "name.last") // Fields
            .likeText("text like this one") // Text
            .maxQueryTerms(12); // Max num of Terms in generated queries

    // flt_field Query - // Only on single field
    public static final QueryBuilder fuzzyLikeThisFieldQueryBuilder = fuzzyLikeThisFieldQuery("name.first") 
            .likeText("text like this one") //
            .maxQueryTerms(12);

    public static final QueryBuilder fuzzyQueryBuilder = fuzzyQuery("user", "kimzhy");

    // mlt Query.
    public static final QueryBuilder moreLikeThisQueryBuilder = moreLikeThisQuery("eric", "charles") // Fields.
            .likeText("text like this one") // Text.
            .minTermFreq(1) // Ignore Threshold.
            .maxQueryTerms(12); // Max num of Terms in generated queries.

    // mlt_field Query - Only on single field.
    public static final QueryBuilder moreLikeThisFieldQueryBuilder = moreLikeThisFieldQuery("eric") 
            .likeText("text like this one") //
            .minTermFreq(1) //
            .maxQueryTerms(12);

    public static final QueryBuilder spanFirstQueryBuilder = spanFirstQuery( //
            spanTermQuery("user", "eric"), // Query
            3 // Max End position
            );

    public static final QueryBuilder spanNearQueryBuilder = spanNearQuery().clause( //
            spanTermQuery("field", "value1"))
            // Span Term Queries
            .clause(spanTermQuery("field", "value2"))
            .clause(spanTermQuery("field", "value3")) //
            .slop(12) // Slop factor
            .inOrder(false) //
            .collectPayloads(false);

    // Span Not
    public static final QueryBuilder spanNotQueryBuilder = spanNotQuery() //
            .include(spanTermQuery("field", "value1")) //
            .exclude(spanTermQuery("field", "value2"));

    // Span Term
    public static final QueryBuilder spanTermQueryBuilder = spanTermQuery("user", "eric");

    // Span Or
    public static final QueryBuilder spanOrQueryBuilder = spanOrQuery() //
            .clause((SpanTermQueryBuilder) spanTermQueryBuilder) //
            .clause(spanTermQuery("field", "value2")) //
            .clause(spanTermQuery("field", "value3"));

//    public static final QueryBuilder customFiltersScoreQueryBuilder = QueryBuilders
//            .customFiltersScoreQuery(matchAllQueryBuilder) //
//            // Query Filters with their boost factors
//            .add(FilterBuilders.rangeFilter("age").from(0).to(10), 3) //
//            .add(FilterBuilders.rangeFilter("age").from(10).to(20), 2) //
//            .scoreMode("first"); // first, min, max, total, avg or multiply.

    // Using another query when no match for the main one.
    public static final QueryBuilder indicesQueryBuilder = indicesQuery(termQueryBuilder, indexName()) //
            .noMatchQuery(termQuery("tag", "kow"));

    // Using all (match all) or none (match no documents).
    public static final QueryBuilder indicesQueryBuidler2 = indicesQuery(termQueryBuilder, indexName()) //
            .noMatchQuery("all"); // all or none.

    // Has Parent.
    public static final QueryBuilder hasParentQueryBuilder = hasParentQuery("blog", termQueryBuilder);

    // Has Child.
    public static final QueryBuilder hasChildQueryBuilder = hasChildQuery("blog_tag", termQueryBuilder);

    public static final QueryBuilder topChildrenQueryBuilder = topChildrenQuery("blog_tag", // field.
            (TermQueryBuilder) termQueryBuilder // Query.
            )//
            .score("max") // max, sum or avg.
            .factor(5) //
            .incrementalFactor(2);

    // Shape within another.
//    public static final QueryBuilder geoShapeQueryBuilder = geoShapeQuery("location", //
//            new PolygonBuilder());

    // Using pre-indexed shapes.
    public static final QueryBuilder geoShapeQueryBuilder = geoShapeQuery("location", //
            "New Zealand", //   
            "countries");

}
