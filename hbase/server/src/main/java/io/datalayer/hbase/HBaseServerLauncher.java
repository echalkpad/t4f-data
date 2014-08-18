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

import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.regionserver.HRegionServer;
import org.apache.hadoop.hbase.zookeeper.HQuorumPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aos.shell.process.AosProcessLauncher;

/**
 * <p>
 * Simple class that launches a Zookeeper server, HBase Master and an HBase Region Server.
 * 
 * It holds the thread so that you can use the HBase console to test the
 * connection, create tables...
 * 
 * It is aimed for demo purposes to highlight the various components and is
 * not intended to be run as a real cluster.
 * 
 * The configuration can be defined in the src/main/resources/hbase-site.xml
 * file.
 * </p>
 */
public class HBaseServerLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseServerLauncher.class);

    public static void main(String... args) throws Exception {
        
        new AosProcessLauncher() {
            @Override
            public void process() throws Exception {
                HBaseZookeeperServerLauncher.main();
            }
        }.launch("ZookeeperServer");

        new AosProcessLauncher() {
            @Override
            public void process() throws Exception {
                HBaseMasterServerLauncher.main();
            }
        }.launch("HRegionServer");

        new AosProcessLauncher() {
            @Override
            public void process() throws Exception {
                HBaseRegionServerLauncher.main();
            }
        }.launch("HMaster");

        while (true) {
            LOGGER.info("Sleeping...");
            Thread.sleep(Long.MAX_VALUE);
        }

    }
    
}
