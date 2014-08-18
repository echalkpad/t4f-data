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
package com.wideplay.warp.persist.jdo;

import com.google.inject.Provider;
import com.wideplay.warp.persist.internal.*;

import javax.jdo.*;

/**
 * @author Miroslav Genov
 */
class JdoInternalWorkManager implements InternalWorkManager<PersistenceManager> {
  private final Provider<PersistenceManagerFactory> pmfProvider;

    JdoInternalWorkManager(Provider<PersistenceManagerFactory> pmfProvider) {
        this.pmfProvider = pmfProvider;
    }

    public PersistenceManager beginWork() {
        PersistenceManagerFactory emf = this.pmfProvider.get();
        PersistenceManager em = ManagedContext.getBind(PersistenceManager.class, emf);
        if (em == null) {
            em = emf.getPersistenceManager();
            ManagedContext.bind(PersistenceManager.class, emf, em);
        }
        return em;
    }

    public void endWork() {
        PersistenceManagerFactory emf = this.pmfProvider.get();
        if (ManagedContext.hasBind(PersistenceManager.class, emf)) {
            PersistenceManager em = ManagedContext.unbind(PersistenceManager.class, emf);
            if (em != null && !em.isClosed()) em.close();
        }
    }

}
