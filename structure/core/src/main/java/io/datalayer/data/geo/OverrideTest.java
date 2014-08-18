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
package io.datalayer.data.geo;

class A {                          // Define a class named A
  int i = 1;                       // An instance field
  int f() { return i; }            // An instance method
  static char g() { return 'A'; }  // A class method
}

class B extends A {                // Define a subclass of A
  int i = 2;                       // Hides field i in class A
  int f() { return -i; }           // Overrides instance method f in class A
  static char g() { return 'B'; }  // Hides class method g() in class A
}

public class OverrideTest {
  public static void main(String... args) {
    B b = new B();               // Creates a new object of type B
    System.out.println(b.i);     // Refers to B.i; prints 2
    System.out.println(b.f());   // Refers to B.f(); prints -2
    System.out.println(b.g());   // Refers to B.g(); prints B
    System.out.println(B.g());   // This is a better way to invoke B.g()

    A a = (A) b;                 // Casts b to an instance of class A
    System.out.println(a.i);     // Now refers to A.i; prints 1
    System.out.println(a.f());   // Still refers to B.f(); prints -2
    System.out.println(a.g());   // Refers to A.g(); prints A
    System.out.println(A.g());   // This is a better way to invoke A.g()
  }
}
