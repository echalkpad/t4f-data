package io.datalayer.kafka;

import java.io.IOException;
import java.util.Properties;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;

public class AosKafkaServer {

    public static void main(String... args) throws IOException {
        Properties kafkaProperties = new Properties();
        kafkaProperties.load(AosKafkaServer.class.getClassLoader().getResourceAsStream("kafka.properties"));
        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);
        KafkaServer kafkaServer = new KafkaServer(kafkaConfig, null);
        kafkaServer.startup();
        kafkaServer.shutdown();
    }

}
