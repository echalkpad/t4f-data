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
package io.datalayer.hbase.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class HBaseTestingUtilityTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseTestingUtilityTest.class);

    @Test
    public void testHBaseCluster() throws Exception {
        Configuration conf = HBaseConfiguration.create();

        HBaseTestingUtility hbaseTestingUtility = new HBaseTestingUtility(conf);

        // Workaround for HBASE-5711, we need to set config value
        // dfs.datanode.data.dir.perm
        // equal to the permissions of the temp dirs on the filesystem. These
        // temp dirs were
        // probably created using this process' umask. So we guess the temp dir
        // permissions as
        // 0777 & ~umask, and use that to set the config value.
        try {
            Process process = Runtime.getRuntime().exec("/bin/sh -c umask");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int rc = process.waitFor();
            if (rc == 0) {
                String umask = br.readLine();

                int umaskBits = Integer.parseInt(umask, 8);
                int permBits = 0777 & ~umaskBits;
                String perms = Integer.toString(permBits, 8);

                LOGGER.info("Setting dfs.datanode.data.dir.perm to " + perms);
                hbaseTestingUtility.getConfiguration().set("dfs.datanode.data.dir.perm", perms);
            } else {
                LOGGER.warn("Failed running umask command in a shell, nonzero return value");
            }
        } catch (Exception e) {
            // ignore errors, we might not be running on POSIX, or "sh" might
            // not be on the path
            LOGGER.warn("Couldn't get umask", e);
        }

        MiniHBaseCluster miniHBaseCluster = hbaseTestingUtility.startMiniCluster();
        Iterator<Map.Entry<String, String>> it = hbaseTestingUtility.getMiniHBaseCluster().getConfiguration()
                .iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            System.out.println(next.getKey() + " = " + next.getValue());
        }
        // HTable table = new HTable(hbaseTestingUtility.getConfiguration(),
        // "test");
        // hbaseTestingUtility.createMultiRegions(table, "cf".getBytes());
        hbaseTestingUtility.shutdownMiniCluster();
    }

}
