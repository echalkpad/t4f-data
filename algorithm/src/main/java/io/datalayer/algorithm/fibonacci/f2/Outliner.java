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
import java.util.*;

public class Outliner extends HTMLEditorKit.ParserCallback {

  private Writer out;
  private int level = 0;
  private boolean inHeader=false;
  private static String lineSeparator = System.getProperty("line.separator", "\r\n");
  
  public Outliner(Writer out) {
    this.out = out;
  }

  public void handleStartTag(HTML.Tag tag, 
   MutableAttributeSet attributes, int position) {
    
    int newLevel = 0;
    if (tag == HTML.Tag.H1) newLevel = 1;
    else if (tag == HTML.Tag.H2) newLevel = 2;
    else if (tag == HTML.Tag.H3) newLevel = 3;
    else if (tag == HTML.Tag.H4) newLevel = 4;
    else if (tag == HTML.Tag.H5) newLevel = 5;
    else if (tag == HTML.Tag.H6) newLevel = 6;
    else return;
    
    this.inHeader = true;
    try {
      if (newLevel > this.level) {
        for (int i =0; i < newLevel-this.level; i++) {
          out.write("<ul>" + lineSeparator + "<li>");
        }
      }
      else if (newLevel < this.level) {
        for (int i =0; i < this.level-newLevel; i++) {
          out.write(lineSeparator + "</ul>" + lineSeparator);
        }
        out.write(lineSeparator + "<li>");
      }
      else {
        out.write(lineSeparator + "<li>"); 
      }
      this.level = newLevel;
      out.flush();
    }
    catch (IOException e) {
      System.err.println(e);
    }
    
  }
  
  public void handleEndTag(HTML.Tag tag, int position) {

    if (tag == HTML.Tag.H1 || tag == HTML.Tag.H2 
     || tag == HTML.Tag.H3 || tag == HTML.Tag.H4
     || tag == HTML.Tag.H5 || tag == HTML.Tag.H6) {
      inHeader = false;
    }
    
    // work around bug in the parser that fails to call flush
    if (tag == HTML.Tag.HTML) this.flush();
    
  }
  
  
  public void handleText(char[] text, int position) { 
    
    if (inHeader) {
      try { 
        out.write(text);
        out.flush();
      }
      catch (IOException e) {
        System.err.println(e);
      }
    }
    
  }
  
  public void flush() {
    try {
      while (this.level-- > 0) {
        out.write(lineSeparator + "</ul>");   
      } 
      out.flush();
    }
    catch (IOException e) {
      System.err.println(e);
    }
  } 
  
  public static void main(String... args) { 
    
    ParserGetter kit = new ParserGetter();
    HTMLEditorKit.Parser parser = kit.getParser();
  
    try {
      URL u = new URL(args[0]);
      InputStream in = u.openStream();
      InputStreamReader r = new InputStreamReader(in);
      HTMLEditorKit.ParserCallback callback = new Outliner
       (new OutputStreamWriter(System.out));
      parser.parse(r, callback, false);
    }
    catch (IOException e) {
      System.err.println(e); 
    }
    catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Usage: java Outliner url"); 
    }
          
  }
  
}
