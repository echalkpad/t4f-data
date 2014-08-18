SET mapred.reduce.tasks=20;

DROP TABLE IF EXISTS tmp;
CREATE TABLE tmp
  ROW FORMAT DELIMITED
  FIELDS TERMINATED BY '\t'
  STORED AS TEXTFILE
AS

SELECT
 vid,
 get_json_object(custom_values, '$.e') as experiment_name,
 get_json_object(custom_values, '$.tc') as targeting_criteria,
 get_json_object(custom_values, '$.c') as creative,
 get_json_object(custom_values, '$.s') as shown,
 get_json_object(custom_values, '$.cnt') as control,
 COUNT(*) as count_instances,
 COUNT(distinct QB_PV_R_PageView_ID) AS page_views
FROM 
  pv
LATERAL VIEW
  EXPLODE(SPLIT(REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(get_json_object(customvalues, '$.etc'), '\\},\\{' , '}_XX_{' ),'\\[',''),'\\]',''),'_XX_')) A AS custom_values

WHERE tid = "example"
 AND rec_date >= "2013-05-16"
 AND rec_date <= "2013-05-16"
 AND get_json_object(qb_pv_r_customvalues, '$.qb_etc_data') IS NOT NULL

GROUP BY
 vid,
 get_json_object(custom_values, '$.e')  ,
 get_json_object(custom_values, '$.tc') ,
 get_json_object(custom_values, '$.c')  ,
 get_json_object(custom_values, '$.s')  ,
 get_json_object(custom_values, '$.cnt') 
;

DROP TABLE IF EXISTS conv;
CREATE TABLE conv
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
AS

SELECT 
  visitor_id as vid,
  sum(get_json_object(conversion_info, '$.total.value'))    as conversion_info__value,
  MIN(get_json_object(conversion_info, '$.total.currency')) as conversion_info__currency_min,
  MAX(get_json_object(conversion_info, '$.total.currency')) as conversion_info__currency_max,
  COUNT(*) as conversions
FROM StandardDataPipeline.conversions WHERE
  tracking_id   = "example" AND 
  record_date >= "2013-05-16"   AND
  record_date <= "2013-05-16"
GROUP BY visitor_id
;

DROP TABLE IF EXISTS tmp2;
CREATE TABLE tmp2
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
AS

SELECT
 t.experiment_name,
 t.targeting_criteria,
 t.creative,
 t.shown,
 t.control,
 CASE when convs.vid IS NOT NULL then "user has conversion" else "user has no conversion" end as did_visitor_convert,
 sum(t.count_instances) as count_instances,
 sum(t.page_views) as page_views,
 COUNT(distinct t.vid) as count_visitors,
 sum(convs.conversion_info__value) as conversion_info__value,
 MIN(convs.conversion_info__currency_min) as conversion_info__currency_min,
 MAX(convs.conversion_info__currency_max) as conversion_info__currency_max,
 sum(convs.conversions) as sum_conversions 
FROM
 tmp t
LEFT OUTER JOIN
 conv convs
ON
 t.vid = convs.vid
GROUP BY 
 t.experiment_name,
 t.targeting_criteria,
 t.creative,
 t.shown,
 t.control,
 CASE when convs.vid IS NOT NULL then "user has conversion" else "user has no conversion" end
ORDER BY
 experiment_name,
 targeting_criteria,
 creative,
 shown,
 control,
 did_visitor_convert
;

DROP TABLE IF EXISTS tmp;
DROP TABLE IF EXISTS conv;

exit
;
