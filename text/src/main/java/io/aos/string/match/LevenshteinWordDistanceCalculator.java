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
package io.aos.string.match;

/**
 * Determines the similarity between words using the Levenshtein Word Distance algorithm.
 *
 */
public class LevenshteinWordDistanceCalculator {
    /** The default instance of the class. */
    public static final LevenshteinWordDistanceCalculator DEFAULT = new LevenshteinWordDistanceCalculator(1, 1, 1);

    /** The unit cost of performing a substitution. */
    private final int _costOfSubstitution;

    /** The unit cost of performing a deletion. */
    private final int _costOfDeletion;

    /** The unit cost of performing an insertion. */
    private final int _costOfInsertion;

    /**
     * Constructor.
     *
     * @param costOfSubstitution The unit cost of performing a substitution.
     * @param costOfDeletion The unit cost of performing a deletion.
     * @param costOfInsertion The unit cost of performing an insertion.
     */
    public LevenshteinWordDistanceCalculator(int costOfSubstitution, int costOfDeletion, int costOfInsertion) {
        assert costOfSubstitution >= 0 : "costOfSubstitution can't be < 0";
        assert costOfDeletion >= 0 : "costOfDeletion can't be < 0";
        assert costOfInsertion >= 0 : "costOfInsertion can't be < 0";

        _costOfSubstitution = costOfSubstitution;
        _costOfDeletion = costOfDeletion;
        _costOfInsertion = costOfInsertion;
    }

    public int calculate(CharSequence source, CharSequence target) {
        assert source != null : "source can't be null";
        assert target != null : "target can't be null";

        int sourceLength = source.length();
        int targetLength = target.length();

        int[][] grid = new int[sourceLength + 1][targetLength + 1];

        grid[0][0] = 0;

        for (int row = 1; row <= sourceLength; ++row) {
            grid[row][0] = row;
        }

        for (int col = 1; col <= targetLength; ++col) {
            grid[0][col] = col;
        }

        for (int row = 1; row <= sourceLength; ++row) {
            for (int col = 1; col <= targetLength; ++col) {
                grid[row][col] = minCost(source, target, grid, row, col);
            }
        }

        return grid[sourceLength][targetLength];
    }

    private int minCost(CharSequence source, CharSequence target, int[][] grid, int row, int col) {
        return min(
                substitutionCost(source, target, grid, row, col),
                deleteCost(grid, row, col),
                insertCost(grid, row, col)
        );
    }

    private int substitutionCost(CharSequence source, CharSequence target, int[][] grid, int row, int col) {
        int cost = 0;
        if (source.charAt(row - 1) != target.charAt(col - 1)) {
            cost = _costOfSubstitution;
        }
        return grid[row - 1][col - 1] + cost;
    }

    private int deleteCost(int[][] grid, int row, int col) {
        return grid[row - 1][col] + _costOfDeletion;
    }

    private int insertCost(int[][] grid, int row, int col) {
        return grid[row][col - 1] + _costOfInsertion;
    }

    private static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
}
