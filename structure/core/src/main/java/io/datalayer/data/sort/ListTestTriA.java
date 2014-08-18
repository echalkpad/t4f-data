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
package io.datalayer.data.sort;

import java.util.*;

public class ListTestTriA {

    public static void main(String... args) {
        
        // creation de la collection
        Collection coll = new LinkedList();
        
        // Initialisation d'une collection de 100 entiers al�atoires
        // int est un type primitif, mais les collections ne savent traiter que des objets
        // -> utilisation de la classe enveloppante Integer
        // Remarque : random renvoie un double compris entre 0 et 1, une conversion est donc necessaire
        for (int i = 0; i < 100; i++)
            coll.add(new Integer((int)(1000 * Math.random())));
        
        // Les classes enveloppantes impl�mentent l'interface comparable -> ok pour �tre tri�es 
        // Remarque : sort n'accepte que le type List g�n�ral -> conversion de type
        Collections.sort((List)coll);
                
        // Affichage ok car la m�thode toString est bien surcharg�e par les diff�rentes classes enveloppantes
        System.out.println(coll);
    }
} 
