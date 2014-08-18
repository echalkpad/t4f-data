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
package io.datalayer.data.tstree;

import io.datalayer.data.list.List;

/**
 * Stores words where each character is represented as a node in a tree.
 *
 */
public class TernarySearchTree {
    /** Special value that matches any matches any single character during a pattern search. */
    public static final char WILDCARD = '?';

    /** The root node; or <code>null</code> if the tree is empty. */
    private Node _root;

    /**
     * Adds a word.
     *
     * @param word The word to insert.
     */
    public void add(CharSequence word) {
        assert word != null : "word can't be null";
        assert word.length() > 0 : "word can't be empty";

        Node node = insert(_root, word, 0);
        if (_root == null) {
            _root = node;
        }
    }

    /**
     * Determines if the tree contains a specified word.
     *
     * @param word The word for which to search.
     * @return <code>true</code> if the word is contained; otherwise <code>false</code>.
     */
    public boolean contains(CharSequence word) {
        assert word != null : "word can't be null";
        assert word.length() > 0 : "word can't be empty";

        Node node = search(_root, word, 0);
        return node != null && node.isEndOfWord();
    }

    /**
     * Searches for all words matching a pattern, optionally containing {@link #WILDCARD} characters.
     *
     * @param pattern The pattern for which to search.
     * @param results A list into which any matching words will added.
     */
    public void patternMatch(CharSequence pattern, List results) {
        assert pattern != null : "pattern can't be null";
        assert pattern.length() > 0 : "pattern can't be empty";
        assert results != null : "results can't be null";

        patternMatch(_root, pattern, 0, results);
    }

    /**
     * Searches for all words with a specified prefix.
     *
     * @param prefix The prefix to use.
     * @param results A list into which any matching words will added.
     */
    public void prefixSearch(CharSequence prefix, List results) {
        assert prefix != null : "prefix can't be null";
        assert prefix.length() > 0 : "prefix can't be empty";

        inOrderTraversal(search(_root, prefix, 0), results);
    }

    private Node search(Node node, CharSequence word, int index) {
        assert word != null : "word can't be null";

        Node result = node;

        if (node == null) {
            return null;
        }

        char c = word.charAt(index);

        if (c == node.getChar()) {
            if (index + 1 < word.length()) {
                result = search(node.getChild(), word, index + 1);
            }
        } else if (c < node.getChar()) {
            result = search(node.getSmaller(), word, index);
        } else {
            result = search(node.getLarger(), word, index);
        }

        return result;
    }

    private Node insert(Node node, CharSequence word, int index) {
        assert word != null : "word can't be null";

        char c = word.charAt(index);

        if (node == null) {
            return insert(new Node(c), word, index);
        }

        if (c == node.getChar()) {
            if (index + 1 < word.length()) {
                node.setChild(insert(node.getChild(), word, index + 1));
            } else {
                node.setWord(word.toString());
            }
        } else if (c < node.getChar()) {
            node.setSmaller(insert(node.getSmaller(), word, index));
        } else {
            node.setLarger(insert(node.getLarger(), word, index));
        }

        return node;
    }

    private void patternMatch(Node node, CharSequence pattern, int index, List results) {
        assert pattern != null : "pattern can't be null";
        assert results != null : "results can't be null";

        if (node == null) {
            return;
        }

        char c = pattern.charAt(index);

        if (c == WILDCARD || c < node.getChar()) {
            patternMatch(node.getSmaller(), pattern, index, results);
        }

        if (c == WILDCARD || c == node.getChar()) {
            if (index + 1 < pattern.length()) {
                patternMatch(node.getChild(), pattern, index + 1, results);
            } else if (node.isEndOfWord()) {
                results.add(node.getWord());
            }
        }

        if (c == WILDCARD || c > node.getChar()) {
            patternMatch(node.getLarger(), pattern, index, results);
        }
    }

    private void inOrderTraversal(Node node, List results) {
        assert results != null : "results can't be null";

        if (node == null) {
            return;
        }

        inOrderTraversal(node.getSmaller(), results);
        if (node.isEndOfWord()) {
            results.add(node.getWord());
        }
        inOrderTraversal(node.getChild(), results);
        inOrderTraversal(node.getLarger(), results);
    }

    /**
     * A node in a {@link TernarySearchTree}.
     *
     */
    private static final class Node {
        /** The character. */
        private final char _c;

        /** The smaller node (if any). */
        private Node _smaller;

        /** The large node (if any). */
        private Node _larger;

        /** The child node (if any). */
        private Node _child;

        /** The whole string if this node marks the end of a word. */
        private String _word;

        public Node(char c) {
            _c = c;
        }

        public char getChar() {
            return _c;
        }

        public Node getSmaller() {
            return _smaller;
        }

        public void setSmaller(Node smaller) {
            _smaller = smaller;
        }

        public Node getLarger() {
            return _larger;
        }

        public void setLarger(Node larger) {
            _larger = larger;
        }

        public Node getChild() {
            return _child;
        }

        public void setChild(Node child) {
            _child = child;
        }

        public String getWord() {
            return _word;
        }

        public void setWord(String word) {
            _word = word;
        }

        public boolean isEndOfWord() {
            return getWord() != null;
        }
    }
}
