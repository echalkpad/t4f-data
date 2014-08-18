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
package io.datalayer.xsd.model.jaxb;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.junit.Test;

/**
 *
 */
public class T4FJaxbTest {

	/**
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void testMarshall() throws JAXBException {

		JAXBContext jc = JAXBContext.newInstance(PurchaseOrderType.class.getPackage().getName());
		ObjectFactory factory = new ObjectFactory();
		
		PurchaseOrderType type = factory.createPurchaseOrderType();
		type.setComment("comment");
		Items items = factory.createItems();
		Items.Item o = factory.createItemsItem();
		o.setPartNum("test");
		o.setComment("part comment");
		o.setProductName("productname");
		o.setQuantity(2);

		items.getItem().add(o);
		type.setItems(items);

		JAXBElement<PurchaseOrderType> element = factory.createPurchaseOrder(type);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(element, System.out);

	}
	
	/**
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void testUnMarshall() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(PurchaseOrderType.class.getPackage().getName());
		Unmarshaller u = jc.createUnmarshaller();
		InputStream is = T4FJaxbTest.class.getClassLoader().getResourceAsStream("aos/t4f/xsd/model/jaxb/purchase-order.xml");
		JAXBElement<PurchaseOrderType> doc = (JAXBElement<PurchaseOrderType>) u.unmarshal(T4FJaxbTest.class.getClassLoader().getResourceAsStream("aos/t4f/xsd/model/jaxb/purchase-order.xml"));
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(doc, System.out);
	}

	/**
	 * 
	 * @throws JAXBException
	 */
	@Test
	public void testUnMarshallWithoutXmlRoot() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(PurchaseOrderType.class.getPackage().getName());
		Unmarshaller u = jc.createUnmarshaller();
		InputStream is = T4FJaxbTest.class.getClassLoader().getResourceAsStream("aos/t4f/xsd/model/jaxb/purchase-order.xml");
		JAXBElement<PurchaseOrderType> doc = (JAXBElement<PurchaseOrderType>) u.unmarshal(T4FJaxbTest.class.getClassLoader().getResourceAsStream("aos/t4f/xsd/model/jaxb/purchase-order.xml"));
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(new JAXBElement(new QName("uri","local"), doc.getClass(), doc), System.out);
	}

}
