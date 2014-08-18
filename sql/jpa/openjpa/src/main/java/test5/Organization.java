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
package test5;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Organization {	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORGANIZATION_ID")
	private int idOrganization;

	@Basic
	@Column(nullable = false, length = 100)
	private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private List<Project> projects;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    private List<Member> members;

	public Organization(String name) {
		this.name = name;
	}

	public int getIdOrganization() {
		return idOrganization;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Project> getProjects() {
		return (ArrayList<Project>) projects;
	}
	
    public void addProject(Project project) {
        if (projects == null) {
            projects = new ArrayList<Project>();
        }
        this.projects.add(project);
    }

    public void addMember(Member member) {
        if (members == null) {
            members = new ArrayList<Member>();
        }
        this.members.add(member);
    }

}
