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
package io.datalayer.hadoop;

import java.util.concurrent.TimeUnit;

import org.apache.hadoop.hdfs.server.namenode.SecondaryNameNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aos.shell.process.AosProcessLauncher;

/**
 * SecondaryNameNode (17 threads) 
 * 
 * java 
 *   -Dproc_secondarynamenode 
 *   -Djava.net.preferIPv4Stack=true 
 *   -Xmx1000m
 *   -Xmx128m 
 *   -Xmx128m
 *   -Dhadoop.log.dir=/opt/hadoop-3.0.0-SNAPSHOT/logs
 *   -Dhadoop.log.file=hadoop-eric-secondarynamenode-eric.log
 *   -Dhadoop.home.dir=/opt/hadoop-3.0.0-SNAPSHOT 
 *   -Dhadoop.id.str=eric
 *   -Dhadoop.root.LOGGER=INFO,RFA 
 *   -Dhadoop.policy.file=hadoop-policy.xml
 *   -Djava.net.preferIPv4Stack=true 
 *   -Dhadoop.security.LOGGER=INFO,RFAS
 *   -Dhdfs.audit.LOGGER=INFO,NullAppender 
 *   -Dhadoop.security.LOGGER=INFO,RFAS
 *   -Dhdfs.audit.LOGGER=INFO,NullAppender 
 *   -Dhadoop.security.LOGGER=INFO,RFAS
 *   -Dhdfs.audit.LOGGER=INFO,NullAppender 
 *   -Dhadoop.security.LOGGER=INFO,RFAS
 *   
 * org.apache.hadoop.hdfs.server.namenode.SecondaryNameNode
 * 
 */
public class HdfsNameNodeSecondary {
    private static final Logger LOGGER = LoggerFactory.getLogger(HdfsNameNodeSecondary.class);

    public static void main(String... args) throws Exception {

        new AosProcessLauncher() {
            @Override
            public void process() throws Exception {
                SecondaryNameNode.main(new String[] {});
            }
        }.launch("HdfsSecondaryNameNode");

        while (true) {
            LOGGER.info("Sleeping...");
            TimeUnit.MINUTES.sleep(1);
        }

    }

}
