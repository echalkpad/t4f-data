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
package aos.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author janstey
 * 
 */
public class MyInterceptor implements InterceptStrategy {
	private static final transient Log log = LogFactory
	        .getLog(MyInterceptor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.camel.spi.InterceptStrategy#wrapProcessorInInterceptors(org
	 * .apache.camel.CamelContext, org.apache.camel.model.ProcessorDefinition,
	 * org.apache.camel.Processor, org.apache.camel.Processor)
	 */
	public Processor wrapProcessorInInterceptors(CamelContext context,
	        ProcessorDefinition<?> definition, final Processor target,
	        Processor nextTarget) throws Exception {
		// to make the Default channel happy
		return new DelegateAsyncProcessor(new Processor() {

			public void process(Exchange exchange) throws Exception {
				log.info("Before the processor...");
				target.process(exchange);
				log.info("After the processor...");
			}
		});
	}

}
