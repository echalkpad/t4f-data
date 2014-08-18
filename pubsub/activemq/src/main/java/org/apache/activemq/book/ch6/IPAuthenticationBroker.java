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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;

public class IPAuthenticationBroker extends BrokerFilter {

	List<String> allowedIPAddresses;
	Pattern pattern = Pattern.compile("^/([0-9\\.]*):(.*)");
	
	public IPAuthenticationBroker(Broker next, List<String> allowedIPAddresses) {
		super(next);
		this.allowedIPAddresses = allowedIPAddresses;
	}

	public void addConnection(ConnectionContext context,
			ConnectionInfo info) throws Exception {

		String remoteAddress = context.getConnection().getRemoteAddress();
		
		Matcher matcher = pattern.matcher(remoteAddress);
		if (matcher.matches()) {
			String ip = matcher.group(1);
			if (!allowedIPAddresses.contains(ip)) {
				throw new SecurityException("Connecting from IP address " + ip + " is not allowed");
			}
		} else {
			throw new SecurityException("Invalid remote address " + remoteAddress);
		}
		
		super.addConnection(context, info);
	}	
	
}
