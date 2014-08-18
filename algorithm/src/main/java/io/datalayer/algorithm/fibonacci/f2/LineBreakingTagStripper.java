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
package io.datalayer.algorithm.fibonacci.f2;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import java.io.*;
import java.net.*;


public class LineBreakingTagStripper extends HTMLEditorKit.ParserCallback {

  private Writer out;
  private String lineSeparator;
  
  public LineBreakingTagStripper(Writer out) {
    this(out, System.getProperty("line.separator", "\r\n")); 
  }  
  
  public LineBreakingTagStripper(Writer out, String lineSeparator) {
    this.out = out; 
    this.lineSeparator = lineSeparator;
  }  
  
  public void handleText(char[] text, int position) {
    try {
      out.write(text);
      out.flush();
    }
    catch (IOException e) {
      System.err.println(e); 
    }
  }
  
  public void handleEndTag(HTML.Tag tag, int position) {

    try {
      if (tag.isBlock()) {
        out.write(lineSeparator);
        out.write(lineSeparator);
      }
      else if (tag.breaksFlow()) {
        out.write(lineSeparator);
      }
    }
    catch (IOException e) {
      System.err.println(e); 
    }
    
  }
  public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, 
   int position) {
    
    try {
      if (tag.isBlock()) {
        out.write(lineSeparator);
        out.write(lineSeparator);
      }
      else if (tag.breaksFlow()) {
        out.write(lineSeparator);
      }
      else {
        out.write(' '); 
      }
    }
    catch (IOException e) {
      System.err.println(e); 
    }
 
  }
  
  public static void main(String... args) {
    
    ParserGetter kit = new ParserGetter();
    HTMLEditorKit.Parser parser = kit.getParser();
    HTMLEditorKit.ParserCallback callback 
     = new LineBreakingTagStripper(new OutputStreamWriter(System.out));
    
    try {
      URL u = new URL(args[0]);
      InputStream in = u.openStream();
      InputStreamReader r = new InputStreamReader(in);
      parser.parse(r, callback, false);
    }
    catch (IOException e) {
      System.err.println(e); 
    }
    
  }
  
}
