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

import org.apache.hadoop.yarn.server.nodemanager.NodeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aos.shell.process.AosProcessLauncher;

/**
 * NodeManager (56 threads) 
 * 
 * java 
 * 
 *   -Dproc_nodemanager 
 *   -Xmx1000m
 * -Dhadoop.log.dir=/opt/hadoop-3.0.0-SNAPSHOT/logs
 * -Dyarn.log.dir=/opt/hadoop-3.0.0-SNAPSHOT/logs
 * -Dhadoop.log.file=yarn-eric-nodemanager-eric.log
 * -Dyarn.log.file=yarn-eric-nodemanager-eric.log -Dyarn.home.dir=
 * -Dyarn.id.str=eric -Dhadoop.root.LOGGER=INFO,RFA
 * -Dyarn.root.LOGGER=INFO,RFA -Dyarn.policy.file=hadoop-policy.xml -server
 * -Dhadoop.log.dir=/opt/hadoop-3.0.0-SNAPSHOT/logs
 * -Dyarn.log.dir=/opt/hadoop-3.0.0-SNAPSHOT/logs
 * -Dhadoop.log.file=yarn-eric-nodemanager-eric.log
 * -Dyarn.log.file=yarn-eric-nodemanager-eric.log
 * -Dyarn.home.dir=/opt/hadoop-3.0.0-SNAPSHOT
 * -Dhadoop.home.dir=/opt/hadoop-3.0.0-SNAPSHOT -Dhadoop.root.LOGGER=INFO,RFA
 * -Dyarn.root.LOGGER=INFO,RFA -classpath
 * /opt/hadoop-3.0.0-SNAPSHOT/etc/hadoop
 * :/opt/hadoop-3.0.0-SNAPSHOT/etc/hadoop
 * :/opt/hadoop-3.0.0-SNAPSHOT/etc/hadoop
 * :/opt/hadoop-3.0.0-SNAPSHOT/share/hadoop
 * /common/lib/*:/opt/hadoop-3.0.0-SNAPSHOT
 * /share/hadoop/common/*:/opt/hadoop
 * -3.0.0-SNAPSHOT/contrib/capacity-scheduler
 * /*.jar:/opt/hadoop-3.0.0-SNAPSHOT/
 * contrib/capacity-scheduler/*.jar:/opt/hadoop
 * -3.0.0-SNAPSHOT/share/hadoop/hdfs
 * :/opt/hadoop-3.0.0-SNAPSHOT/share/hadoop/hdfs
 * /lib/*:/opt/hadoop-3.0.0-SNAPSHOT
 * /share/hadoop/hdfs/*:/opt/hadoop-3.0.0-SNAPSHOT
 * /share/hadoop/mapreduce/lib/*
 * :/opt/hadoop-3.0.0-SNAPSHOT/share/hadoop/mapreduce
 * /*:/opt/hadoop-3.0.0-SNAPSHOT
 * /share/hadoop/mapreduce/*:/opt/hadoop-3.0.0-SNAPSHOT
 * /share/hadoop/mapreduce/
 * lib/*:/opt/hadoop-3.0.0-SNAPSHOT/etc/hadoop/nm-config/log4j.properties
 * org.apache.hadoop.yarn.server.nodemanager.NodeManager
 */
public class YarnNodeManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(YarnNodeManager.class);

    public static void main(String... args) throws Exception {

        new AosProcessLauncher() {
            @Override
            public void process() throws Exception {
                NodeManager.main(new String[] {});
            }
        }.launch("YarnNodeManager");

        while (true) {
            LOGGER.info("Sleeping...");
            TimeUnit.MINUTES.sleep(1);
        }

    }

}
