package io.datalayer.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

public class KafkaProducerMain {

    public static void main(String... args) {

        String zkHost = "localhost:2181";
        if (args.length > 0) {
            zkHost = args[0];
        }

        Properties props = new Properties();
        props.put("zk.connect", zkHost);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        // props.put("serializer.class", "aos.kafka.TrackingDataSerializer");
        // props.put("partitioner.class", "aos.kafka.MemberIdPartitioner");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

//        ProducerData<String, String> data = new ProducerData<String, String>(KafkaQueue.QUEUE_TEST_1.name(),
//                "test-message-0");
//        producer.send(data);
//
//        List<String> messages = new ArrayList<String>();
//        messages.add("test-message-1");
//        messages.add("test-message-2");
//
//        ProducerData<String, String> producerData1 = new ProducerData<String, String>(KafkaQueue.QUEUE_TEST_1.name(),
//                messages);
//        ProducerData<String, String> producerData2 = new ProducerData<String, String>(KafkaQueue.QUEUE_TEST_2.name(),
//                messages);
//
//        List<ProducerData<String, String>> producerDataList = Lists.newArrayList(producerData1, producerData2);
//        producer.send(producerDataList);

        producer.close();

    }

}
