/**
 * **************************************************************
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
 * **************************************************************
 */
package io.aos.ebnf.order

import org.junit.Assert.assertTrue
import org.junit.Test

import io.aos.ebnf.order.OrderParser._

@Test
class OrderParserTest {

  @Test
  def testOK() = assertTrue(true)

  @Test
  def test() {
    val dsl =
      "(buy 100 IBM shares at max 45, sell 40 Sun shares at min 24,buy 25 CISCO shares at max 56) for trading account \"A1234\""

    instr(new lexical. Scanner(dsl)) match {
      case Success(ord, _) => processOrder(ord) // ord is a ClientOrder
      case Failure(msg, _) => println(msg)
      case Error(msg, _) => println(msg)
    }
  }

  def processOrder(order: ClientOrder) {
    println(order)
  }

}
