package io.datalayer.lucene.helper;

import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo.IndexOptions;

public class AosFieldType {
    public static final FieldType INDEXED_STORED_TERMVECTORS;
    public static final FieldType INDEXEDNOT_STORED_TERMVECTORSNOT;
    public static final FieldType INDEXED_STOREDNOT_TERMVECTORS;
    public static final FieldType INDEXEDNOT_STOREDNOT_TERMVECTORSNOT;

    static {

        INDEXED_STORED_TERMVECTORS = new FieldType();
        INDEXED_STORED_TERMVECTORS.setIndexed(true);
        INDEXED_STORED_TERMVECTORS.setStored(true);
        INDEXED_STORED_TERMVECTORS.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        INDEXED_STORED_TERMVECTORS.setStoreTermVectors(true);
        INDEXED_STORED_TERMVECTORS.setStoreTermVectorOffsets(true);
        INDEXED_STORED_TERMVECTORS.setStoreTermVectorPayloads(true);
        INDEXED_STORED_TERMVECTORS.setStoreTermVectorPositions(true);
        INDEXED_STORED_TERMVECTORS.freeze();

        INDEXEDNOT_STORED_TERMVECTORSNOT = new FieldType();
        INDEXEDNOT_STORED_TERMVECTORSNOT.setIndexed(false);
        INDEXEDNOT_STORED_TERMVECTORSNOT.setStored(true);
        INDEXEDNOT_STORED_TERMVECTORSNOT.setIndexOptions(IndexOptions.DOCS_ONLY);
        INDEXEDNOT_STORED_TERMVECTORSNOT.setStoreTermVectors(false);
        INDEXEDNOT_STORED_TERMVECTORSNOT.setStoreTermVectorOffsets(false);
        INDEXEDNOT_STORED_TERMVECTORSNOT.setStoreTermVectorPayloads(false);
        INDEXEDNOT_STORED_TERMVECTORSNOT.setStoreTermVectorPositions(false);
        INDEXEDNOT_STORED_TERMVECTORSNOT.freeze();

        INDEXED_STOREDNOT_TERMVECTORS = new FieldType();
        INDEXED_STOREDNOT_TERMVECTORS.setIndexed(true);
        INDEXED_STOREDNOT_TERMVECTORS.setStored(false);
        INDEXED_STOREDNOT_TERMVECTORS.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        INDEXED_STOREDNOT_TERMVECTORS.setStoreTermVectors(true);
        INDEXED_STOREDNOT_TERMVECTORS.setStoreTermVectorOffsets(true);
        INDEXED_STOREDNOT_TERMVECTORS.setStoreTermVectorPayloads(true);
        INDEXED_STOREDNOT_TERMVECTORS.setStoreTermVectorPositions(true);
        INDEXED_STOREDNOT_TERMVECTORS.freeze();

        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT = new FieldType();
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setIndexed(false);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setStored(false);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setIndexOptions(IndexOptions.DOCS_ONLY);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setStoreTermVectors(false);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setStoreTermVectorOffsets(false);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setStoreTermVectorPayloads(false);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.setStoreTermVectorPositions(false);
        INDEXEDNOT_STOREDNOT_TERMVECTORSNOT.freeze();

    }

}
