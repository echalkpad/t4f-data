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

/**
 * Definition of a Book. Extends basic Product class.
 */
@PersistenceCapable(
		identityType = IdentityType.APPLICATION, 
		table = "JDO_BOOK")
@DatastoreIdentity(
		strategy = IdGeneratorStrategy.IDENTITY)
@Inheritance(
		strategy = InheritanceStrategy.NEW_TABLE)
public class JdoBook extends JdoProduct {
	
	@Persistent
	protected String author = null;

	@Persistent
	protected String isbn = null;

	@Persistent
	protected String publisher = null;

	protected JdoBook() {
		super();
	}

	public JdoBook(String name, String description, double price, String author, String isbn, String publisher) {
		super(name, description, price);
		this.author = author;
		this.isbn = isbn;
		this.publisher = publisher;
	}

	public String getAuthor() {
		return author;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String toString() {
		return "Book : " + author + " - " + name;
	}
}
