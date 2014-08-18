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
package com.wideplay.warp.persist.jpa;

import com.google.inject.Provider;
import com.wideplay.warp.persist.internal.InternalWorkManager;
import com.wideplay.warp.persist.internal.ManagedContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Robbie Vanbrabant
 */
class JpaInternalWorkManager implements InternalWorkManager<EntityManager> {
    private final Provider<EntityManagerFactory> emfProvider;

    JpaInternalWorkManager(Provider<EntityManagerFactory> emfProvider) {
        this.emfProvider = emfProvider;
    }

    public EntityManager beginWork() {
        EntityManagerFactory emf = this.emfProvider.get();
        EntityManager em = ManagedContext.getBind(EntityManager.class, emf);
        if (em == null) {
            em = emf.createEntityManager();
            ManagedContext.bind(EntityManager.class, emf, em);
        }
        return em;
    }

    public void endWork() {
        EntityManagerFactory emf = this.emfProvider.get();
        if (ManagedContext.hasBind(EntityManager.class, emf)) {
            EntityManager em = ManagedContext.unbind(EntityManager.class, emf);
            if (em != null && em.isOpen()) em.close();
        }
    }
}
