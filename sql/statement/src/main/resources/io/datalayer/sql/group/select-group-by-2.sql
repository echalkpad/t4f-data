SELECT
	pings,
	MIN(pageview_id) as min_pageview_id,
	MAX(pageview_id) as max_pageview_id,
	COUNT(*) as pageviews
FROM
(
	SELECT
		pageview_id,
		COUNT(*) as pings
	FROM
    	table1
	WHERE
		contract_id = "name" and
		recorddate = "2013-06-10"
	GROUP BY 
    	pagview_id
) pvoverview
GROUP BY pings
ORDER BY pings desc
LIMIT 100;
