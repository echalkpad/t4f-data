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
package io.datalayer.data.spl1;

import java.util.*;


public class Magasin2 {

     List listeClients;
    
    // Constructeur
    // Appel� automatiquement lors de l'instanciation de l'objet via new
    public Magasin2() {        
        
        // Initialisation de l'ensemble de donn�es
        listeClients = new ArrayList();
        
        // Lancement de la "simulation
        simulate();
    }
    
    // M�thode de "simulation"
    // On cr�e manuellement une situation
    public void simulate() {
        
        // Cr�ation de 4 objects clients
        // On r�cup�re la r�f�rence renvoy�e par le new afin de pouvoir leur faire effectuer des achats
        MagasinClient2 a = new MagasinClient2("Hayez", "Fran�ois");
        MagasinClient2 b = new MagasinClient2("Charles", "Eric");
        MagasinClient2 c = new MagasinClient2("Biddau", "Angelo");
        MagasinClient2 d = new MagasinClient2("Claus", "Cedric");
        
        // Achats
        a.buyFor(5);
        b.buyFor(10000000);
        c.buyFor(44);
        d.buyFor(45);
        
        // Ajout � la collection de la liste des clients
        listeClients.add(a);
        listeClients.add(b);
        listeClients.add(c);
        listeClients.add(d);
        
        // Cr�ation d'un client du m�me nom qu'un de ceux d�fini plus haut
        // But : tester notre m�thode "Already registered"
        MagasinClient2 e = new MagasinClient2("Claus", "Cedric");        
        System.out.println(this.alreadyRegistered(e)); 
        
        // Affichage de la liste des clients
        // System.out.println fait appel � la m�thode toString de la classe de l'objet
        // Dans le cas pr�sent, la m�thode toString des listes est pr�vue pour appeler
        // r�cursivement la m�thode toString de ses �l�ments
        System.out.println("-------- Avant le tri -------");
        System.out.println(listeClients);
        System.out.println("-------- Apr�s le tri -------");
        
        // AJOUT
        // On fait appel � la m�thode statique de tri de java.util.Collections
        // Celle-ci va employer sur chacun des objects de l'ensemble la methode compareTo
        // -> ok car on l'a bien surcharg�e dans la classe Client
        // dans le cas contraire, il aurait seulement compar� les r�f�rences des objets
        Collections.sort(listeClients);
        
        // AJOUT
        // Affichage effectu� via un it�rateur
        Iterator it = listeClients.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
    }
    
    // M�thode testant si un client a d�j� �t� ajout� dans la liste ou non
    public boolean alreadyRegistered(MagasinClient2 client) {
        // On va faire appel � la m�thode contains contenue dans javautil.Collections
        // Cette m�thode fait appel � la m�thode equals des objets
        return listeClients.contains(client);
    }
    
    // Lancement du programme
    public static void main(String... args) {
        // Cr�ation d'un objet de type magasin
        new Magasin2();
    }
}
