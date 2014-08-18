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
package io.datalayer.mvel;

import java.io.IOException;

/**
 * Usage example after having launched the console:
 * 
 *   mvel2$ a=10
 *   10
 *   mvel2$ b=20
 *   20
 *   mvel2$ a+b
 *   30
 *   mvel2$ System.out.println("Hello MVEL2!");
 *   Hello MVEL2!
 * 
 */
public class MvelShell {
    
    public static void main(String... args) throws IOException {
        org.mvel2.sh.Main.main(args);
    }

}
