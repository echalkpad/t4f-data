package io.datalayer.elasticsearch.server.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class AosElasticSearchClient {
    private static final int NUMBER_OF_NODES = 1;
    private static Node[] nodes;

    public static Client client(boolean isLocal) {
        
        File dataPath = new File("./target/_index/es-cluster-data");
        try {
            FileUtils.deleteDirectory(dataPath);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        nodes = new Node[NUMBER_OF_NODES];
        for (int i = 0; i < nodes.length; i++) {
            final Settings nodeSettings = ImmutableSettings //
                    .settingsBuilder() //
                    .put("name", "aos-es-node-" + i)
                    .put("cluster.name", "localhost") //
                    .put("index.number_of_shards", 1) //
                    .put("path.data", dataPath.getAbsolutePath()) //
                    .build();
            nodes[i] = NodeBuilder.nodeBuilder() //
                    .settings(nodeSettings) //
                    .local(isLocal) //
                    .node();
        }
    
        if (nodes.length == 1) {
            return nodes[0].client();
        }
        else {
            return nodes[1].client();  
        }

    }
    
    public static void close() {
        if (nodes != null) {
            for (Node node: nodes) {
                node.close();
            }
        }
    }

}
