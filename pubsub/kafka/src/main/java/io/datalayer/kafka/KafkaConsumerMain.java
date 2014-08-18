/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership. The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package io.datalayer.kafka;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import io.aos.console.AosConsole;

public class KafkaConsumerMain {

    public static void main(String... args) {

        String zkHost = "localhost";
        int zkPort = 2181;
        String topic = KafkaQueue.QUEUE_TEST_1.name();

        if (args.length > 0) {
            zkHost = args[0];
            zkPort = new Integer(args[1]);
            topic = args[2];
        }

        // Kafka 0.7.2
//        Properties props = new Properties();
//        props.put("zk.connect", zkHost + ":" + zkPort);
//        props.put("zk.sessiontimeout.ms", "800");
//        props.put("zk.synctime.ms", "300");
//        props.put("autocommit.interval.ms", "1000");
//        // props.put("compression.codec", "2");
//        props.put("groupid", "test-consumer");
//
//        ConsumerConfig consumerConfig = new ConsumerConfig(props);
//        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
//        // consumer.getOffsetsBefore("storm-uvid-out-staging", 0, 1357567820000,
//        // 1)
//
//        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//        topicCountMap.put(topic, new Integer(1));
//
//        Map<String, List<KafkaStream<Message>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
//        KafkaStream<Message> stream = consumerMap.get(topic).get(0);
//        ConsumerIterator<Message> it = stream.iterator();
//        while (it.hasNext()) {
//            AosConsole.println(asString(it.next().message()));
//            AosConsole.println("---");
//        }

        // Kafka 0.8
//         Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//         topicCountMap.put(topic, new Integer(1));
//         Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap =
//         consumer.createMessageStreams(topicCountMap);
//         KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
//        
//         ConsumerIterator<byte[], byte[]> it = stream.iterator();
//         while (it.hasNext()) {
//         System.out.println(getMessage(it.next().message()));
//         }

    }

    public static String asString(Message message) {
        ByteBuffer buffer = message.payload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes);
    }

}
