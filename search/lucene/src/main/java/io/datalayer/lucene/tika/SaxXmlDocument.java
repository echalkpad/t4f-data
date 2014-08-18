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
package io.datalayer.lucene.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * #1 Start parser #2 Called when parsing begins #3 Beginning of new XML element
 * #4 Append element contents to elementBuffer #5 Called when closing XML
 * elements are processed
 */
public class SaxXmlDocument extends DefaultHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaxXmlDocument.class);

    private final StringBuilder elementBuffer = new StringBuilder();
    private final Map<String, String> attributeMap = new HashMap<String, String>();

    private Document doc;

    public static void main(String... args) throws Exception {
        SaxXmlDocument handler = new SaxXmlDocument();
        Document doc = handler.getDocument(new FileInputStream(new File(args[0])));
        LOGGER.info(doc.toString());
    }

    public Document getDocument(InputStream is) //
            throws DocumentHandlerException {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser parser = spf.newSAXParser();
            parser.parse(is, this);
        }
        catch (Exception e) {
            throw new DocumentHandlerException("Cannot parse XML document", e);
        }

        return doc;
    }

    @Override
    public void startDocument() { //
        doc = new Document();
    }

    @Override
    public void startElement(String uri, String localName, //
            String qName, Attributes atts) //
            throws SAXException { //

        elementBuffer.setLength(0);
        attributeMap.clear();
        int numAtts = atts.getLength();
        if (numAtts > 0) {
            for (int i = 0; i < numAtts; i++) {
                attributeMap.put(atts.getQName(i), atts.getValue(i));
            }
        }
    }

    @Override
    public void characters(char[] text, int start, int length) { //
        elementBuffer.append(text, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) //
            throws SAXException {
        if (qName.equals("address-book")) {
            return;
        }
        else if (qName.equals("contact")) {
            for (Entry<String, String> attribute : attributeMap.entrySet()) {
                String attName = attribute.getKey();
                String attValue = attribute.getValue();
                doc.add(new StoredField(attName, attValue));
            }
        }
        else {
            doc.add(new StoredField(qName, elementBuffer.toString()));
        }
    }

}
