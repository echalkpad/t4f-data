/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
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
package aos.storm.topology.storm;

import io.aos.storm.bolt.ExclamationBolt;
import io.aos.storm.bolt.SplitSentenceBolt;
import io.aos.storm.bolt.WordCountBolt;
import io.aos.storm.spout.RandomSentenceSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class WordCountTopology {
    public static final boolean RUN_LOCALLY = true;

    public static void main(String... args) throws AlreadyAliveException, InvalidTopologyException {

        String topologyName = "word-count";

        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("sentence-spout", new RandomSentenceSpout());
        topologyBuilder.setBolt("exclaim-1", new ExclamationBolt()).shuffleGrouping("sentence-spout");
        topologyBuilder.setBolt("exclaim-2", new ExclamationBolt(), 5).shuffleGrouping("sentence-spout")
                .shuffleGrouping("exclaim-1");
        topologyBuilder.setBolt("sentence-split", new SplitSentenceBolt(), 10).shuffleGrouping("exclaim-2");

        topologyBuilder.setBolt("word-count", new WordCountBolt(), 20).fieldsGrouping("sentence-split",
                new Fields("word"));

        // topologyBuilder.setBolt(4, new MyBolt(),
        // 12).shuffleGrouping(1).shuffleGrouping(2)
        // .fieldsGrouping(3, new Fields("id1", "id2"));

        // topologyBuilder.setBolt(4, new MyBolt(),
        // 12).shuffleGrouping(1).shuffleGrouping(2)
        // .fieldsGrouping(3, new Fields("id1", "id2"));

        StormTopology stormTopology = topologyBuilder.createTopology();

        Config topologyConf = new Config();
        topologyConf.setMaxSpoutPending(50);
        if (RUN_LOCALLY) {
            topologyConf.setDebug(false);
            topologyConf.setNumWorkers(2);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(topologyName, topologyConf, stormTopology);
            Utils.sleep(10000);
            cluster.killTopology(topologyName);
            cluster.shutdown();
        } else {
            topologyConf.setNumWorkers(1);
            StormSubmitter.submitTopology(topologyName, topologyConf, stormTopology);
        }

    }

}
