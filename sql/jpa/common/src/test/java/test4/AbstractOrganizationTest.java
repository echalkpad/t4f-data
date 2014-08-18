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
package test4;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import junit.framework.TestResult;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AbstractOrganizationTest implements junit.framework.Test {

    @Test
    public void insertValues() {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("test4", System.getProperties());

        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();

        Organization organization = new Organization(" The Apache Software Foundation");

        Project project = new Project("Streaming LOB support (for OpenJPA)");
        organization.addProject(project);

        project = new Project("Maven Dependency Visualization");
        organization.addProject(project);

        em.persist(organization);

        organization = new Organization(" Mono Project");
        project = new Project("Gendarme Tasks");
        organization.addProject(project);

        em.persist(organization);

        em.getTransaction().commit();
        em.close();
        factory.close();
    }

    @Test
    public void readValues() {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("test4", System.getProperties());

        EntityManager em = factory.createEntityManager();

        Query q = em.createQuery("select o from Organization o");

        // for (Organization organization : (List<Organization>)
        // q.getResultList()) {
        // System.out.println("Organization: " + organization.getName());
        // if (organization.getProjects() != null &&
        // organization.getProjects().size() > 0) {
        // for (Project p : organization.getProjects()) {
        // System.out.println("-" + p.getName() );
        // }
        // } else {
        // System.out.println("No proyects yet");
        // }
        // }
        em.close();
        factory.close();
    }

    @Override
    public int countTestCases() {
        return 2;
    }

    @Override
    public void run(TestResult result) {
        insertValues();
        readValues();
    }

}
