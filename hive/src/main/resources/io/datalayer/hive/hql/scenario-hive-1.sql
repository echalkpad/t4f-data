SET mapred.reduce.tasks=20;

USE Database1;

DROP TABLE IF EXISTS table_test;
CREATE TABLE table_test
  ROW FORMAT DELIMITED
  FIELDS TERMINATED BY '\t'
  STORED AS TEXTFILE
AS
SELECT
 Visitor_ID,
 get_json_object(universal_variables, '$.e') as experiment_name,
 get_json_object(universal_variables, '$.tc') as targeting_criteria,
 get_json_object(universal_variables, '$.c') as creative,
 get_json_object(universal_variables, '$.s') as shown,
 get_json_object(universal_variables, '$.cnt') as control,
 COUNT(*) as count_instances,
 COUNT(distinct QB_PV_R_PageView_ID) AS page_views
FROM Database1.t2_108_pageviews_legacy
LATERAL VIEW
  EXPLODE(SPLIT(REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(get_json_object(qb_pv_r_customvalues, '$.uv_data'), '\\},\\{' , '}_XX_{' ),'\\[',''),'\\]',''),'_XX_')) A AS universal_variables
WHERE TrackingID = "bravissimo-pepperberry"
 AND RecordDate >= "2013-05-16"
 AND RecordDate <= "2013-05-16"
 AND get_json_object(qb_pv_r_customvalues, '$.uv_data') IS NOT NULL
GROUP BY
 Visitor_ID,
 get_json_object(universal_variables, '$.e')  ,
 get_json_object(universal_variables, '$.tc') ,
 get_json_object(universal_variables, '$.c')  ,
 get_json_object(universal_variables, '$.s')  ,
 get_json_object(universal_variables, '$.cnt') 
;

DROP TABLE IF EXISTS table_test_2;

CREATE TABLE table_test_2
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS TEXTFILE
AS
SELECT 
  visitor_id as Visitor_ID,
  sum(get_json_object(conversion_info, '$.total.value'))    as conversion_info__value,
  MIN(get_json_object(conversion_info, '$.total.currency')) as conversion_info__currency_min,
  MAX(get_json_object(conversion_info, '$.total.currency')) as conversion_info__currency_max,
  COUNT(*) as conversions
FROM Database1.conversions WHERE
  tid   = "bravissimo-pepperberry" AND 
  record_date >= "2013-05-16"   AND
  record_date <= "2013-05-16"
GROUP BY visitor_id;

DROP TABLE IF EXISTS brain_cew_etc_overview2_analysis_aq_bravissimo_pepperberry_1368783380;
CREATE TABLE brain_cew_etc_overview2_analysis_aq_bravissimo_pepperberry_1368783380
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
 CASE when convs.Visitor_ID IS NOT NULL then "user has conversion" else "user has no conversion" end as did_visitor_convert,
 sum(t.count_instances) as count_instances,
 sum(t.page_views) as page_views,
 COUNT(distinct t.Visitor_ID) as count_visitors,
 sum(convs.conversion_info__value) as conversion_info__value,
 MIN(convs.conversion_info__currency_min) as conversion_info__currency_min,
 MAX(convs.conversion_info__currency_max) as conversion_info__currency_max,
 sum(convs.conversions) as sum_conversions 
FROM
 table_test t
LEFT OUTER JOIN
 table_test_2 convs
ON
 t.Visitor_ID = convs.Visitor_ID
GROUP BY 
 t.experiment_name,
 t.targeting_criteria,
 t.creative,
 t.shown,
 t.control,
 CASE when convs.Visitor_ID IS NOT NULL then "user has conversion" else "user has no conversion" end
ORDER BY
 experiment_name,
 targeting_criteria,
 creative,
 shown,
 control,
 did_visitor_convert
;

DROP TABLE IF EXISTS table_test;
DROP TABLE IF EXISTS table_test_2;

exit
;

LEFT OUTER JOIN 
 (select visitor_id,
         sum(get_json_object(conversion_info, '$.total.value'))  as    conversion_info__value,
         MIN(get_json_object(conversion_info, '$.total.currency')) conversion_info__currency_min,
         MAX(get_json_object(conversion_info, '$.total.currency')) conversion_info__currency_max,
         COUNT(*) as conversions
     FROM Database1.conversions WHERE
      tid   = "bravissimo-pepperberry" AND 
      record_date >= "2013-05-16"   AND
      record_date <= "2013-05-16"
      GROUP BY visitor_id ) converters 

select get_json_object(conversion_info, '$.total.value')    conversion_info__value,
   get_json_object(conversion_info, '$.total.currency') conversion_info__currency,
    conversion_info, 
     FROM Database1.conversions WHERE
      tid   = "tid_value" AND 
      record_date >= "2013-01-20"   AND
      record_date <= "2013-01-20"
       limit 20;

select * 
 FROM Database1.conversions WHERE
  tid   = "tid_value" AND 
  record_date >= "2013-01-20"   AND
  record_date <= "2013-01-20"
   limit 20;

