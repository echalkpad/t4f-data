SELECT
	LENGTH(cv) as cv,
	MIN(pageview_id) as min_pageview_id,
	MAX(pageview_id) as max_pageview_id,
	COUNT(*) as pings
FROM
    table1
WHERE
	contract_id = "name"
	AND record_date = "2013-02-09"
GROUP BY
	LENGTH(cv) 
ORDER BY cv DESC 
LIMIT 100;
;
