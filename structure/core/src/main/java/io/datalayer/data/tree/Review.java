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

/**
 * An annotation of this type represents a single code review of the
 * annotated element.  Every review must specify the name of the reviewer
 * and the grade assigned to the code.  Optionally, reviews may also include
 * a comment string.
 */
public @interface Review {
    // Nested enumerated type
    public static enum Grade { EXCELLENT, SATISFACTORY, UNSATISFACTORY };

    // These methods define the annotation members
    Grade grade();                // member named "grade" with type Grade
    String reviewer();          
    String comment() default "";  // Note default value here.
}
