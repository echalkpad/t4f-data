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
(defproject t4-data-nosql-hadoop-cascalog "1.0-SNAPSHOT"
   :description "T4f Data NoSQL Hadoop Cascalog"
   :dependencies [
     [org.clojure/clojure "1.4.0"]
     [cascalog "1.9.0"]
     [org.apache.hadoop/hadoop-core "0.20.2-dev"]
     [cascalog-checkpoint "0.2.0"]
   ]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]
  :resource-paths ["src/main/resource"]
  :test-paths ["src/test/clojure"]
  :test-java-source-paths ["src/test/java"]
  :test-resource-paths ["src/test/resource"]
  :compile-path "target/classes"
  :jvm-opts ["-Xmx1000m"]
  :main io.aos.cascalog.core
)
