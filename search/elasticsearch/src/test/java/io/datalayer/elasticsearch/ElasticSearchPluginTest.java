package io.datalayer.elasticsearch;

import io.datalayer.elasticsearch._base.ElasticSearchBaseTest;

import java.io.IOException;

import org.junit.Test;

public class ElasticSearchPluginTest extends ElasticSearchBaseTest {
    
    @Test
    public void testPlugin() throws IOException {
        
        indexName("plugin");
        typeName("plugin");
        int numDocs = 100;
        index(numDocs);
    }

}
