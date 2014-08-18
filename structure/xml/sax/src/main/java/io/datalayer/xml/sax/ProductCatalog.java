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
package io.datalayer.xml.sax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProductCatalog extends DefaultHandler{

	String content="";
    public ProductCatalog() {
		super();
	}

	protected void parseContent(File file)
	{
		try {
			SAXParser parser = createParser();			
	        InputStream is = new FileInputStream(file);

	        if (is == null) {
	            return;
	        }
			
			parser.parse(is, this);
			
        } 
		catch (Exception e) {
			
			e.printStackTrace();
			
        }
			
		return;
		
	}
	
	protected SAXParser createParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory f = SAXParserFactory.newInstance();		
		f.setValidating(false);
		return f.newSAXParser();
	}

    /**
     * Receive notification of the beginning of an element.
     */
    public void startElement(String namespaceURI, String localName, String elementName, Attributes atts) throws SAXException {
        
    	content+="\n"+elementName + ":\n";
    	
    	for(int idx=0; idx<atts.getLength(); idx++)
    	{
    		content+="\t"+atts.getQName(idx) + "=" + atts.getValue(idx)+"\n";
    	}
    }
    
    /**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	public void readData(String name) {
		
		if(name == null)
		{
			System.out.println("invalid folder name");
			return;
		}
		
		File file = new File(name);
		if(!file.exists() || !file.isDirectory())
		{
			System.out.println("invalid folder name " + name);
			return;
		}
		
	    File[] files = file.listFiles();
	    for(int idx=0; idx<files.length; idx++)
	    	parseContent(files[idx]);
	}
	
}
