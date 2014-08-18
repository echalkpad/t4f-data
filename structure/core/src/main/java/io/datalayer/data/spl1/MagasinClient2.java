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

// Pour que l'objet soit classable, on doit impl�menter l'interface Comparable
public class MagasinClient2 implements Comparable{
    
    // Un client se caract�rise par son nom et son pr�nom
    private String nom;
    private String prenom;
    private int achats;
    
    // Constructeur
    // Appel� automatiquement lors de l'instanciation de l'objet via new
    public MagasinClient2 (String nom, String prenom) {
        
        // Le mot cl� this fait r�f�rence aux variables propres � l'objet
        this.nom=nom;
        this.prenom=prenom;
        achats=0;
    }
    
    // Effectuer un achat
    public void buyFor(int note) {
        achats+=note;
    }
    
    // Obtenir le montant total des achats
    public int getValue() {
        return this.achats;
    }
    
    // Fonction d'acc�s au nom
    // Permet de ne pas laisser en acc�s public la variable,
    // et la prot�ge donc des modifications ext�rieures non d�sir�es
    public String getLastname() {
        return this.nom;
    }
    
    // Fonction d'acc�s au pr�nom
    // Permet de ne pas laisser en acc�s public la variable,
    // et la prot�ge donc des modifications ext�rieures non d�sir�es
    public String getFirstname() {
        return this.prenom;
    }
    
    // Surcharge de la m�thode de la classe Object toString.
    // M�thode appel�e automatiquement par certains fonctionnalit�s de java, 
    // offre une repr�sentation textuelle de l'objet
    // Exemple : println(objet);
    public String toString() {
        return nom+" "+prenom+" a d�pens� "+achats+" dans notre magasin";
    }
    
    // Surcharge de la m�thode de la classe Object equals.
    // M�thode appel�e automatiquement par certains fonctionnalit�s de java,
    // pour d�terminer si deux objets sont �gaux
    //
    // Exemple : la m�thode contains de la classe java.utils.Collections effectue des m�thodes equals sur les objets
    //
    // Faire attention � respecter la syntaxe exacte de la m�thode � surcharger 
    // -> param�tre de type Object et non pas Client
    public boolean equals(Object o) {
        
        // On choisit comme crit�re d'�galit� le fait que le nom et le pr�nom soient �gaux
        
        // 1. Param�tre de type Object -> on caste en Client
        // 2. R�cup�ration des champs noms et pr�noms de l'objet pass� en param�tre
        // 3. L'objet renvoy� est de type String. Or cette classe, ainsi que les autres classes enveloppantes, red�fini la m�thode equals
        // -> appel "r�cursif"
        return ((this.nom.equals(((MagasinClient2)o).getLastname())&&(this.prenom.equals(((MagasinClient2)o).getFirstname()))));
    }
    
    // AJOUT
    // Surcharge de la m�thode la classe Object compareTo
    // 
    // De mani�re analogue � la surcharge de la m�thode equals, on va proc�der par r�cursivit�
    // la m�thode compareTo est en effet impl�ment�e dans les classes enveloppantes
    public int compareTo(Object o) {
        return new Integer(this.achats).compareTo(new Integer(((MagasinClient2)o).getValue()));
    }
}
