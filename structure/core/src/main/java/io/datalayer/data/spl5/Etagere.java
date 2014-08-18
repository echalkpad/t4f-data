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



import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Administrateur
 * Date: May 20, 2003
 * Time: 9:39:39 PM
 * To change this template use Options | File Templates.
 */
public class Etagere {

    private Collection livres;

    public Etagere() {
        this.livres =new Vector();
    }

    public void ajouter(Livre livre){
        livres.add(livre);
    }

    public Collection getLivres() {
        return livres;
    }

    public Collection getLivresFromAuteur(String auteurName){
        Collection result = new Vector();
        Iterator iterate = livres.iterator();
        while(iterate.hasNext()){
            Livre current = (Livre) iterate.next();
            if(current.getTitre().equalsIgnoreCase(auteurName)){
                result.add(current);
            }
        }
        return result;
    }

    public boolean estDansMonEtagere(Livre livre){
        Iterator iterate = livres.iterator();

        while(iterate.hasNext()){
            Livre current = (Livre) iterate.next();
            if(livre.equals(current)) return true;
        }
        return false;

    }

    public Livre getFirst() throws NoMoreBookException{
        if(livres.isEmpty()){
            throw new NoMoreBookException("no more books....");
        }  else{LinkedList list = new LinkedList(livres);
            Livre first = (Livre)list.getFirst();
            livres = new Vector(list);
            return first ;

    }    }

}
