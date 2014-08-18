-- If you have tables A and B, both with colum C, 
-- here are the records, which are present in TABLE A but not in B:

SELECT A.*
FROM A
    LEFT JOIN B ON (A.C = B.C)
WHERE B.C IS NULL

-- To get all the differences with a single query, 
-- a full join must be used.
-- What you need to know in this CASE is, that when a record can be found in A, 
-- but not in B, than the columns which come FROM B will be NULL, and similarly for those, 
-- which are present in B and not in A, the columns FROM A will be null.

SELECT A.*, B.*
FROM A
    FULL JOIN B ON (A.C = B.C)
WHERE A.C IS NULL OR B.C IS NULL
