-- Only for MySQL

SELECT COUNT(DISTINCT vid) 
  FROM table1 
  WHERE pid = 21
    AND date_record
      BETWEEN str_to_date('2013-02-01','%Y-%m-%d') 
        AND str_to_date('2013-02-15','%Y-%m-%d')
