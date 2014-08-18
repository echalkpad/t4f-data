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
import java.io.*;
import java.awt.*;

public class OReillyHomePage {

  public static void main(String... args) {
        
     JEditorPane jep = new JEditorPane();
     jep.setEditable(false);   
     
     try {
       jep.setPage("http://www.oreilly.com");
     }
     catch (IOException e) {
       jep.setContentType("text/html");
       jep.setText("<html>Could not load http://www.oreilly.com </html>");
     } 
      
     JScrollPane scrollPane = new JScrollPane(jep);     
     JFrame f = new JFrame("O'Reilly & Associates");
     // Next line requires Java 1.3
     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     f.setContentPane(scrollPane);
     f.setSize(512, 342);
     f.show();
    
  }

}
