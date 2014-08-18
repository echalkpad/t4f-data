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
package org.apache.activemq.book.ch6;

import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.Message;
import org.apache.activemq.security.MessageAuthorizationPolicy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthorizationPolicy implements
		MessageAuthorizationPolicy {
	
    private static final Log LOG = LogFactory.getLog(AuthorizationPolicy.class);

	public boolean isAllowedToConsume(ConnectionContext context, Message message) {
		LOG.info(context.getConnection().getRemoteAddress());
		if (context.getConnection().getRemoteAddress().startsWith("/127.0.0.1")) {
			LOG.info("Permission to consume granted");
			return true;
		} else {
			LOG.info("Permission to consume denied");
			return false;
		}
	}

}
