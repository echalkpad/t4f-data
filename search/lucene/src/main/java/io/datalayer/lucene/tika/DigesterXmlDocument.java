/****************************************************************
\ * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http:www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package io.datalayer.lucene.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * #1 Create DigesterXMLDocument #2 Create Contact #3 Set type attribute #4 Set
 * name property #5 Call populateDocument #6 Parse XML InputStream #7 Create
 * Lucene document
 */
public class DigesterXmlDocument {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigesterXmlDocument.class);

    private final Digester dig;
    private static Document doc;

    public DigesterXmlDocument() {

        dig = new Digester();
        dig.setValidating(false);

        dig.addObjectCreate("address-book", DigesterXmlDocument.class);
        dig.addObjectCreate("address-book/contact", Contact.class);

        dig.addSetProperties("address-book/contact", "type", "type");

        dig.addCallMethod("address-book/contact/name", "setName", 0);
        dig.addCallMethod("address-book/contact/address", "setAddress", 0);
        dig.addCallMethod("address-book/contact/city", "setCity", 0);
        dig.addCallMethod("address-book/contact/province", "setProvince", 0);
        dig.addCallMethod("address-book/contact/postalcode", "setPostalcode", 0);
        dig.addCallMethod("address-book/contact/country", "setCountry", 0);
        dig.addCallMethod("address-book/contact/telephone", "setTelephone", 0);

        dig.addSetNext("address-book/contact", "populateDocument");
    }

    public synchronized Document getDocument(InputStream is) throws DocumentHandlerException {

        try {
            dig.parse(is);
        }
        catch (IOException e) {
            throw new DocumentHandlerException("Cannot parse XML document", e);
        }
        catch (SAXException e) {
            throw new DocumentHandlerException("Cannot parse XML document", e);
        }

        return doc;
    }

    public void populateDocument(Contact contact) {

        doc = new Document();

        doc.add(new StoredField("type", contact.getType()));
        doc.add(new StoredField("name", contact.getName()));
        doc.add(new StoredField("address", contact.getAddress()));
        doc.add(new StoredField("city", contact.getCity()));
        doc.add(new StoredField("province", contact.getProvince()));
        doc.add(new StoredField("postalcode", contact.getPostalcode()));
        doc.add(new StoredField("country", contact.getCountry()));
        doc.add(new StoredField("telephone", contact.getTelephone()));
    }

    public static class Contact {
        private String type;
        private String name;
        private String address;
        private String city;
        private String province;
        private String postalcode;
        private String country;
        private String telephone;

        public void setType(String newType) {
            type = newType;
        }

        public String getType() {
            return type;
        }

        public void setName(String newName) {
            name = newName;
        }

        public String getName() {
            return name;
        }

        public void setAddress(String newAddress) {
            address = newAddress;
        }

        public String getAddress() {
            return address;
        }

        public void setCity(String newCity) {
            city = newCity;
        }

        public String getCity() {
            return city;
        }

        public void setProvince(String newProvince) {
            province = newProvince;
        }

        public String getProvince() {
            return province;
        }

        public void setPostalcode(String newPostalcode) {
            postalcode = newPostalcode;
        }

        public String getPostalcode() {
            return postalcode;
        }

        public void setCountry(String newCountry) {
            country = newCountry;
        }

        public String getCountry() {
            return country;
        }

        public void setTelephone(String newTelephone) {
            telephone = newTelephone;
        }

        public String getTelephone() {
            return telephone;
        }
    }

    public static void main(String[] args) throws Exception {
        DigesterXmlDocument handler = new DigesterXmlDocument();
        Document doc = handler.getDocument(new FileInputStream(new File(args[0])));
        LOGGER.info(doc.toString());
    }
}
