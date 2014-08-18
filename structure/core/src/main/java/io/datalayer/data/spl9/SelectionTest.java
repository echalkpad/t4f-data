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
package io.datalayer.data.spl9;

import java.util.*;

public class SelectionTest {
    
    // M�thode renvoyant un sous-ensemble de "source", respectant le crit�re sp�cifi�
    static Collection selection(Collection source, SelectionCriteria critere) {
        
        // Initialisation de la collection r�sultat
        Collection res = new Vector();
        
        // On va it�rer sur chacun des �l�ments de l'ensemble et v�rifier le crit�re
        Iterator iter = source.iterator();
        // Rappel : hasNext() doit toujours pr�c�der next()
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (critere.ok(obj))
                res.add(obj);
        }
        return res; 
    }
    
    // Classe interne impl�mentant l'interface
    static class EtreMultipleDe3 implements SelectionCriteria {
        
        //D�finition de la m�thode ok
        public boolean ok(Object x) {
            
            // Etapes
            // 1. V�rification de la classe de l'objet pour plus de s�curit� 
            // 2. Cast du param�tre de type g�n�ral Object en un Integer
            // 3. R�cup�ration de la valeur primitive (int) de l'objet (Integer)
            // 4. Application de la fonction modulo
            return x instanceof Integer
                && ((Integer) x).intValue() % 3 == 0;
        }
    }
    
    
    public static void main(String... args) {
    
        // Cr�ation de la liste de test
        Collection liste = new LinkedList();
        for (int i = 0; i < 50; i++)
            liste.add(new Integer(i));
        
        // Appel de la fonction de vc�rification d'un crit�re
        // Outre la liste cr�e ci-dessus, on passe en param�tre une instance de la classe impl�mentant CritereSelection
        Collection sousListe = selection(liste, new EtreMultipleDe3());
        
        System.out.println(sousListe);
    }
}

