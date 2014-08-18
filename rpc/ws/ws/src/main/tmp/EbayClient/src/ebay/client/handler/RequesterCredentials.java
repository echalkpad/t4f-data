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
 * RequesterCredentials.java
 *
 * Created on August 29, 2006, 11:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ebay.client.handler;

/**
 *
 * @author mode
 */
import ebay.apis.*;
import java.util.Properties;
import javax.xml.ws.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.soap.*;
import javax.xml.namespace.QName;
import java.util.Set;
import java.io.*;

public class RequesterCredentials implements SOAPHandler<SOAPMessageContext> {
    private static Properties props = new Properties();

    static {
        try {
            props.load(new FileReader("ebay.properties"));
        }catch (FileNotFoundException fne) {
            System.out.println("Could not find ebay.properties");
            System.exit(1);
        }catch (IOException ioe) {
            System.out.println("Error reading ebay.properties " + ioe.getMessage());
            System.exit(1);
        }
    }

    public Set<QName> getHeaders() {
        return null;
    }
    
    public boolean handleMessage(SOAPMessageContext smc) {
        addRequesterCredentials(smc);
        return true;
    }
    
    public boolean handleFault(SOAPMessageContext smc) {
        return true;
    }
    

    public void close(MessageContext messageContext) {
    }
    
    private void addRequesterCredentials(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean)
            smc.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        if (outboundProperty.booleanValue()) {
            SOAPMessage message = smc.getMessage();
            try {
                SOAPHeader header = message.getSOAPHeader();
		if (header == null) {
		    message.getSOAPPart().getEnvelope().addHeader();
		    header = message.getSOAPHeader();
		}
                SOAPElement heSecurity = header.addChildElement
		                        ("RequesterCredentials", 
					"ebl", 
					"urn:ebay:apis:eBLBaseComponents");
                heSecurity.addChildElement("eBayAuthToken", "ebl", "urn:ebay:apis:eBLBaseComponents").addTextNode((String) props.get("authToken"));
                SOAPElement userNameToken = heSecurity.addChildElement("Credentials", "ebl", "urn:ebay:apis:eBLBaseComponents");
                userNameToken.addChildElement("AppId", "ebl", "urn:ebay:apis:eBLBaseComponents").addTextNode((String) props.get("appID"));
                userNameToken.addChildElement("DevId", "ebl", "urn:ebay:apis:eBLBaseComponents").addTextNode((String) props.get("devID"));
                userNameToken.addChildElement("AuthCert", "ebl", "urn:ebay:apis:eBLBaseComponents").addTextNode((String) props.get("certID"));
		//message.writeTo(System.out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
