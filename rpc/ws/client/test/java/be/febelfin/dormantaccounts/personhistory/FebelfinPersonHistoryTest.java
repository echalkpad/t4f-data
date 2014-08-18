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
package be.febelfin.dormantaccounts.personhistory;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

import org.junit.Test;

import be.febelfin.dormantaccounts.FebelfinBaseUtil;
import be.febelfin.dormantaccounts.model.personhistory.AddressHistoryType;
import be.febelfin.dormantaccounts.model.personhistory.FebelfinPersonHistoryResponse;
import be.febelfin.dormantaccounts.model.personhistory.IPersonHistory;
import be.febelfin.dormantaccounts.model.personhistory.IPersonHistoryBindingStub;
import be.febelfin.dormantaccounts.model.personhistory.LegalDataBlockType;
import be.febelfin.dormantaccounts.model.personhistory.LegalDataBlocksType;
import be.febelfin.dormantaccounts.model.personhistory.PersonHistoryRequestType;
import be.febelfin.dormantaccounts.model.personhistory.PersonHistoryWebServiceLocator;

public class FebelfinPersonHistoryTest extends TestCase {
	
	private IPersonHistory _personHistory;
	
	//--------------------------------------------------------------------Tests

	@Test
	public void testPersonHistory() throws ServiceException, RemoteException {
		
		instanciatePersonHistoryWebService();
		
		PersonHistoryRequestType personHistoryRequest = new PersonHistoryRequestType();
		new FebelfinBaseUtil().setRequestBaseAttributes(personHistoryRequest);
		
		LegalDataBlocksType legalDataBlocks = new LegalDataBlocksType();
		legalDataBlocks.setLegalDataBlock(LegalDataBlockType.FAMILYCOMPOSITION);
		personHistoryRequest.setLegalDataBlocks(legalDataBlocks);
		
		personHistoryRequest.setSocialSecurityUser("socialSecurityUser");

		FebelfinPersonHistoryResponse personHistoryResponse = _personHistory.getPersonHistory(personHistoryRequest);
		
		new FebelfinBaseUtil().printErrorsAndWarnings(personHistoryResponse);
		new FebelfinBaseUtil().printResponseBaseAttributes(personHistoryResponse);
		
		printPersonHistory(personHistoryResponse);
		
	}

	//-----------------------------------------------------------------Privates

	private void instanciatePersonHistoryWebService() throws ServiceException {
		
		PersonHistoryWebServiceLocator personHistoryLocator = new PersonHistoryWebServiceLocator();

		personHistoryLocator.setEndpointAddress(new QName("IPersonHistoryPort"),
			"http://localhost:80/umg.u3.isabel.dormant-accounts.bank-interfaces/services/IPersonHistoryPort");

		_personHistory = personHistoryLocator.getIPersonHistoryPort();
		
		((IPersonHistoryBindingStub) _personHistory).setUsername("bank-username");
		((IPersonHistoryBindingStub) _personHistory).setPassword("bank-password");
		
	}
	
	private void printPersonHistory(FebelfinPersonHistoryResponse personHistoryResponse) {

		AddressHistoryType[] addressHistory = personHistoryResponse.getPersonHistoryResponse().getAddressHistory();

		for (AddressHistoryType addressHistoryItem : addressHistory) {
			addressHistoryItem.getAddressInformation().getLegalAddress().getStandardAddress().getStreet();
		}
	
	}
	
}
