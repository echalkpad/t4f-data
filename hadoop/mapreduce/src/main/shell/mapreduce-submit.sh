#!/bin/bash

# Licensed to the AOS Community (AOS) under one or more
# contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The AOS licenses this file
# to you under the Apache License, Version 2.0 (the 
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

###############################################################################
# Run this script from the top folder project with 
# ./src/main/shell/mapreduce-submit.sh <A_FOLDER>
###############################################################################

mvn clean install -DskipTests -o

hadoop fs -mkdir $1
hadoop fs -copyFromLocal ./src/main/data/README $1

hadoop jar ./target/datalayer-hadoop-mapreduce-1.0.0-SNAPSHOT.jar io.datalayer.mapreduce.count.WordCountTool $1 $1_out

hadoop fs -ls $1_out
hadoop fs -copyToLocal $1_out/part-r-00000 $1_part-r-00000
cat ./$1_part-r-00000
