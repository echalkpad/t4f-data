USE test;

SET mapred.output.compress=true;
SET hive.exec.compress.output=true;
SET io.seqfile.compression.type=BLOCK;
SET mapred.output.compression.codec=com.hadoop.compression.lzo.LzopCodec;
SET hive.exec.dynamic.partition.mode=nonstrict;
SET hive.exec.dynamic.partition=true;
SET hive.exec.max.dynamic.partitions=100000;
SET hive.exec.max.dynamic.partitions.pernode=10000;

CREATE EXTERNAL TABLE table_sample (
        visitor_id                         STRING,
        server_side_time                   BIGINT,
        url                        STRING,
        referrer_url               STRING,
        group_site_id           STRING,
        visitor_table_composite_ID STRING,
        composite_id STRING
)
PARTITIONED BY (site_id string, record_date string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
STORED AS INPUTFORMAT "com.hadoop.mapred.DeprecatedLzoTextInputFormat"
OUTPUTFORMAT "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat"
LOCATION 's3n://..../data/table_sample/';

INSERT OVERWRITE TABLE table_sample
  PARTITION(
    site_id, 
    record_date
  )
SELECT
    PV_RTime_UNI
    PV_R
    PV_Rrrer
    PV_Rr,
    CustomVal
    R_Usergent _UserIPAds,
    UserWebting   PVlientPing_UNIX,
    V_R_Sessounte _eView_ID,
    Group_Visitor_ID,
    Group_TracIngId   
    at(PageView_ID,Visitor_ID),
    at(ID_Visitor_ID,RecateTrackingIG_R_TragID, Recate
 FROM 
  tablename;

SELECT COUNT(*) 
  FROM table_sample a 
  JOIN pv_categorised b 
  ON (
    a.visitor_ID=b.visitor_ID 
    and a.table_ID=b.table_ID 
    and a.record_date=b.record_date 
    and a.site_id=b.site_id
  ) 
  WHERE 
    a.record_date='2012-08-01' 
    and b.record_date='2012-08-01' 
    and a.site_id='test' 
    and b.site_id='test';

SELECT COUNT(*) 
 FROM table_sample a 
 JOIN pv_categorised b 
  ON (
  a.composite_id=b.composite_id
 ) 
 WHERE 
   a.record_date='2012-08-01' 
   and b.record_date='2012-08-01' 
   and a.site_id='test' 
   and b.site_id='test'
 );
