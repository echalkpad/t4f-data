SELECT 
  record_date, 
  COUNT(record_date) 
FROM pv 
LEFT OUTER JOIN (
  SELECT 
    guid as guid, 
    date as record_date 
  FROM 
    pv 
  WHERE 
    tid = 'tid2'
) t 
ON 
  pv.guid = t.guid 
  AND pv.record_date = t.record_date 
WHERE tid IN ('tid1', 'tid2') 
  AND record_date > '2010-12-20' 
  AND t.guid is null
GROUP BY 
  record_date;
