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
package io.datalayer.algorithm.astar;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AStar {
	final char[][] grid;
	int startX, startY, goalX, goalY;
	int width, height;
	Map<Integer, Value> OPEN = new HashMap<Integer, Value>();
	Map<Integer, Value> CLOSE = new HashMap<Integer, Value>();
	int[][] step = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	class Value {
		int f;
		int g;
		int h;

		public Value(int f, int g, int h) {
			this.f = f;
			this.g = g;
			this.h = h;
		}

		public void setF(int f) {
			this.f = f;
		}

		public void setG(int g) {
			this.g = g;
		}

		public void setH(int h) {
			this.h = h;
		}
	}

	public AStar(final char[][] grid) {
		if (grid == null || grid.length < 1 || grid[0].length < 1
				|| grid.length * grid[0].length < 2) {
			throw new IllegalArgumentException("grid must be N*M and N*M>=2");
		}

		this.grid = grid;

		width = grid.length;
		height = grid[0].length;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (grid[i][j] == 'S') {
					startX = i;
					startY = j;
				} else if (grid[i][j] == 'G') {
					goalX = i;
					goalY = j;
				}
			}
		}

		OPEN.put(getID(startX, startY), new Value(0, 0, 0));

		astar(startX, startY);
	}

	void astar(int x, int y) {
		System.out.println(x + " " + y);
		if (x == goalX && y == goalY) {
			return;
		}

		int nextX, nextY, id = getID(x, y);
		for (int i = 0; i < 4; i++) {
			nextX = x + step[i][0];
			nextY = y + step[i][1];

			if (isValid(nextX, nextY)) {
				int nextId = getID(nextX, nextY);
				if (CLOSE.containsKey(nextId)) {
					continue;
				}

				int g = OPEN.get(id).g + 1, h, f;
				Value value = OPEN.get(nextId);

				if (value == null) {
					h = heristic(nextX, nextY);
					f = g + h;
					OPEN.put(nextId, new Value(f, g, h));
				} else if (g < value.g) {
					value.setG(g);
					value.setF(g + value.h);
				}
			}
		}
		CLOSE.put(id, OPEN.remove(id));

		int maxF = Integer.MAX_VALUE;
		int nextPoint = -1;
		for (Entry<Integer, Value> entry : OPEN.entrySet()) {
			if (entry.getValue().f < maxF) {
				maxF = entry.getValue().f;
				nextPoint = entry.getKey();
			}
		}

		if (nextPoint != -1) {
			astar(nextPoint / height, nextPoint % height);
		}
	}

	private int heristic(int nextX, int nextY) {
		return Math.abs(goalX - nextX) + Math.abs(goalY - nextY);
	}

	private int getID(int x, int y) {
		return x * height + y;
	}

	private boolean isValid(int nextX, int nextY) {
		if (nextX >= 0 && nextX < width && nextY >= 0 && nextY < height
				&& grid[nextX][nextY] != 'X') {
			return true;
		}
		return false;
	}

	public static void main(final String[] args) throws Exception {
		char[][] grid = new char[4][40];
		grid[0] = "........................................".toCharArray();
		grid[1] = "S......................................G".toCharArray();
		grid[2] = "........................................".toCharArray();
		grid[3] = "........................................".toCharArray();

		new AStar(grid);
	}

}