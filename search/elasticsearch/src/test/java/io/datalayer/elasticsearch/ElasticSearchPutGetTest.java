package io.datalayer.elasticsearch;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This test class gathers the ElasticSearch common methods and executes them in order.
 * It will put entries in an index.
 * </p>
 * <p>
 * You can override the #useRemoteServer method (default is false):
 * <ul>
 * <li>If false, this test will kick-off an ElasticSearch Embedded Cluster (easy!).</li>
 * <li>If true, ensure you have an ElasticSearch server running on ES_CLUSTER_HOST:ES_CLUSTER_PORT with name ES_CLUSTER_NAME.</li>
 * </ul>
 * </p>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ElasticSearchPutGetTest extends ElasticSearchBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchPutGetTest.class);
    
    @Test
    public void test1PutMap() throws IOException {
        
        indexName("put-get-1");
        typeName("put-get-1");

        // --- Put
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", "eric");
        map.put("postDate", new Date());
        map.put("message", "trying out Elastic Search");
    
        LOGGER.info("JSON=" + new ObjectMapper().writeValueAsString(map));
    
        IndexResponse response = client().prepareIndex(indexName(), typeName()) //
                .setSource(map) //
                .execute() //
                .actionGet();
        
        assertEquals(indexName(), response.getIndex());
        assertEquals(typeName(), response.getType());
        assertNotNull(response.getId());
        assertEquals(1, response.getVersion());
    
        // --- Get
        
        GetResponse getResponse = client().prepareGet(indexName(), typeName(), response.getId()) //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());
        assertEquals(response.getId(), getResponse.getId());

    }

    @Test
    public void test2PutJson() throws IOException {
        
        indexName("put-get-2");
        typeName("put-get-2");

        // --- Put
        
        String json = "{" //
                + "\"user\":\"eric\"," //
                + "\"postDate\":\"2013-01-30\"," //
                + "\"message\":\"Trying out Elastic Search\"" //
                + "}";
        
        LOGGER.info("JSON=" + json);
    
        IndexResponse response = client().prepareIndex(indexName(), typeName()) //
                .setSource(json) //
                .execute() //
                .actionGet();
    
        assertEquals(indexName(), response.getIndex());
        assertEquals(typeName(), response.getType());
        assertNotNull(response.getId());
        assertEquals(1, response.getVersion());
    
        // --- Get
        
        GetResponse getResponse = client().prepareGet(indexName(), typeName(), response.getId()) //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());
        assertEquals(response.getId(), getResponse.getId());

    }

    @Test
    public void test3PutGetVersion() throws IOException {
        
        indexName("put-get-version");
        typeName("put-get-version");

        // --- Put
        
        XContentBuilder builder = jsonBuilder() //
                .startObject() //
                .field("user", "eric.1", "eric.2", "eric.3") //
                .field("postDate", new Date()) //
                .startObject("level1") //
                .startObject("level2") //
                .field("level31", "floor-3-1") //
                .field("level32", "floor-3-2") //
                .field("level33", "floor-3-3.1", "floor-3-3.2") //
                .endObject() //
                .endObject() //
                .field("message", "Trying out Elastic Search") //
                .endObject();
        LOGGER.info("XContent=" + builder.string());
    
        IndexResponse putResponse1 = client().prepareIndex(indexName(), typeName(), "1") //
                .setSource(builder) //
                .execute() //
                .actionGet();
        
        assertEquals(indexName(), putResponse1.getIndex());
        assertEquals(typeName(), putResponse1.getType());
        assertEquals("1", putResponse1.getId());
        assertEquals(1, putResponse1.getVersion());

        IndexResponse putResponse2 = client().prepareIndex(indexName(), typeName(), "1") //
                .setSource(builder) //
                .execute() //
                .actionGet();

        assertEquals(indexName(), putResponse2.getIndex());
        assertEquals(typeName(), putResponse2.getType());
        assertEquals("1", putResponse1.getId());
        assertEquals(2, putResponse2.getVersion());

        // --- Get
        
        GetResponse getResponse = client().prepareGet(indexName(), typeName(), "1") //
//              .setOperationThreaded(false) //
                .setFields("level1.level2.level33") //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());
        assertEquals("1", getResponse.getId());
        LOGGER.info("level1.level2.level33=" + getResponse.getField("level1.level2.level33").getValue());
        assertEquals(2, getResponse.getField("level1.level2.level33").getValues().size());

    }

    @Test
    public void test4PutGetArrays() throws IOException {
        
        indexName("put-get-version");
        typeName("put-get-version");

        // --- Put
        
        XContentBuilder builder = jsonBuilder() //
                .startObject() //
                .field("user", "eric.1", "eric.2", "eric.3") //
                .field("postDate", new Date()) //
                .startObject("level1") //
                .startObject("level2") //
                .field("level31", "floor-3-1") //
                .field("level32", "floor-3-2") //
                .field("level33", "floor-3-3.1", "floor-3-3.2") //
                .endObject() //
                .endObject() //
                .field("message", "Trying out Elastic Search") //
                .endObject();
        LOGGER.info("XContent=" + builder.string());
    
        IndexResponse putResponse1 = client().prepareIndex(indexName(), typeName(), "1") //
                .setSource(builder) //
                .execute() //
                .actionGet();

        assertEquals(indexName(), putResponse1.getIndex());
        assertEquals(typeName(), putResponse1.getType());
        assertEquals("1", putResponse1.getId());
        assertEquals(3, putResponse1.getVersion());

        IndexResponse putResponse2 = client().prepareIndex(indexName(), typeName(), "1") //
                .setSource(builder) //
                .execute() //
                .actionGet();

        assertEquals(indexName(), putResponse2.getIndex());
        assertEquals(typeName(), putResponse2.getType());
        assertEquals("1", putResponse1.getId());
        assertEquals(2, putResponse2.getVersion());

        // --- Get
        
        GetResponse getResponse = client().prepareGet(indexName(), typeName(), "1") //
//              .setOperationThreaded(false) //
                .setFields("level1.level2.level33") //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());
        assertEquals("1", getResponse.getId());
        LOGGER.info("level1.level2.level33=" + getResponse.getField("level1.level2.level33").getValue());
        assertEquals(2, getResponse.getField("level1.level2.level33").getValues().size());

    }

    @Test
    public void test5PutGetNested() throws IOException {
        
        indexName("put-get-nested");
        typeName("put-get-nested");

        // --- Put
        
        IndexResponse putResponse = client().prepareIndex(indexName(), typeName(), "2") //
                .setSource(nestedBuilder()) //
                .execute() //
                .actionGet();
        assertEquals("2", putResponse.getId());
    
        // --- Get
        
        GetResponse getResponse = client().prepareGet(indexName(), typeName(), "2") //
//              .setOperationThreaded(false) //
                .setFields("_source", //
                        "network", //
                        "network.twitter", //
                        "message.nested" //
                        ) //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());
        assertNotNull(getResponse.getSourceAsString());
        assertEquals("2", getResponse.getId());
        assertEquals("eric@twitter", getResponse.getField("network.twitter").getValue());
        assertNull(getResponse.getField("message.nested"));
        LOGGER.info("network=" + getResponse.getField("network").getValue());
        LOGGER.info("network.twitter=" + getResponse.getField("network.twitter").getValue());

    }

    /**
     * Nested Type
     * 
     * Nested objects/documents allow to map certain sections in the document
     * indexed as nested allowing to query them as if they are separate docs
     * joining with the parent owning doc.
     * 
     * This feature is experimental and might require reindexing the data if
     * using it.
     * 
     * One of the problems when indexing inner objects that occur several times
     * in a doc is that “cross object” search match will occur, for example:
     * 
     * <pre>
     * {
     *     "obj1" : [
     *         {
     *             "name" : "blue",
     *             "count" : 4
     *         },
     *         {
     *            "name" : "green",
     *            "count" : 6
     *        }
     *    ]
     * }
     * </pre>
     * 
     * Searching for name set to blue and count higher than 5 will match the
     * doc, because in the first element the name matches blue, and in the
     * second element, count matches “higher than 5”.
     * 
     * Nested mapping allows mapping certain inner objects (usually multi
     * instance ones), for example:
     * 
     * <pre>
     * {
     *     "type1" : {
     *         "properties" : {
     *             "obj1" : {
     *                 "type" : "nested"
     *             }
     *         }
     *     }
     * }
     * </pre>
     * 
     * The above will cause all obj1 to be indexed as a nested doc. The mapping
     * is similar in nature to setting type to object, except that it’s nested.
     * 
     * The nested object fields can also be automatically added to the immediate
     * parent by setting include_in_parent to true, and also included in the
     * root object by setting include_in_root to true.
     * 
     * Nested docs will also automatically use the root doc _all field.
     * 
     * Searching on nested docs can be done using either the nested query or
     * nested filter.
     * 
     * Internal Implementation
     * 
     * Internally, nested objects are indexed as additional documents, but,
     * since they can be guaranteed to be indexed within the same “block”, it
     * allows for extremely fast joining with parent docs.
     * 
     * Those internal nested documents are automatically masked away when doing
     * operations against the index (like searching with a match_all query), and
     * they bubble out when using the nested query.
     * 
     * @see http://www.elasticsearch.org/guide/reference/mapping/nested-type
     * 
     */
    @Test
    public void test6PutGetNestedMapping() throws IOException {
        
        indexName("put-get-nested-mapping");
        typeName("put-get-nested-mapping");
        deleteIndex();

        // --- Define index
        
        CreateIndexResponse createIndexResponse = client().admin() //
                .indices() //
                .prepareCreate(indexName()) //
                .addMapping(typeName(), resourceAsString("aos.elasticsearch.mappings-3.json")) //
                .execute() //
                .actionGet();

        assertTrue(createIndexResponse.isAcknowledged());

        // --- Put
        
        IndexResponse putResponse = client().prepareIndex(indexName(), typeName(), "2") //
                .setSource(nestedBuilder()) //
                .execute() //
                .actionGet();
        assertEquals("2", putResponse.getId());
    
        // --- Get
        
        GetResponse getResponse = client().prepareGet(indexName(), typeName(), "2") //
//              .setOperationThreaded(false) //
                .setFields("_source", //
                        "network", //
                        "network.twitter", //
                        "message.nested" //
                        ) //
                .execute() //
                .actionGet();
      
        LOGGER.info("getReponse=" + getResponse.getSourceAsString());
        assertNotNull(getResponse.getSourceAsString());
        assertEquals("2", getResponse.getId());
        assertEquals("eric@twitter", getResponse.getField("network.twitter").getValue());
        assertNull(getResponse.getField("message.nested"));
        LOGGER.info("network=" + getResponse.getField("network").getValue());
        LOGGER.info("network.twitter=" + getResponse.getField("network.twitter").getValue());

    }

    private XContentBuilder nestedBuilder() throws IOException {
        return jsonBuilder() //
            .startObject() //
            .field("user", "eric-nested") //
            .field("postDate", new Date()) //
            .field("message", "Trying out Elastic Search") //
            .startObject("network") //
                .field("twitter", "eric@twitter") //
                .field("facebook", "eric@facebook") //
            .endObject() //
            .startArray("nestedobj")
                .startObject()
                    .field("f1", "v1") //
                    .field("f2", "v2") //
                .endObject() //
                .startObject()
                    .field("f1", "v3") //
                    .field("f2", "v2") //
                .endObject() //
                .startObject()
                    .field("f4", "v4") //
                    .field("f5", "v5") //
                .endObject() //
            .endArray() //
            .field("message.nested", "Trying out Elastic Search Nested") //
            .endObject();
    }

    
}
