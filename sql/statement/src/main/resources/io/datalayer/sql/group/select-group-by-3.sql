SELECT
CASE WHEN LENGTH(cv) > 100000 then "100000+" 
     WHEN LENGTH(cv) > 10000 then "10000+" 
     WHEN LENGTH(cv) > 1000 then "1000+"
     WHEN LENGTH(cv) > 100 then "100+" 
     WHEN LENGTH(cv) > 10 then "10+"
 ELSE "small" END AS length_buckets ,
sum(LENGTH(cv)) as tot_lens,
MIN(pageview_id) as min_pageview_id,
MAX(pageview_id) as max_pageview_id,
COUNT(*) as pings
from
table1
WHERE
	contract_id = "name" 
	AND record_date = "2013-03-12"
GROUP BY
CASE WHEN LENGTH(cv) > 100000 then "100000+" 
     WHEN LENGTH(cv) > 10000 then "10000+" 
     WHEN LENGTH(cv) > 1000 then "1000+"
     WHEN LENGTH(cv) > 100 then "100+" 
     WHEN LENGTH(cv) > 10 then "10+"
 ELSE "small" end
;
