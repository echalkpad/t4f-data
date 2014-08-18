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
package aos.storm.topology.trident;


import org.apache.thrift7.TException;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.testing.MemoryMapState;
import io.aos.storm.function.SplitFunction;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class TridentWordCount3 {
    public static final boolean RUN_LOCALLY = true;

    public static void main(String... args) throws AlreadyAliveException, InvalidTopologyException, TException,
            DRPCExecutionException {

        String topologyName = "trident-word-count";

        FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 100, //
                new Values("the cow jumped over the moon"), //
                new Values("the man went to the store and bought some candy"), //
                new Values("four score and seven years ago"), //
                new Values("how many apples can you eat"));
        spout.setCycle(true);

        TridentTopology tridentTopology = new TridentTopology();
        TridentState wordCounts = tridentTopology.newStream("spout1", spout) //
                .each(new Fields("sentence"), new SplitFunction(), new Fields("word")) //
                .groupBy(new Fields("word")) //
                .persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count")) //
                .parallelismHint(6);

        StormTopology stormTopology = tridentTopology.build();

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
