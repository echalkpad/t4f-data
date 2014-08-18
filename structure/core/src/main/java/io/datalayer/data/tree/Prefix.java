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
package io.datalayer.data.tree;

public enum Prefix {
    // These are the values of this enumerated type.
    // Each one is followed by constructor arguments in parentheses.
    // The values are separated from each other by commas, and the 
    // list of values is terminated with a semicolon to separate it from
    // the class body that follows.
    MILLI("m",    .001),
    CENTI("c",    .01),
    DECI("d",     .1),
    DECA("D",   10.0),
    HECTA("h", 100.0),
    KILO("k", 1000.0);  // Note semicolon


    // This is the constructor invoked for each value above.
    Prefix(String abbrev, double multiplier) {
        this.abbrev = abbrev;
        this.multiplier = multiplier;
    }

    // These are the private fields set by the constructor
    private String abbrev;
    private double multiplier;

    // These are accessor methods for the fields.  They are instance methods
    // of each value of the enumerated type.
    public String abbrev() { return abbrev; }
    public double multiplier() { return multiplier; }
}
