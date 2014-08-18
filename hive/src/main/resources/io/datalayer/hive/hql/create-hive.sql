CREATE DATABASE IF NOT EXISTS DB1;

USE DB1;

DROP TABLE IF EXISTS T1;

CREATE EXTERNAL TABLE T1 (
    stock_symbol          STRING,  
    stock_date            STRING,
    stock_price_open      DOUBLE,
    stock_price_high      DOUBLE,   
    stock_price_low       DOUBLE,
    stock_price_close     DOUBLE,
    stock_volume          BIGINT,
    stock_price_adj_close DOUBLE
)
PARTITIONED BY (exchange_place STRING, year BIGINT)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
-- STORED AS 
--  INPUTFORMAT "com.hadoop.mapred.DeprecatedLzoTextInputFormat"
--  OUTPUTFORMAT "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat"
--  LOCATION 's3n://${DATA_MODEL_BUCKET}/data/table_sample/'
LOCATION 'hdfs://localhost:9000/user/eric/datawarehouse/DB1/T1';

LOAD DATA name LOCAL INPATH '/dataset/hdp/nyse/NYSE-2000-2001.tsv' 
  OVERWRITE INTO TABLE T1 
  PARTITION (exchange='NYSE', year='2000');

--- ---------------------------------------------------------------------------

ALTER TABLE table_name 
  DROP IF EXISTS PARTITION (site_id='test', record_date='2012-08-01');
ALTER TABLE table_name 
  DROP IF EXISTS PARTITION (site_id='test', record_date='2012-08-02');
ALTER TABLE table_name 
  ADD PARTITION (site_id='test', record_date='2012-08-01') 
    LOCATION 'hdfs://localhost:9000/name-test/data/table/tracking_id=test/record_date=2012-08-01';
ALTER TABLE table_name 
  ADD PARTITION (site_id='test', record_date='2012-08-02') 
  LOCATION 'hdfs://localhost:9000/name-test/data/table/tracking_id=test/record_date=2012-08-02';

--- ---------------------------------------------------------------------------

SELECT COUNT(*) FROM table_name;

--- ---------------------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS dbname;

USE dbname;

DROP TABLE IF EXISTS table_hive;

CREATE EXTERNAL TABLE table_hive (
        composite_ID     STRING,
        page_time        INT
)
PARTITIONED BY (site_id string, record_date string)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
-- STORED AS 
--  INPUTFORMAT "com.hadoop.mapred.DeprecatedLzoTextInputFormat"
--  OUTPUTFORMAT "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat"
-- LOCATION 's3n://${DATA_MODEL_BUCKET}/data/table_hive/'
  LOCATION 'hdfs://localhost:9000/name-test/data/table_hive';

ALTER TABLE table_hive 
DROP IF EXISTS PARTITION (site_id='test', record_date='2012-08-01');

ALTER TABLE table_hive 
DROP IF EXISTS PARTITION (site_id='test', record_date='2012-08-02');

ALTER TABLE table_hive 
ADD PARTITION (site_id='test', record_date='2012-08-01') LOCATION 'hdfs://localhost:9000/name-test/data/table_hive/tracking_id=test/record_date=2012-08-01';
ALTER TABLE table_hive 
ADD PARTITION (site_id='test', record_date='2012-08-02') LOCATION 'hdfs://localhost:9000/name-test/data/table_hive/tracking_id=test/record_date=2012-08-02';

--- ---------------------------------------------------------------------------
SELECT COUNT(*) FROM table_hive;
--- ---------------------------------------------------------------------------
