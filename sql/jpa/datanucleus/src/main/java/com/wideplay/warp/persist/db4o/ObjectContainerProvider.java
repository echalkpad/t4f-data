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

import com.db4o.ObjectContainer;
import com.google.inject.Provider;
import com.wideplay.warp.persist.internal.InternalWorkManager;

/**
 * @author Jeffrey Chung (jeffreymchung@gmail.com)
 * @author Robbie Vanbrabant
 */
class ObjectContainerProvider implements Provider<ObjectContainer> {
    private final InternalWorkManager<ObjectContainer> internalWorkManager;

    public ObjectContainerProvider(InternalWorkManager<ObjectContainer> internalWorkManager) {
        this.internalWorkManager = internalWorkManager;
	}
	
	public ObjectContainer get() {
		return internalWorkManager.beginWork();
    }
}
