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
// Graph, implemented with an adjacency list
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;
/**
 * A class implementing a directed, adjacency-list based graph.
 *
 * @version $Id: GraphListDirected.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey and kimberly tabtiang
 * @see GraphList
 * @see GraphListUndirected
 * @see GraphMatrixDirected
 */
public class GraphListDirected extends GraphList
{

    /**
     * Construct a directed, adjacency-list based graph.
     *
     * @post constructs an directed graph
     */
    public GraphListDirected()
    {
        super(true);
    }

    /**
     * Add an edge between two vertices within the graph.  Edge is directed.
     * Duplicate edges are silently replaced.
     * Labels on edges may be null.
     *
     * @pre vLabel1 and vLabel2 are labels of existing vertices, v1 & v2
     * @post an edge is inserted between v1 and v2;
     *       if edge is new, it is labeled with label (can be null)
     * 
     * @param vLabel1 Source vertex.
     * @param vLabel2 Destination vertex.
     * @param label Label associated with the edge.
     */
    public void addEdge(Object vLabel1, Object vLabel2, Object label)
    {
        GraphListVertex v1 = (GraphListVertex) dict.get(vLabel1);
        GraphListVertex v2 = (GraphListVertex) dict.get(vLabel2);
        Edge e = new Edge(v1.label(), v2.label(), label, true);
        v1.addEdge(e);
    }

    /**
     * Remove a vertex from the graph.  Associated edges are also 
     * removed.  Non-vertices are silently ignored.
     *
     * @pre label is non-null vertex label
     * @post vertex with "equals" label is removed, if found
     * 
     * @param label The label of the vertex within the graph.
     * @return The label associated with the vertex.
     */
    public Object remove(Object label)
    {
        GraphListVertex v = (GraphListVertex)dict.get(label);

        Iterator vi = iterator();
        while (vi.hasNext())
        {
            Object v2 = vi.next();
            if (!label.equals(v2)) removeEdge(v2,label);
        }
        dict.remove(label);
        return v.label();
    }

    /**
     * Remove possible edge between vertices labeled vLabel1 and vLabel2.
     * vLabel1 is the source.
     *
     * @pre vLabel1 and vLabel2 are labels of existing vertices
     * @post edge is removed, its label is returned
     * 
     * @param vLabel1 Source vertex.
     * @param vLabel2 Destination vertex.
     * @return The label associated with the edge removed.
     */
    public Object removeEdge(Object vLabel1, Object vLabel2)  
    {
        GraphListVertex v1 = (GraphListVertex) dict.get(vLabel1);
        GraphListVertex v2 = (GraphListVertex) dict.get(vLabel2);
        Edge e = new Edge(v1.label(), v2.label(), null, true);
        e = v1.removeEdge(e);
        if (e == null) return null;
        else return e.label();
    }

    /**
     * Determine the number of edges in graph.
     *
     * @post returns the number of edges in graph
     * 
     * @return Number of edges in graph.
     */
    public int edgeCount()
    {
        int count = 0;
        Iterator i = dict.values().iterator();
        while (i.hasNext())
            count += ((GraphListVertex) i.next()).degree();
        return count;
    }

    /**
     * Construct a string representation of graph.
     *
     * @post returns string representation of graph
     * 
     * @return String representing graph.
     */
    public String toString()
    {

        return "<GraphListDirected: "+dict.toString()+">";
    }
}
