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

import java.util.ArrayList;
import java.util.List;


public class Magasin1 {

     List listeClients;
    
    public Magasin1() {        
        listeClients = new ArrayList();
        simulate();
    }
    
    public void simulate() {
        
        MagasinClient1 a = new MagasinClient1("Hayez", "Francois");
        MagasinClient1 b = new MagasinClient1("Charles", "Eric");
        MagasinClient1 c = new MagasinClient1("Charles", "Eric2");
        MagasinClient1 d = new MagasinClient1("Claus", "Cedric");
        
        a.buyFor(5);
        b.buyFor(10000000);
        c.buyFor(44);
        d.buyFor(45);
        
        listeClients.add(a);
        listeClients.add(b);
        listeClients.add(c);
        listeClients.add(d);
        
        MagasinClient1 e = new MagasinClient1("Claus", "Cedric");        
        System.out.println(this.alreadyRegistered(e)); 
        
        System.out.println(listeClients);

    }
    
    public boolean alreadyRegistered(MagasinClient1 client) {
        return listeClients.contains(client);
    }
    
    public static void main(String... args) {
        new Magasin1();
    }

}
