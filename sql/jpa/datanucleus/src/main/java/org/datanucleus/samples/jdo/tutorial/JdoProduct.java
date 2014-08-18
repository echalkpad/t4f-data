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
package org.datanucleus.samples.jdo.tutorial;

import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Definition of a Product Represents a product, and contains the key aspects of
 * the item.
 */
@PersistenceCapable(
		identityType=IdentityType.APPLICATION, 
		table="JDO_PRODUCT")
@DatastoreIdentity(
		strategy=IdGeneratorStrategy.IDENTITY)
@Inheritance(
		strategy=InheritanceStrategy.NEW_TABLE)
public class JdoProduct {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
	protected Integer productId;
	
	@Persistent
	protected String name = null;

	@Persistent
	protected String description = null;

	@Persistent
	protected Double price = 0.0;

	protected JdoProduct() {
	}

	public JdoProduct(String name, String description, double price) {
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Double getPrice() {
		return price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String toString() {
		return "Product : " + name + " [" + description + "]";
	}
}
