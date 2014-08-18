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

useradd {username}
passwd {username}]

Set account disable date
useradd -e {yyyy-mm-dd} {username}
useradd -e 2008-12-31 jerry

Set default password expiry
useradd -f {days} {username}
useradd -e 2009-12-31 -f 30 {username}

useradd -G {group-name} {username}

groupadd developers
useradd -G developers {username}
