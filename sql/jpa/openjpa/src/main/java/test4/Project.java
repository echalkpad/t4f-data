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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity(name = "Proj")
public class Project {	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROJECT_ID")
	private int idProject;

	@Basic
	@Column(nullable = false)
	private String name;

	@Basic
	@Column(nullable = true)
	@Index
	private String name2;

//    @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
//    @JoinColumn(name="id", nullable = false)
//	private Organization organization;

	public Project(String name) {
		this.name = name;
	}

	public int getIdProject() {
		return idProject;
	}

	public String getName() {
		return name;
	}

//	public Organization getOrganization() {
//		return (Organization) organization;
//	}

}
