package io.datalayer.kafka;

import java.nio.ByteBuffer;

import kafka.api.FetchRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aos.console.AosConsole;

public class KafkaConsumerSimpleMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerSimpleMain.class);

    public static void main(String... args) {

        String zkHost = "localhost";
        Integer zkPort = 2181;
        String topic = KafkaQueue.QUEUE_TEST_1.name();

        if (args.length > 0) {
            zkHost = args[0];
            zkPort = new Integer(args[1]);
            topic = args[2];
        }

        LOGGER.info("zkHost: {}, zkPort: {}, topic: {}", new Object[] { zkHost, zkPort, topic });

        SimpleConsumer consumer = new SimpleConsumer(zkHost, zkPort, 10000, 1024000, "");

        long offset = -1;

        while (true) {

//            FetchRequest fetchRequest = new FetchRequest(topic, 1, offset, 1000000);
//
//            ByteBufferMessageSet messages = consumer.fetch(fetchRequest);
//
//            for (MessageAndOffset message : messages) {
//                AosConsole.println("consumed: " + asString(message));
//                AosConsole.println("---");
//                offset = message.offset();
//            }

        }

    }

    public static String asString(MessageAndOffset message) {
        ByteBuffer buffer = message.message().payload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }

}
