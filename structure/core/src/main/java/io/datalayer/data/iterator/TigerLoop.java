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
package io.datalayer.data.iterator;

public class TigerLoop {

    private static Person[] persons;

    public static void main(String... args) {

        createPersons();

        sampleLoopTiger();
        searchLoopTiger();

    }

    private static void sampleLoopTiger() {

        for (Person datasBean : persons) {
            System.out.println(datasBean.toString());
        }

    }

    private static void searchLoopTiger() {

        for (Person person : persons) {

            if (person.getFirstName().equals("Eric")) {
                System.out.println("Found !? Eric ");
            } 
            
            else {
                System.out.println("Eric is not  found.");
            }

        }

    }
    
    private static void createPersons() {

        persons = new Person[2];
        
        persons[0] = new Person();

        persons[0].setAddress("Rue du commerce 185/187");
        persons[0].setFirstName("Eric");
        persons[0].setLastName("Charles");
        persons[0].setZipCity("Dour");
        persons[0].setZipCode(7370);

        persons[1] = new Person();

        persons[1].setAddress("Rue de la fontaine 61");
        persons[1].setFirstName("Angelo");
        persons[1].setLastName("Biddau");
        persons[1].setZipCity("Dour");
        persons[1].setZipCode(7370);

    }

}
