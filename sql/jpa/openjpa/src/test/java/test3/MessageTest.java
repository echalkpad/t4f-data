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
package test3;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MessageTest {

    @Test
    public void test() {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("test3", System.getProperties());
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();

        // em.createQuery("DELETE FROM Header").executeUpdate();
        // em.createQuery("DELETE FROM Message").executeUpdate();

        Message message1 = new Message("Message 1");
        em.persist(message1);

        Message message2 = new Message("Message 2");
        message2.addHeader(new Header("Header 2.1"));
        message2.addHeader(new Header("Header 2.2"));
        em.persist(message2);

        em.getTransaction().commit();

    }

}
