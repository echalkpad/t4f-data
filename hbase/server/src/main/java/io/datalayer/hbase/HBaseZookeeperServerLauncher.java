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
package io.datalayer.hbase;

import io.aos.shell.process.AosProcessLauncher;

import org.apache.hadoop.hbase.zookeeper.HQuorumPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Simple class that launches a Zookeeper server.
 * </p>
 */
public class HBaseZookeeperServerLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseZookeeperServerLauncher.class);

    public static void main(String... args) throws Exception {
        
        new AosProcessLauncher() {
            @Override
            public void process() throws Exception {
                HQuorumPeer.main(new String[] { "start" });
            }
        }.launch("ZookeeperServer");

        while (true) {
            LOGGER.info("Sleeping...");
            Thread.sleep(Long.MAX_VALUE);
        }

    }
    
}
