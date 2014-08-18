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
package test4;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

/**
 * 
 * 
 */
@MappedSuperclass
public abstract class AbstractOrganization {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ORGANIZATION_ID")
	private int organisationId;

	@Basic
	@Column(nullable = false, length = 100)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ElementJoinColumn(name="PROJECT_ORGANIZATION_ID", referencedColumnName="ORGANIZATION_ID")
//	@InverseLogical("organization")
	private List<Project> projects;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ElementJoinColumn(name="MEMBER_ORGANIZATION_ID", referencedColumnName="ORGANIZATION_ID")
	private List<Member> members;

	public ArrayList<Project> getProjects() {
		return (ArrayList<Project>) projects;
	}

//	public ArrayList<Member> getMembers() {
//		return (ArrayList<Member>) members;
//	}

	public void addProject(Project project) {
		if (projects == null) {
			projects = new ArrayList<Project>();
		}
		this.projects.add(project);
	}

//	public void addMember(Member member) {
//		if (members == null) {
//			members = new ArrayList<Member>();
//		}
//		this.members.add(member);
//	}

	public int getOrganizationId() {
		return organisationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
