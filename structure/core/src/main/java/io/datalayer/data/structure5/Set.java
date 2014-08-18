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
// A simple Set interface.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure5;

/**
 * Implementation of a set of elements.
 * As with the mathematical object, the elements of the set are
 * not duplicated.  No order is implied or enforced in this structure, but
 * simple set operations such as intersection, union, difference, and subset
 * are provided. 
 * <P>
 * Example Usage:
 * Given a list of students who completed a computer science thesis in the
 * 2001-2002 academic year at Williams College and a list of graduating 
 * computer science majors who are continuing on to graduate school, we could
 * determine which thesis students are planning to attend graduate school
 * as follows:
 * <P>
 * <pre>
 * public static void main(String... argv){
 *      //thesis students in the class of '02
 *      String[] thesis = new String[]{"Doug", "Evan", "Feng"};
 *      
 *      //students continuing on to grad school
 *      String[] grad = new String[]{"Doug", "Feng", "Lida"};
 *
 *      //instantiate our sets
 *      Set thesisSet = new {@link io.datalayer.data.structure.SetVector#SetVector()}, 
 *          gradSet = new {@link io.datalayer.data.structure.SetVector#SetVector()};
 *              
 *      //build sets up
 *      for(int i = 0; i < thesis.length; i++) thesisSet.{@link io.datalayer.data.structure.SetVector#add(Object) add(thesis[i])};
 *      for(int i = 0; i < grad.length; i++) gradSet.{@link io.datalayer.data.structure.SetVector#add(Object) add(grad[i])};
 *      
 *      //calculate the intersection of the two sets
 *      thesisSet.{@link io.datalayer.data.structure.SetVector#retainAll(Structure) retainAll(gradSet)};
 *      System.out.println(thesisSet);
 * }
 * </pre>
 *
 * @version $Id: Set.java 22 2006-08-21 19:27:26Z bailey $
 * @author, 2001 duane a. bailey
 */
public interface Set<E> extends Structure<E>
{
    /**
     * Union other set into this set.
     * @pre other is non-null
     * @post values from other are added into this set
     */
    public void addAll(Structure<E> other);

    /**
     * Check to see if this set is contained in the other structure.
     * @pre other is non-null
     * @post returns true if every value in set is in other
     */
    public boolean containsAll(Structure<E> other);

    /**
     * Computes the difference between this set and the other structure
     * @pre other is non-null
     * @post values of this set contained in other are removed
     */
    public void removeAll(Structure<E> other);

    /**
     * Computes the intersection between this set and the other structure.
     * @pre other is non-null
     * @post values not appearing in the other structure are removed
     */
    public void retainAll(Structure<E> other);
}
