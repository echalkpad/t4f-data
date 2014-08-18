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
package io.datalayer.algorithm.sort;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class JdkSortTest {

    @Test
    public void testAverage() {
        ArrayList<Long> longList = new ArrayList<Long>();
        int[] arraysNumbers = { 1, 4, 5, 6, 7, 9, 20, 78, 43 };
        int sizeDatas = 6;
        long numbers = 0;
        for (int i = 0; i < arraysNumbers.length; i++) {
            numbers = numbers + arraysNumbers[i];
            System.out.println("xxx=>" + numbers);
            longList.add(Long.valueOf(numbers));
        }
        Collections.sort(longList);
        long average = numbers / sizeDatas;
        System.out.println("Numbers =>" + numbers);
        System.out.println("Average =>" + average);
        for (Long longCurrent : longList) {
            System.out.println("LongCurrent =>" + longCurrent);
        }
    }

}
