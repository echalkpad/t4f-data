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
/*
 * CVS file status:
 * 
 * $Id: PublicationWsServicePortImpl.java,v 1.22 2010/03/11 08:43:41 dg Exp $
 * 
 * Copyright (c) Smals
 */
package io.datalayer.ws.spl._1;

import io.aos.ws.spl._1.PublicationPortType;
import io.aos.ws.spl._1.SystemError;
import io.aos.xsd.protocol.core._1_0.PublicationMessageType;
import io.aos.xsd.publication.protocol._1_0.SendMessageResponse;

import javax.jws.WebService;

@WebService()
public class PublicationWsServicePortImpl implements PublicationPortType {

    /*
     * (non-Javadoc)
     * 
     * @see
     * aos.t4f.publicationws.ws.PublicationPortType#sendMessage(aos.t4f.xsd.
     * protocol.core._1.PublicationMessageType)
     */
    @Override
    public SendMessageResponse sendMessage(PublicationMessageType body) throws SystemError {
        body.getMessageIdForReply();
        System.out.println("++++++++++++++++++++++");
        return null;
    }

}
