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
package org.apache.activemq.book.ch8.jms.web.controller;

import org.apache.activemq.book.ch8.jms.domain.JmsMessage;
import org.apache.activemq.book.ch8.jms.service.JmsMessageSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;


/**
 * @author bsnyder
 *
 */
@Controller
@RequestMapping("/send.html")
public class JmsMessageSenderController {
	
	@Autowired
	private JmsMessageSenderService messageSenderService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(ModelMap model) {
        model.addAttribute("jmsMessageBean", new JmsMessage());
	    model.remove("successfulSend");
	    
	    return "send";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(
	        @ModelAttribute("jmsMessageBean") JmsMessage jmsMessageBean,
	        BindingResult result,
	        SessionStatus status,
	        ModelMap model)
			throws Exception {
		messageSenderService.sendMessage(jmsMessageBean);
		model.addAttribute("successfulSend", "The message was sent successfully");
        model.addAttribute("jmsMessageBean", new JmsMessage());
		status.setComplete();
		
		return "send";
	}

    public void setMessageSender(JmsMessageSenderService messageSender) {
        this.messageSenderService = messageSender;
    }

}
