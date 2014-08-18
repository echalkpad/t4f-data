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

package com.wideplay.codemonkey.web.startup;

import com.google.inject.Inject;
import com.wideplay.warp.persist.PersistenceService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * On: May 26, 2007 2:33:20 PM
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class Initializer {
    private final PersistenceService service;

    @Inject
    public Initializer(PersistenceService service) {
        this.service = service;

        service.start();
        System.out.println("Initializer started up...");
    }

    public static Properties loadProperties(String name) {
        Properties properties = new Properties();
        final InputStream stream = Initializer.class.getResourceAsStream(name);
        try {
            properties.load(stream);
        } catch (IOException e) {
//            log.warn("Unable to find/load persistence.properties for hibernate module (assuming defaults)", e);
            return new Properties();
        } finally {
            try {
                stream.close();
            } catch(IOException e) {
                //cant do anything
//                log.warn("Exception encountered while closing stream after loading " + PERSISTENCE_PROPERTIES, e);
            }
        }

        return properties;
    }
}
