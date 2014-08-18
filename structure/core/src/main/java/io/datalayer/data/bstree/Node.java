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
package io.datalayer.data.bstree;

/**
 * A node in a {@link BinarySearchTree}. Holds a value, a reference to a smaller child, larger child and parent nodes.
 *
 */
public class Node implements Cloneable {
    /** The value. */
    private Object _value;

    /** The parent; or <code>null</code>. */
    private Node _parent;

    /** The smaller child; or <code>null</code>. */
    private Node _smaller;

    /** The larger child; or <code>null</code>. */
    private Node _larger;

    /**
     * Constructor. Sets the smaller and larger nodes to <code>null</code>.
     *
     * @param value The contained value.
     */
    public Node(Object value) {
        this(value, null, null);
    }

    /**
     * Constructor. Fixes parent references for smaller and larger nodes if non-<code>null</code>.
     *
     * @param value The contained value.
     * @param smaller The smaller child; or <code>null</code>.
     * @param larger The larger child; or <code>null</code>.
     */
    public Node(Object value, Node smaller, Node larger) {
        setValue(value);
        setSmaller(smaller);
        setLarger(larger);

        if (smaller != null) {
            smaller.setParent(this);
        }

        if (larger != null) {
            larger.setParent(this);
        }
    }

    /**
     * Obtains the value.
     *
     * @return The value.
     */
    public Object getValue() {
        return _value;
    }

    /**
     * Sets the value.
     *
     * @param value The value.
     */
    public void setValue(Object value) {
        assert value != null : "value can't be null";
        _value = value;
    }

    /**
     * Obtains the parent.
     *
     * @return the parent; or <code>null</code>.
     */
    public Node getParent() {
        return _parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent The parent; or <code>null</code>.
     */
    public void setParent(Node parent) {
        _parent = parent;
    }

    /**
     * Obtains the smaller child.
     *
     * @return The smaller child; or <code>null</code>.
     */
    public Node getSmaller() {
        return _smaller;
    }

    /**
     * Sets the smaller child. Updates the new smaller child to reflect that this is now its parent. Also updates any
     * exisiting smaller child to reflect that this is no longer its parent.
     *
     * @param smaller The new smaller child.
     */
    public void setSmaller(Node smaller) {
        assert smaller != getLarger() : "smaller can't be the same as larger";
        _smaller = smaller;
    }

    /**
     * Obtains the larger child.
     *
     * @return the larger child; or <code>null</code>.
     */
    public Node getLarger() {
        return _larger;
    }


    /**
     * Sets the larger child. Updates the new larger child to reflect that this is now its parent. Also updates any
     * exisiting larger child to reflect that this is no longer its parent.
     *
     * @param larger The new larger child.
     */
    public void setLarger(Node larger) {
        assert larger != getSmaller() : "larger can't be the same as smaller";
        _larger = larger;
    }

    /**
     * Determines if this is the smaller child of its parent.
     *
     * @return <code>true</code> if this is the smaller child of it's parent; otherwise <code>false</code>.
     */
    public boolean isSmaller() {
        return getParent() != null && this == getParent().getSmaller();
    }

    /**
     * Determines if this is the larger child of its parent.
     *
     * @return <code>true</code> if this is the larger child of it's parent; otherwise <code>false</code>.
     */
    public boolean isLarger() {
        return getParent() != null && this == getParent().getLarger();
    }

    /**
     * Obtains the node with the smallest value starting from this.
     *
     * @return The minimum.
     */
    public Node minimum() {
        Node node = this;

        while (node.getSmaller() != null) {
            node = node.getSmaller();
        }

        return node;
    }

    /**
     * Obtains the node with the largest value starting from this.
     *
     * @return The maximum.
     */
    public Node maximum() {
        Node node = this;

        while (node.getLarger() != null) {
            node = node.getLarger();
        }

        return node;
    }

    /**
     * Obtains the node with the next largest value starting from this.
     *
     * @return The successor; or <code>null</code>.
     */
    public Node successor() {
        if (getLarger() != null) {
            return getLarger().minimum();
        }

        Node node = this;

        while (node.isLarger()) {
            node = node.getParent();
        }

        return node.getParent();
    }

    /**
     * Obtains the node with the next smallest value starting from this.
     *
     * @return The predecessor; or <code>null</code>.
     */
    public Node predecessor() {
        if (getSmaller() != null) {
            return getSmaller().maximum();
        }

        Node node = this;

        while (node.isSmaller()) {
            node = node.getParent();
        }

        return node.getParent();
    }

    /**
     * Obtains the size of the tree starting from this.
     *
     * @return The number of nodes.
     */
    public int size() {
        return size(this);
    }

    /**
     * Obtains the height of the tree starting from this.
     *
     * @return The longest path.
     */
    public int height() {
        return height(this) - 1;
    }

    public int hashCode() {
        return hashCode(this);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        Node other = (Node) object;

        return getValue().equals(other.getValue())
                && equalsSmaller(other.getSmaller())
                && equalsLarger(other.getLarger());
    }

    public String toString() {
        return getValue().toString();
    }

    public Object clone() {
        Node clone = new Node(getValue());

        if (getSmaller() != null) {
            clone.setSmaller((Node) getSmaller().clone());
        }

        if (getLarger() != null) {
            clone.setLarger((Node) getLarger().clone());
        }

        return clone;
    }

    /**
     * Recursively calculates the size of the tree starting from this node.
     *
     * @param node The node at which to start.
     * @return The number of nodes in the tree.
     */
    private int size(Node node) {
        if (node == null) {
            return 0;
        }

        return 1 + size(node.getSmaller()) + size(node.getLarger());
    }

    /**
     * Recursively calculates the height of the tree starting from this node.
     *
     * @param node The node at which to start.
     * @return The longest path.
     */
    private int height(Node node) {
        if (node == null) {
            return 0;
        }

        return 1 + Math.max(height(node.getSmaller()), height(node.getLarger()));
    }

    /**
     * Recursively calculates the hash code of the tree starting from this node.
     *
     * @param node The node at which to start.
     * @return The number of nodes in the tree.
     */
    private int hashCode(Node node) {
        if (node == null) {
            return 0;
        }

        return getValue().hashCode() ^ hashCode(node.getSmaller()) ^ hashCode(node.getLarger());
    }

    /**
     * Recursively determines if the smaller node is equal to another.
     *
     * @param other The othe node with which to compare.
     * @return <code>true</code> if equal; otherwise <code>false</code>.
     */
    private boolean equalsSmaller(Node other) {
        return getSmaller() == null && other == null || getSmaller() != null && getSmaller().equals(other);
    }

    /**
     * Recursively determines if the larger node is equal to another.
     *
     * @param other The othe node with which to compare.
     * @return <code>true</code> if equal; otherwise <code>false</code>.
     */
    private boolean equalsLarger(Node other) {
        return getLarger() == null && other == null || getLarger() != null && getLarger().equals(other);
    }
}
