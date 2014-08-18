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
// Graph, implemented with an adjacency matrix
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;
import java.util.Iterator;

/**
 * A class implementing a directed, adjacency-matrix based graph.
 * User must commit to upper-bound on number of vertices.
 *
 * @version $Id: GraphMatrixDirected.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey and kimberly tabtiang
 * @see GraphMatrix
 * @see GraphMatrixUndirected
 * @see GraphListDirected
 */
public class GraphMatrixDirected extends GraphMatrix
{
    /**
     * Construct a directed, adjacency-matrix based graph.
     *
     * @pre size > 0
     * @post constructs an empty graph that may be expanded to
     *       at most size vertices.  Graph is directed if dir true
     *       and undirected otherwise
     * 
     * @param size The maximum number of vertices allowed in graph.
     */
    public GraphMatrixDirected(int size)
    {
        super(size,true);
    }

    /**
     * Add an edge between two vertices within the graph.  Edge is directed.
     * Duplicate edges are silently replaced.
     * Labels on edges may be null.
     *
     * @pre vLabel1 and vLabel2 are labels of existing vertices
     * @post an edge is inserted between vLabel1 and vLabel2;
     *       if edge is new, it is labeled with label (can be null)
     * 
     * @param vLabel1 Source vertex.
     * @param vLabel2 Destination vertex.
     * @param label Label associated with the edge.
     */
    public void addEdge(Object vLabel1, Object vLabel2, Object label)
    {
        GraphMatrixVertex vtx1,vtx2;
        // get vertices
        vtx1 = (GraphMatrixVertex) dict.get(vLabel1);
        vtx2 = (GraphMatrixVertex) dict.get(vLabel2);
        // update matrix with new edge
        Edge e = new Edge(vtx1.label(), vtx2.label(), label, true);
        data[vtx1.index()][vtx2.index()] = e;
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
        // get indices
        int row = ((GraphMatrixVertex)dict.get(vLabel1)).index();
        int col = ((GraphMatrixVertex)dict.get(vLabel2)).index();
        // cache old value
        Edge e = data[row][col];
        // update matrix
        data[row][col] = null;
        if (e == null) return null;
        else return e.label(); // return old value
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
        // count non-null entries in table
        int sum = 0;                
        for (int row=0; row<size; row++) 
            for (int col=0; col<size; col++)
                if (data[row][col] != null) sum++;
        return sum;
    }
          
    /**
     * Construct an traversal over all edges.
     * edge is considered exactly once.  Order is not guaranteed.
     *
     * @post returns traversal across all edges of graph (returns Edges)
     * 
     * @return AbstractIterator over edges.
     */
    public Iterator edges()
    {
        List list = new SinglyLinkedList();
        for (int row=size-1; row>=0; row--) 
            for (int col=size-1; col >= 0; col--) {
                Edge e = data[row][col];
                if (e != null) list.add(e);
            }
        return list.iterator();
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
        StringBuffer s = new StringBuffer();
        Iterator source = iterator();
        Iterator dest;

        s.append("<GraphMatrixDirected:");
        while (source.hasNext()) {
            Object srcVal = source.next();
            s.append(" ("+srcVal+"->");
            dest = neighbors(srcVal);
            while (dest.hasNext()) {
                s.append(srcVal+"->"+dest.next());
            }
            s.append(")");
        }
        s.append(">");
        return s.toString();
    }
}
