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
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.*;

public class SimpleWebBrowser {


  public static void main(String... args) {
        
    // get the first URL
    String initialPage = "http://metalab.unc.edu/javafaq/";
    if (args.length > 0) initialPage = args[0];       
    
    // set up the editor pane
    JEditorPane jep = new JEditorPane();
    jep.setEditable(false);   
    jep.addHyperlinkListener(new LinkFollower(jep));
    
    try {
      jep.setPage(initialPage);      
    }
    catch (IOException e) {
      System.err.println("Usage: java SimpleWebBrowser url"); 
      System.err.println(e);
      System.exit(-1);
    }
      
    // set up the window
    JScrollPane scrollPane = new JScrollPane(jep);     
    JFrame f = new JFrame("Simple Web Browser");
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.getContentPane().add(scrollPane);
    f.setSize(512, 342);
    f.show();
    
  }

}
