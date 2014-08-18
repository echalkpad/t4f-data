-- id somenum
-- 1 10
-- 2 12
-- 3 3
-- 4 15

SELECT t1.id, t1.somenum, sum(t2.somnenum) AS sum 
FROM t t1
INNER JOIN t t2 on t1.id >= t2.id
GROUP BY t1.id, t1.somenum
ORDER BY t1.id
