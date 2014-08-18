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


public class Magasin4 {

    List listeClients;
    
    // Constructeur
    // Appel� automatiquement lors de l'instanciation de l'objet via new
    public Magasin4() {        
        
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
        MagasinClient4 a = new MagasinClient4("Hayez", "Fran�ois");
        MagasinClient4 b = new MagasinClient4("Charles", "Eric");
        MagasinClient4 c = new MagasinClient4("Biddau", "Angelo");
        MagasinClient4 d = new MagasinClient4("Claus", "Cedric");
        
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
        MagasinClient4 e = new MagasinClient4("Claus", "Cedric");        
        System.out.println(this.alreadyRegistered(e)); 
        
        // Affichage de la liste des clients
        // System.out.println fait appel � la m�thode toString de la classe de l'objet
        // Dans le cas pr�sent, la m�thode toString des listes est pr�vue pour appeler
        // r�cursivement la m�thode toString de ses �l�ments
        System.out.println("---------- Avant tri");
        System.out.println(listeClients);
        
        // AJOUT
        // Au lieu d'impl�menter l'interface comparable, nous avons choisi une m�thode alternative : 
        // d�finir deux classes impl�mentants toutes deux l'interface Comparator
        //
        // La m�thode sort peut prendre comme param�tre un objet de type comparator, cf ci-dessous
        // On peut ainsi trier successivement la collection selon plusieurs crit�res diff�rents
        // Ici, on trie d'abord par "nom - prenom"
        Collections.sort(listeClients, new ComparatorName());
        System.out.println("---------- Apr�s tri de type nom-prenom");
        Iterator it = listeClients.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        
        // REMARQUE
        // En ce qui concerne les TREESET, un de leur constructeur peut prendre en param un comparator
        // Exemple : 
        // listeClients1 = new TreeSet(new NameComparator());
        // listeClients2 = new TreeSet(new ValueComparator());
        // listeClients2.addAll(listeClients1);
        System.out.println("---------- Apr�s tri de type valeur croissante d'achats");
        Collections.sort(listeClients, new ComparatorValue());
        
        Iterator it2 = listeClients.iterator();
        while (it2.hasNext()) {
            System.out.println(it2.next());
        }
    }
    
    // M�thode testant si un client a d�j� �t� ajout� dans la liste ou non
    public boolean alreadyRegistered(MagasinClient4 client) {
        // On va faire appel � la m�thode contains contenue dans javautil.Collections
        // Cette m�thode fait appel � la m�thode equals des objets
        return listeClients.contains(client);
    }
    
    // Lancement du programme
    public static void main(String... args) {
        // Cr�ation d'un objet de type magasin
        new Magasin4();
    }
}
