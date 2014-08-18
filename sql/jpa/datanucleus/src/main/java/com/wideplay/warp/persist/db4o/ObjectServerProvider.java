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

package com.wideplay.warp.persist.db4o;

import com.db4o.Db4o;
import com.db4o.ObjectServer;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.wideplay.warp.persist.internal.LazyReference;
import static com.wideplay.warp.persist.internal.Text.empty;
import net.jcip.annotations.Immutable;

/**
 * @author Jeffrey Chung (jeffreymchung@gmail.com)
 * @author Robbie Vanbrabant
 */
@Immutable
class ObjectServerProvider implements Provider<ObjectServer> {
    /**
     * Lazily loaded ObjectServer.
     */
    private LazyReference<ObjectServer> objectServer =
            LazyReference.of(new Provider<ObjectServer>() {
                public ObjectServer get() {
                    Db4oSettings actualSettings = getSettings();

                    //validate configuration object
                    if ((!HostKind.FILE.equals(actualSettings.getHostKind())) && null == actualSettings.getConfiguration())
                        throw new IllegalStateException("Must specify a Configuration when using " + actualSettings.getHost() + " server mode." +
                                " For starters, try: bind(Configuration.class).toInstance(Db4o.newConfiguration());");

                    //use local (i.e. open our own) object server?
                    if (HostKind.LOCAL.equals(actualSettings.getHostKind())) {
                        ObjectServer objectServer = Db4o.openServer(actualSettings.getConfiguration(), actualSettings.getDatabaseFileName(), actualSettings.getPort());

                        //auth if credentials are available
                        if (!empty(actualSettings.getUser()))
                            objectServer.grantAccess(actualSettings.getUser(), actualSettings.getPassword());

                        return objectServer;
                        //otherwise it's a simple local-file database
                    } else if (HostKind.FILE.equals(actualSettings.getHostKind())) {
                        // optional Configuration instance
                        if (actualSettings.getConfiguration() != null) {
                            return Db4o.openServer(actualSettings.getConfiguration(), actualSettings.getDatabaseFileName(), actualSettings.getPort());
                        } else {
                            return Db4o.openServer(actualSettings.getDatabaseFileName(), actualSettings.getPort());
                        }
                    }
                    // remote, fake objectServer.
                    return new NullObjectServer();
                }
            });

    @Inject
    private final Injector injector = null;

    private final Db4oSettings settings;

    public ObjectServerProvider(Db4oSettings settings) {
        this.settings = settings;
    }

    public ObjectServer get() {
        return this.objectServer.get();
    }

    protected Db4oSettings getSettings() {
        return this.settings != null ?
                this.settings :
                injector.getInstance(Db4oPersistenceStrategy.Db4oPersistenceStrategyBuilder.class).buildDb4oSettings();
    }
}
