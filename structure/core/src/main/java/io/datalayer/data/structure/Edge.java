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
// Generic base class for describing edges in graphs.
// (c) 1998, 2001 duane a. bailey

package io.datalayer.data.structure;

/**
 * A class implementing common edge type among graphs.  This class
 * supports both directed and undirected edges.  Edge may also have
 * visited flags set and cleared.
 *
 * @version $Id: Edge.java 8 2006-08-02 19:03:11Z bailey $
 * @author, 2001 duane a. bailey
 * @see io.datalayer.data.structure.Graph
 */
public class Edge
{
    /**
     * Two element array of vertex labels.
     * When necessary, first element is source.
     */
    protected Object[] vLabel;  // labels of adjacent vertices
    /**
     * Label associated with edge.  May be null.
     */
    protected Object label;     // edge label
    /**
     * Whether or not this edge has been visited.
     */
    protected boolean visited;  // this edge visited
    /**
     * Whether or not this edge is directed.
     */
    protected boolean directed; // this edge directed

    /**
     * Construct a (possibly directed) edge between two labeled
     * vertices.  When edge is directed, vtx1 specifies source.
     * When undirected, order of vertices is unimportant.  Label
     * on edge is any type, and may be null.
     * Edge is initially unvisited.
     *
     * @post edge associates vtx1 and vtx2; labeled with label
     *       directed if "directed" set true
     *
     * @param vtx1 The label of a vertex (source if directed).
     * @param vtx2 The label of another vertex (destination if directed).
     * @param label The label associated with the edge.
     * @param directed True iff this edge is directed.
     */
    public Edge(Object vtx1, Object vtx2, Object label,
                boolean directed)
    {
        vLabel = new Object[2];
        vLabel[0] = vtx1;
        vLabel[1] = vtx2;
        this.label = label;
        visited = false;
        this.directed = directed;
    }

    /**
     * Returns the first vertex (or source if directed).
     *
     * @post returns first node in edge
     * 
     * @return A vertex; if directed, the source.
     */
    public Object here()
    {
        return vLabel[0];
    }

    /**
     * Returns the second vertex (or source if undirected).
     *
     * @post returns second node in edge
     * 
     * @return A vertex; if directed, the destination.
     */
    public Object there()
    {
        return vLabel[1];
    }

    /**
     * Sets the label associated with the edge.  May be null.
     *
     * @post sets label of this edge to label 
     * 
     * @param label Any object to label edge, or null.
     */
    public void setLabel(Object label)
    {
        this.label = label;
    }

    /**
     * Get label associated with edge.
     *
     * @post returns label associated with this edge
     * 
     * @return The label found on the edge.
     */
    public Object label()
    {
        return label;
    }

    /**
     * Test and set visited flag on vertex.
     *
     * @post visits edge, returns whether previously visited
     * 
     * @return True iff edge was visited previously.
     */
    public boolean visit()
    {
        boolean was = visited;
        visited = true;
        return was;
    }

    /**
     * Check to see if edge has been visited.
     *
     * @post returns true iff edge has been visited
     * 
     * @return True iff the edge has been visited.
     */
    public boolean isVisited()
    {
        return visited;
    }

    /**
     * Check to see if edge is directed.
     *
     * @post returns true iff edge is directed
     * 
     * @return True iff the edge has been visited.
     */
    public boolean isDirected()
    {
        return directed;
    }

    /**
     * Clear the visited flag associated with edge.
     *
     * @post resets edge's visited flag to initial state
     */
    public void reset()
    {
        visited = false;
    }

    /**
     * Returns hashcode associated with edge.
     *
     * @post returns suitable hashcode
     * 
     * @return An integer code suitable for hashing.
     */
    public int hashCode()
    {
        if (directed) return here().hashCode()-there().hashCode();
        else          return here().hashCode()^there().hashCode();
    }

    /**
     * Test for equality of edges.  Undirected edges are equal if
     * they connect the same vertices.  Directed edges must have same
     * direction.
     *
     * @post returns true iff edges connect same vertices
     * 
     * @param o The other edge.
     * @return True iff this edge is equal to other edge.
     */
    public boolean equals(Object o)
    {
        Edge e = (Edge)o;
        return ((here().equals(e.here()) && 
                 there().equals(e.there())) ||
                (!directed &&
                 (here().equals(e.there()) && 
                  there().equals(e.here()))));
    }
    
    /**
     * Construct a string representation of edge.
     *
     * @post returns string representation of edge
     * 
     * @return String representing edge.
     */
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("<Edge:");
        if (visited) s.append(" visited");
        s.append(" "+here());
        if (directed) s.append(" <->");
        else s.append("->");
        s.append(" "+there()+">");
        return s.toString();
    }
}
