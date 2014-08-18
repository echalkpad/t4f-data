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
package aos.lucene.search.ext.sorting;

import org.apache.lucene.search.SortField;
import org.apache.lucene.search.FieldComparatorSource;
import org.apache.lucene.search.FieldComparator;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.Term;

import java.io.IOException;

// From chapter 6
public class DistanceComparatorSource
  extends FieldComparatorSource {                 //
  private int x;
  private int y;

  public DistanceComparatorSource(int x, int y) { //
    this.x = x;
    this.y = y;
  }

  public FieldComparator newComparator(java.lang.String fieldName,   //
                                       int numHits, int sortPos,   //
                                       boolean reversed)   //
    throws IOException {       //
    return new DistanceScoreDocLookupComparator(fieldName,
                                                numHits);
  }

  private class DistanceScoreDocLookupComparator  //
      extends FieldComparator {
    private int[] xDoc, yDoc;                     //
    private float[] values;                       //
    private float bottom;                         //
    String fieldName;

    public DistanceScoreDocLookupComparator(
                  String fieldName, int numHits) throws IOException {
      values = new float[numHits];
      this.fieldName = fieldName;
    }

    public void setNextReader(IndexReader reader, int docBase) throws IOException {
      xDoc = FieldCache.DEFAULT.getInts(reader, "x");  //
      yDoc = FieldCache.DEFAULT.getInts(reader, "y");  //
    }

    private float getDistance(int doc) {              //
      int deltax = xDoc[doc] - x;                     //
      int deltay = yDoc[doc] - y;                     //
      return (float) Math.sqrt(deltax * deltax + deltay * deltay); //
    }

    public int compare(int slot1, int slot2) {          //0
      if (values[slot1] < values[slot2]) return -1;     //0
      if (values[slot1] > values[slot2]) return 1;      //0
      return 0;                                         //0
    }

    public void setBottom(int slot) {                   //1
      bottom = values[slot];
    }

    public int compareBottom(int doc) {                 //2
      float docDistance = getDistance(doc);
      if (bottom < docDistance) return -1;              //2
      if (bottom > docDistance) return 1;               //2
      return 0;                                         //2
    }

    public void copy(int slot, int doc) {               //3
      values[slot] = getDistance(doc);                  //3
    }

    public Comparable value(int slot) {                 //4
      return new Float(values[slot]);                   //4
    }                                                   //4

    public int sortType() {
      return SortField.CUSTOM;
    }
  }

  public String toString() {
    return "Distance from ("+x+","+y+")";
  }
}

/*
#1 Extend FieldComparatorSource
#2 Give constructor base location
#3 Create comparator
#4 FieldComparator implementation
#5 Array of x, y per document
#6 Distances for documents in the queue
#7 Worst distance in the queue
#8 Get x, y values from field cache
#9 Compute distance for one document
#10 Compare two docs in the top N
#11 Record worst scoring doc in the top N
#12 Compare new doc to worst scoring doc
#13 Insert new doc into top N
#14 Extract value from top N
*/

