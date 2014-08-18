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
package com.wideplay.warp.persist.spi;

import com.wideplay.warp.persist.WorkManager;
import com.wideplay.warp.persist.PersistenceService;

/**
 * Used to visit a {@link PersistenceModule}
 * and gather state that needs to be used with static methods. This
 * hides the only static state we need (Servlet Filters) behind
 * some OO goodness.
 *
 * @author Robbie Vanbrabant
 */
public interface PersistenceModuleVisitor {
    /**
     * Publishes the module's {@link com.wideplay.warp.persist.WorkManager}
     * for consumption by Warp Persist's common infrastructure,
     * notably {@link com.wideplay.warp.persist.PersistenceFilter}
     * and {@link com.wideplay.warp.persist.PersistenceFilter}.
     * <p>
     * Only use with {@link com.wideplay.warp.persist.UnitOfWork#REQUEST}.
     *
     * @param wm the {@code WorkManager} to publish
     */
    void publishWorkManager(WorkManager wm);

    /**
     * Publishes the module's {@link com.wideplay.warp.persist.PersistenceService}
     * for consumption by Warp Persist's common infrastructure,
     * notably {@link com.wideplay.warp.persist.PersistenceFilter}.
     * <p>
     * Usually used with {@link com.wideplay.warp.persist.UnitOfWork#REQUEST}, but
     * technically it could make sense to use the
     * {@link com.wideplay.warp.persist.PersistenceFilter} with other units
     * of work.
     *
     * @param persistenceService the {@code PersistenceService} to publish
     */
    void publishPersistenceService(PersistenceService persistenceService);
}
