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
package be.febelfin.dormantaccounts;

import java.util.Date;

import be.febelfin.dormantaccounts.model.common.BankBIC;
import be.febelfin.dormantaccounts.model.common.BankRequestIdentification;
import be.febelfin.dormantaccounts.model.common.CaseUniqueID;
import be.febelfin.dormantaccounts.model.common.FebelfinBaseRequest;
import be.febelfin.dormantaccounts.model.common.FebelfinBaseResponse;
import be.febelfin.dormantaccounts.model.common.TargetApplication;
import be.febelfin.dormantaccounts.model.common.TargetEnvironment;
import be.febelfin.dormantaccounts.model.common.Version;

public class FebelfinBaseUtil {
	
	public void setRequestBaseAttributes(FebelfinBaseRequest febelfinBaseRequest) {
		
		BankBIC bankBIC = new BankBIC("bankCode","countryCode", "locationCode", "branchCode");
		
		CaseUniqueID caseUniqueID = new CaseUniqueID(bankBIC, "bank-23253-3434343232-343");
		febelfinBaseRequest.setCaseUniqueID(caseUniqueID);
		
		BankRequestIdentification bankRequestIdentitification = new BankRequestIdentification(bankBIC, 
				TargetApplication.DORMANT_ACCOUNTS, "HRYRTEZ7565");
		febelfinBaseRequest.setBankRequestIdentification(bankRequestIdentitification);
		
		febelfinBaseRequest.setTargetEnvironment(TargetEnvironment.TEST);
		
		febelfinBaseRequest.setVersion(Version.value1);
		
		febelfinBaseRequest.setTimestamp(new Date());

	}

	public void printErrorsAndWarnings(FebelfinBaseResponse febelfinBaseResponse) {
		
		febelfinBaseResponse.getFebelfinReplyContextResultSummary().getReturnCode();		
		febelfinBaseResponse.getFebelfinReplyContextResultSummary().getOk();
		febelfinBaseResponse.getFebelfinReplyContextResultSummary().getDetail();

		febelfinBaseResponse.getKszBcssReplyContextResultSummary().getReturnCode();
		febelfinBaseResponse.getKszBcssReplyContextResultSummary().getOk();
		febelfinBaseResponse.getKszBcssReplyContextResultSummary().getDetail();

	}

	public void printResponseBaseAttributes(FebelfinBaseResponse febelfinBaseResponse) {
		
		febelfinBaseResponse.getFebelfinResponseIdentification().getBankRequestIdentification();
		febelfinBaseResponse.getCaseUniqueID().getBankBIC().getBankCode();
		febelfinBaseResponse.getTargetEnvironment();
		febelfinBaseResponse.getTimestamp();
		febelfinBaseResponse.getVersion();

	}

}
