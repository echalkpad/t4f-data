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
package io.datalayer.text.regexp;

public class RegexReplaceTest2 {

    public void test() {
        System.out.println("1. dog=" + new String("Exemple dog ").replaceAll("Exempl. ", ""));
        System.out.println("2. dog=" + new String("Exemple http://dog dfd").replaceAll("://", ""));
        System.out.println("3. dog=" + new String("Exemple dog dfd").replaceAll("\\bdog\\b", ""));
        System.out.println("4. Exemple=" + new String("Exemple dog http://dfq.com/sfdqf dsfqd").replaceAll("http://[a-zA-Z./]*", ""));
        System.out.println("5. Exemple=" + new String("@test Exemple @test dog http://dfq.com/sfdqf dsfqd @ldkjf").replaceAll("@[a-zA-Z./]*", ""));
    }

}
