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
package io.datalayer.data.spl5;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: May 20, 2003
 * Time: 9:39:12 PM
 * To change this template use Options | File Templates.
 */
public class Livre {

    private String auteur;
    private String titre;

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livre)) return false;

        final Livre livre = (Livre) o;

        if (!auteur.equals(livre.auteur)) return false;
        if (!titre.equals(livre.titre)) return false;

        return true;
    }

    public String toString() {
        StringBuffer stb = new StringBuffer("Ce Livre, ");
        stb.append(titre);
        stb.append("a �t� �crit par");
        stb.append(auteur);
        return stb.toString();

    }
}
