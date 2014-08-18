package io.datalayer.elasticsearch.server;

import io.datalayer.elasticsearch.server.util.AosElasticSearchClient;

import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AosElasticSearchServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AosElasticSearchServer.class);

    public static void main(String[] args) throws InterruptedException {
       
        Client client = AosElasticSearchClient.client(false);
        
        while(true) {
            LOGGER.info("ElasticSearch Server is running and has a Client " + client.hashCode());
            Thread.sleep(Long.MAX_VALUE);
        }

    }

}
