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
package camelinaction 

import java.net.ConnectException

import org.apache.camel.builder.RouteBuilder

import akka.actor.Actor._
import akka.actor.Uuid
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE6 extends Application {
  import SampleActors._

  val service = CamelServiceManager.startCamelService
  val producer = actorOf[HttpProducer1].start

  for (context  <- CamelContextManager.context;
       template <- CamelContextManager.template) {
    context.addRoutes(new CustomRoute(producer.uuid))
    template.requestBody("direct:test", "feel good", classOf[String]) match {
      case "<received>feel good</received>" => println("communication ok")
      case "feel bad"                       => println("communication failed")
      case _                                => println("unexpected response")
    }
  }

  service.stop
  producer.stop
}

class CustomRoute(uuid: Uuid) extends RouteBuilder {
  def configure = {
    from("direct:test")
    .onException(classOf[ConnectException])
      .handled(true)
      .transform.constant("feel bad")
      .end
    .to("actor:uuid:%s" format uuid)
  }
}
