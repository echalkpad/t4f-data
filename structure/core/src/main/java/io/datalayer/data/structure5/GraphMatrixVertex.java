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
package io.datalayer.data.structure5;
/**
 * A private implementation of a vertex for use in graphs that
 * are internally represented as a Matrix.  A vertex
 * is capable of holding a label and has a flag that can be set  
 * to mark it as visited.   
 * <P>
 * Typical Usage:
 * <P>
 * <pre>
 *     Vertex v = new {@link #GraphMatrixVertex(Object, int) Vertex(someLabel)};
 *     //...several graph related operations occur
 *     if(!v.{@link #isVisited() isVisited()}){
 *         Object label = v.label();
 *         v.{@link #visit() visit()};
 *     }
 * </pre>
 * @see GraphListVertex
 * @see Vertex
 * @version $Id: GraphMatrixVertex.java 22 2006-08-21 19:27:26Z bailey $
 * @author, 2001 duane a. bailey
 */
class GraphMatrixVertex<V> extends Vertex<V>
{
    protected int index;

    /**
     * @post constructs a new augmented vertex
     * 
     * @param label 
     * @param idx 
     */
    public GraphMatrixVertex(V label, int idx)
    {
        super(label);
        index = idx;
    }

    /**
     * @post returns index associated with vertex
     * 
     * @return 
     */
    public int index()
    {
        return index;
    }

    /**
     * @post returns string representation of vertex
     * 
     * @return 
     */
    public String toString()
    {
        return "<GraphMatrixVertex: "+label()+">";
    }
}
