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
package io.datalayer.system.cpu;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Date;

public class CpuUsage {

    /**
     * @param args
     */
    public static void main(String... args) {

        ThreadMXBean TMB = ManagementFactory.getThreadMXBean();
        long time = new Date().getTime() * 1000000;
        long cput = 0;
        double cpuperc = -1;

        // Begin loop.

        if (TMB.isThreadCpuTimeSupported()) {
            if (new Date().getTime() * 1000000 - time > 1000000000) // Reset
                                                                    // once per
                                                                    // second
            {
                time = new Date().getTime() * 1000000;
                cput = TMB.getCurrentThreadCpuTime();
            }

            if (!TMB.isThreadCpuTimeEnabled()) {
                TMB.setThreadCpuTimeEnabled(true);
            }

            if (new Date().getTime() * 1000000 - time != 0)
                cpuperc = (TMB.getCurrentThreadCpuTime() - cput)
                        / (new Date().getTime() * 1000000.0 - time) * 100.0;
        } else {
            cpuperc = -2;
        }

        while (true) {
        }

    }

}
