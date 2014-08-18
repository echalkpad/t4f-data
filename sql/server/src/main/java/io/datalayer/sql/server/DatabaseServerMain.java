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
package io.datalayer.sql.server;

import java.io.PrintWriter;
import java.net.InetAddress;

import org.apache.derby.drda.NetworkServerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseServerMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseServerMain.class);

    public static void main(String... args) throws Exception {

        final NetworkServerControl serverControl = new NetworkServerControl(InetAddress.getByName("0.0.0.0"), 1527);
        final PrintWriter pw = new PrintWriter(System.out, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverControl.start(pw);
                } catch (Exception e) {
                    LOGGER.error("Exception while running the database server", e);
                    throw new RuntimeException("Exception while running the database server", e);
                }
            }
        }).start();

    }

}
