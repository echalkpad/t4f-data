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
/*
 * Created on 16 f�vr. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package io.datalayer.data.spl1;

import java.util.*;


public class Magasin3 {

    // AJOUT
    // Changement de structure de donn�e
    // On va utiliser un treeset pour que les �l�ments se classent "naturellement"
    Set listeClients;
    
    // Constructeur
    // Appel� automatiquement lors de l'instanciation de l'objet via new
    public Magasin3() {        
        
        // AJOUT
        // Initialisation de l'ensemble de donn�es
        listeClients = new TreeSet();
        
        // Lancement de la "simulation"
        simulate();
    }
    
    // M�thode de "simulation"
    // On cr�e manuellement une situation
    public void simulate() {
        
        // Cr�ation de 4 objects clients
        // On r�cup�re la r�f�rence renvoy�e par le new afin de pouvoir leur faire effectuer des achats
        MagasinClient3 a = new MagasinClient3("Hayez", "Fran�ois");
        MagasinClient3 b = new MagasinClient3("Charles", "Eric");
        MagasinClient3 c = new MagasinClient3("Biddau", "Angelo");
        MagasinClient3 d = new MagasinClient3("Claus", "Cedric");
        
        // Achats
        a.buyFor(5);
        b.buyFor(10000000);
        c.buyFor(44);
        d.buyFor(45);
        
        // EXPLICATION SUPPLEMENTAIRE
        // Ajout � la collection de la liste des clients
        // Vu que la classe Client impl�mente l'interface Comparable, l'arbre va s'auto-trier
        // Si l'on avait pas impl�menter l'interface, une exception se serait d�clench�e lors du premier ajout
        listeClients.add(a);
        listeClients.add(b);
        listeClients.add(c);
        listeClients.add(d);
        
        // Affichage de la liste des clients
        // System.out.println fait appel � la m�thode toString de la classe de l'objet
        // Dans le cas pr�sent, la m�thode toString des listes est pr�vue pour appeler
        // r�cursivement la m�thode toString de ses �l�ments
        System.out.println("Liste tri�e naturellement ");
        System.out.println(listeClients);
        
    }
    
    // M�thode testant si un client a d�j� �t� ajout� dans la liste ou non
    public boolean alreadyRegistered(MagasinClient3 client) {
        // On va faire appel � la m�thode contains contenue dans javautil.Collections
        // Cette m�thode fait appel � la m�thode equals des objets
        return listeClients.contains(client);
    }
    
    // Lancement du programme
    public static void main(String... args) {
        // Cr�ation d'un objet de type magasin
        new Magasin3();
    }
}
