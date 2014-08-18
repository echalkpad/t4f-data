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
package io.datalayer.sql.server.driver;

import org.apache.derby.jdbc.ClientDriver;
import org.apache.derby.jdbc.EmbeddedDriver;

public interface JdbcDriver {

    public static final Class DERBY_JDBC_EMBEDDED_CLASS = EmbeddedDriver.class;

    public static final Class DERBY_JDBC_EMBEDDED_DIRECTORY_CLASS = EmbeddedDriver.class;

    public static final Class DERBY_JDBC_EMBEDDED_MEMORY_CLASS = EmbeddedDriver.class;

    public static final Class DERBY_JDBC_NETTWORK_CLASS = ClientDriver.class;

}
