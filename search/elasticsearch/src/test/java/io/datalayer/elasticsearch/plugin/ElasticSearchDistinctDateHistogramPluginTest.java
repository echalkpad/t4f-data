package io.datalayer.elasticsearch.plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import crate.elasticsearch.facet.distinct.InternalDistinctDateHistogramFacet;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticSearchDistinctDateHistogramPluginTest extends ElasticSearchBaseTest {
    
    @Test
    public void test1Mapping() throws IOException {

        indexName("distinct-date-histogram");
        typeName("distinct-date-histogram");

        String settings = XContentFactory.jsonBuilder()
                .startObject()
                .field("number_of_shards", 2)
                .field("number_of_replicas", 0)
                .startArray("aliases").value("data").endArray()
                .endObject().string();

        String mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("distinct_data")
                .startObject("_all").field("enabled", false).endObject()
                .startObject("_source").field("enabled", false).endObject()
                .startObject("properties")
                .startObject("created_at").field("type", "date").field("store", "yes").endObject()
                .startObject("distinct").field("type", "string").field("store", "yes").endObject()
                .startObject("wrong_type").field("type", "float").field("store", "yes").endObject()
                .startObject("long_type").field("type", "long").field("store", "yes").endObject()
                .endObject()
                .endObject()
                .endObject().string();
        
        client().admin().indices().preparePutTemplate("data")
                .setTemplate("data_*")
                .setSettings(settings)
                .addMapping("data", mapping)
                .execute().actionGet();
        
    }

    @Test
    public void test2DistinctString() throws Exception {
        
        client().prepareIndex(indexName(), typeName(), "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 1000000000)
                        .field("distinct", "1")
                        .field("wrong_type", 1.1)
                        .endObject())
                .execute().actionGet();
        flush();
        XContentBuilder facetQuery = XContentFactory.contentBuilder(XContentType.JSON)
                .startObject()
                .startObject("distinct")
                .startObject("distinct_date_histogram")
                .field("field", "created_at")
                .field("value_field", "distinct")
                .field("interval", "week")
                .endObject()
                .endObject()
                .endObject();
        SearchResponse response = client().prepareSearch(indexName())
                .setSearchType(SearchType.COUNT)
                .setFacets(facetQuery.bytes().array())
                .execute().actionGet();
        InternalDistinctDateHistogramFacet facet = (InternalDistinctDateHistogramFacet) response.getFacets().facet("distinct");
        XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON).startObject();
        facet.toXContent(builder, ToXContent.EMPTY_PARAMS).endObject();
        assertThat(builder.string(), equalTo(
                "{\"distinct\":{" +
                        "\"_type\":\"distinct_date_histogram\",\"entries\":[" +
                        "{\"time\":950400000,\"count\":1}" +
                        "]," +
                        "\"count\":1" +
                        "}" +
                        "}"));

        client().prepareIndex("distinct_data_1", "data", "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 1000000000)
                        .field("distinct", "2")
                        .field("wrong_type", 1.1)
                        .endObject())
                .execute().actionGet();
        flush();
        response = client().prepareSearch(indexName())
                .setSearchType(SearchType.COUNT)
                .setFacets(facetQuery.bytes().array())
                .execute().actionGet();
        facet = (InternalDistinctDateHistogramFacet) response.getFacets().facet("distinct");
        builder = XContentFactory.contentBuilder(XContentType.JSON).startObject();
        facet.toXContent(builder, ToXContent.EMPTY_PARAMS).endObject();
        assertThat(builder.string(), equalTo(
                "{\"distinct\":{" +
                        "\"_type\":\"distinct_date_histogram\",\"entries\":[" +
                        "{\"time\":950400000,\"count\":2}" +
                        "]," +
                        "\"count\":2" +
                        "}" +
                        "}"));

        client().prepareIndex("distinct_data_1", "data", "3")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 2000000000)
                        .startArray("distinct")
                        .value("2")
                        .value("3")
                        .endArray()
                        .field("wrong_type", 1.1)
                        .endObject())
                .execute().actionGet();
        client().prepareIndex("distinct_data_1", "data", "4")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 2000000000)
                        .startArray("distinct")
                        .value("3")
                        .value("4")
                        .endArray()
                        .field("wrong_type", 1.1)
                        .endObject())
                .execute().actionGet();
        flush();
        response = client().prepareSearch(indexName())
                .setSearchType(SearchType.COUNT)
                .setFacets(facetQuery.bytes().array())
                .execute().actionGet();
        facet = (InternalDistinctDateHistogramFacet) response.getFacets().facet("distinct");
        builder = XContentFactory.contentBuilder(XContentType.JSON).startObject();
        facet.toXContent(builder, ToXContent.EMPTY_PARAMS).endObject();
        assertThat(builder.string(), equalTo(
                "{\"distinct\":{" +
                        "\"_type\":\"distinct_date_histogram\",\"entries\":[" +
                        "{\"time\":950400000,\"count\":2}," +
                        "{\"time\":1555200000,\"count\":3}" +
                        "]," +
                        "\"count\":4" +
                        "}" +
                        "}"));
    }

    @Test
    public void test3DistinctLong() throws Exception {
        
        client().prepareIndex("distinct_data_1", "data", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 1000000000)
                        .field("long_type", 1)
                        .endObject())
                .execute().actionGet();
        flush();
        XContentBuilder facetQuery = XContentFactory.contentBuilder(XContentType.JSON)
                .startObject()
                .startObject("distinct")
                .startObject("distinct_date_histogram")
                .field("field", "created_at")
                .field("value_field", "long_type")
                .field("interval", "week")
                .endObject()
                .endObject()
                .endObject();
        SearchResponse response = client().prepareSearch(indexName())
                .setSearchType(SearchType.COUNT)
                .setFacets(facetQuery.bytes().array())
                .execute().actionGet();
        InternalDistinctDateHistogramFacet facet = (InternalDistinctDateHistogramFacet) response.getFacets().facet("distinct");
        XContentBuilder builder = XContentFactory.contentBuilder(XContentType.JSON).startObject();
        facet.toXContent(builder, ToXContent.EMPTY_PARAMS).endObject();
        assertThat(builder.string(), equalTo(
                "{\"distinct\":{" +
                        "\"_type\":\"distinct_date_histogram\",\"entries\":[" +
                        "{\"time\":950400000,\"count\":1}" +
                        "]," +
                        "\"count\":1" +
                        "}" +
                        "}"));

        client().prepareIndex("distinct_data_1", "data", "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 1000000000)
                        .field("long_type", 2)
                        .endObject())
                .execute().actionGet();
        flush();
        response = client().prepareSearch(indexName())
                .setSearchType(SearchType.COUNT)
                .setFacets(facetQuery.bytes().array())
                .execute().actionGet();
        facet = (InternalDistinctDateHistogramFacet) response.getFacets().facet("distinct");
        builder = XContentFactory.contentBuilder(XContentType.JSON).startObject();
        facet.toXContent(builder, ToXContent.EMPTY_PARAMS).endObject();
        assertThat(builder.string(), equalTo(
                "{\"distinct\":{" +
                        "\"_type\":\"distinct_date_histogram\",\"entries\":[" +
                        "{\"time\":950400000,\"count\":2}" +
                        "]," +
                        "\"count\":2" +
                        "}" +
                        "}"));

        client().prepareIndex("distinct_data_1", "data", "3")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 2000000000)
                        .startArray("long_type")
                        .value(2)
                        .value(3)
                        .endArray()
                        .endObject())
                .execute().actionGet();
        client().prepareIndex("distinct_data_1", "data", "4")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("created_at", 2000000000)
                        .startArray("long_type")
                        .value(3)
                        .value(4)
                        .endArray()
                        .endObject())
                .execute().actionGet();
        flush();
        response = client().prepareSearch(indexName())
                .setSearchType(SearchType.COUNT)
                .setFacets(facetQuery.bytes().array())
                .execute().actionGet();
        facet = (InternalDistinctDateHistogramFacet) response.getFacets().facet("distinct");
        builder = XContentFactory.contentBuilder(XContentType.JSON).startObject();
        facet.toXContent(builder, ToXContent.EMPTY_PARAMS).endObject();
        assertThat(builder.string(), equalTo(
                "{\"distinct\":{" +
                        "\"_type\":\"distinct_date_histogram\",\"entries\":[" +
                        "{\"time\":950400000,\"count\":2}," +
                        "{\"time\":1555200000,\"count\":3}" +
                        "]," +
                        "\"count\":4" +
                        "}" +
                        "}"));
    }

}
