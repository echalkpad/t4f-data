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
package be.febelfin.dormantaccounts.identityperson;

import java.math.BigInteger;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

import org.junit.Test;

import be.febelfin.dormantaccounts.FebelfinBaseUtil;
import be.febelfin.dormantaccounts.model.common.Gender;
import be.febelfin.dormantaccounts.model.common.Name;
import be.febelfin.dormantaccounts.model.identifyperson.FebelfinPersonIdentificationPhoneticRequest;
import be.febelfin.dormantaccounts.model.identifyperson.FebelfinPersonIdentificationSSINRequest;
import be.febelfin.dormantaccounts.model.identifyperson.IIdentifyPerson;
import be.febelfin.dormantaccounts.model.identifyperson.IIdentifyPersonBindingStub;
import be.febelfin.dormantaccounts.model.identifyperson.IdentificationResultSet;
import be.febelfin.dormantaccounts.model.identifyperson.IdentifyPersonWebServiceLocator;
import be.febelfin.dormantaccounts.model.identifyperson.PersonIdentificationResponse;

public class FebelfinIdentifyPersonTest extends TestCase {
	
	private IIdentifyPerson _identifyPerson;
	
	//--------------------------------------------------------------------Tests

	@Test
	public void testIdentityPersonSSIN() throws ServiceException, RemoteException {
		
		instanciateIdentifyPersonWebService();
		
		FebelfinPersonIdentificationSSINRequest personIdentificationSSINRequest = 
			new FebelfinPersonIdentificationSSINRequest();

		new FebelfinBaseUtil().setRequestBaseAttributes(personIdentificationSSINRequest);

		personIdentificationSSINRequest.setSSIN("12345678901");
		
		PersonIdentificationResponse personIdentificationResponse = 
			_identifyPerson.identifyPersonSSIN(personIdentificationSSINRequest);
		
		new FebelfinBaseUtil().printErrorsAndWarnings(personIdentificationResponse);
		new FebelfinBaseUtil().printResponseBaseAttributes(personIdentificationResponse);
		
		printIdentiyPersonResponse(personIdentificationResponse);
		
	}
		
	@Test
	public void testIdentityPersonPhonetic() throws ServiceException, RemoteException {
		
		instanciateIdentifyPersonWebService();
		
		FebelfinPersonIdentificationPhoneticRequest personIdentificationPhoneticRequest = 
			new FebelfinPersonIdentificationPhoneticRequest();
		new FebelfinBaseUtil().setRequestBaseAttributes(personIdentificationPhoneticRequest);
		personIdentificationPhoneticRequest.setMaximumResults(new BigInteger("20"));
		
		personIdentificationPhoneticRequest.setName(new Name("lastname", new String[]{"firstname1", "firstname2"}));
		personIdentificationPhoneticRequest.setGender(Gender.male);
		personIdentificationPhoneticRequest.setBirthDate("1960-00-00");
		personIdentificationPhoneticRequest.setBirthDateTolerance(new BigInteger("1"));
		
		PersonIdentificationResponse personIdentificationResponse = 
			_identifyPerson.identifyPersonPhonetic(personIdentificationPhoneticRequest);
		
		new FebelfinBaseUtil().printErrorsAndWarnings(personIdentificationResponse);
		new FebelfinBaseUtil().printResponseBaseAttributes(personIdentificationResponse);
		
		printIdentiyPersonResponse(personIdentificationResponse);
		
	}
	
	//-----------------------------------------------------------------Privates

	private void instanciateIdentifyPersonWebService() throws ServiceException {
		
		IdentifyPersonWebServiceLocator identifyPersonLocator = new IdentifyPersonWebServiceLocator();
		
		identifyPersonLocator.setEndpointAddress(new QName("IIdentifyPersonPort"),
			"http://localhost:80/umg.u3.isabel.dormant-accounts.bank-interfaces/services/IIdentifyPersonPort");

		_identifyPerson = identifyPersonLocator.getIIdentifyPersonPort();

		((IIdentifyPersonBindingStub) _identifyPerson).setUsername("bank-username");
		((IIdentifyPersonBindingStub) _identifyPerson).setPassword("bank-password");
		
	}

	private void printIdentiyPersonResponse(PersonIdentificationResponse personIdentificationResponse) {
		
		IdentificationResultSet[] identificationResults = personIdentificationResponse.getIdentificationResultSet();
		
		for (IdentificationResultSet identificationResult : identificationResults) {
			
			identificationResult.getSSIN();
			identificationResult.getPlaceOfBirth();
			identificationResult.getDateOfPossibleDeath();
			identificationResult.getName().getLastName();
			identificationResult.getName().getFirstName(0);
			identificationResult.getName().getFirstName(1);
			identificationResult.getLegalAddress().getStreet();
			identificationResult.getLegalAddress().getTown();
			identificationResult.getLegalAddress().getZip();
			identificationResult.getLegalAddress().getCountry();
		
		}
		
	}

}
