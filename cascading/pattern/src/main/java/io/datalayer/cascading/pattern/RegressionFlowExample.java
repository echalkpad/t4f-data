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
package io.datalayer.cascading.pattern;

import java.io.File;
import java.io.IOException;

import cascading.flow.Flow;
import cascading.flow.FlowDef;
import cascading.flow.local.LocalFlowConnector;
import cascading.pattern.pmml.PMMLPlanner;
import cascading.scheme.local.TextDelimited;
import cascading.tap.SinkMode;
import cascading.tap.Tap;
import cascading.tap.local.FileTap;
import cascading.tuple.TupleEntryIterator;

/**
 *
 */
public class RegressionFlowExample {
    
    public static void main(String[] args) throws Exception {
        new RegressionFlowExample().run();
    }

    public void run() throws IOException {

        Tap inTap = new FileTap( new TextDelimited(true, "\t", "\""), "src/main/data/iris.lm_p.tsv", SinkMode.KEEP);
        PMMLPlanner pmmlPlanner = new PMMLPlanner().setPMMLInput(new File("src/main/data/iris.lm_p.xml")).retainOnlyActiveIncomingFields();
        Tap resultsTap = new FileTap(new TextDelimited(true, "\t", "\""), "target/output/flow/iris-out-1.tsv", SinkMode.REPLACE);

//        Tap inTap = new FileTap(new TextDelimited(true, "\t", "\""), "src/main/data2/in-1.csv", SinkMode.KEEP);
//        PMMLPlanner pmmlPlanner = new PMMLPlanner().setPMMLInput(new File("src/main/data2/model-1.pmml")).retainOnlyActiveIncomingFields();
//        Tap resultsTap = new FileTap(new TextDelimited(true, "\t", "\""), "target/output/flow/out-1.tsv", SinkMode.REPLACE);

        FlowDef flowDef = FlowDef.flowDef().setName("pmml flow").addSource("iris", inTap).addSink("results", resultsTap);

        flowDef.addAssemblyPlanner(pmmlPlanner);

        Flow flow = new LocalFlowConnector().connect(flowDef);

        flow.complete();

        TupleEntryIterator iterator = resultsTap.openForRead(flow.getFlowProcess());

        while (iterator.hasNext())
            System.out.println(iterator.next());

        iterator.close();
    
    }
    
}
