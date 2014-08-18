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
import javax.swing.*;


public class HTMLLabelApplet extends JApplet {

  public void init() {
  
    JLabel theText = new JLabel(
     "<html>"
     + "Hello! This is a multiline label with <b>bold</b> "
     + "and <i>italic</i> text. <P> "
     + "It can use paragraphs, horizontal lines, <hr> "
     + "<font color=red>colors</font> "
     + "and most of the other basic features of HTML 3.2</html>");
   
    this.getContentPane().add(theText);
  
  }

}
