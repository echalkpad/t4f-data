SELECT COUNT(*) FROM table_hive;

SELECT composite_id, count 
  FROM (
    SELECT 
      composite_id, COUNT(*) AS count 
    FROM 
      TABLE 
    GROUP BY 
      composite_id
    ) t 
  WHERE 
    count > 1;   

SELECT table.*
  FROM TABLE 
  LEFT OUTER JOIN table_hive 
    ON (
      table.composite_id = table_hive.composite_id
      )
  WHERE 
    table_hive.composite_id IS NULL;

SELECT table.*
  FROM TABLE 
  LEFT OUTER JOIN raw_table_sample 
    ON (
      table.composite_id = raw_table_sample.v
      )
  WHERE 
    table_hive.composite_id IS NULL;

SELECT table.*
  FROM TABLE 
  LEFT OUTER JOIN 
    (SELECT * FROM table_hive 
      WHERE table_hive.record_date='2012-08-01') table_hive
    ON (table_hive.composite_id = table.composite_id)
  WHERE table_hive.composite_id IS NULL;

SELECT COUNT(table_hive.*)
  FROM table_hive 
  LEFT OUTER JOIN table
    ON (table_hive.composite_id = table.composite_id)
  WHERE table.composite_id IS NULL;

SELECT distinct table_hive.page_time
  FROM table_hive 
  LEFT OUTER JOIN table
    ON (table_hive.composite_id = table.composite_id)
  WHERE table.composite_id IS NULL;

SELECT distinct table_hive.composite_id
  FROM table_hive 
  LEFT OUTER JOIN table
    ON (table_hive.composite_id = table.composite_id)
  WHERE table.composite_id IS NULL;

-- -----------------------------------------------------------------------------
hive -e "use database; describe extended TABLE PARTITION (tracking_id='test', recorddate='2012-08-01')"
s3n://name-model/raw/table/tracking_id=test/recorddate=2012-08-01
-- -----------------------------------------------------------------------------
hive -e "use database; describe extended table;"
s3n://name-test/data/raw_table_sample
-- -----------------------------------------------------------------------------
CREATE EXTERNAL TABLE TABLE (
        Visitor_ID                  STRING,
        PingTime_UNIX              BIGINT,

        URL                        STRING,
        Referrer_URL               STRING,
        PageNumber                 INT,
        
        CustomValues               STRING,
        
        UserWebAgent                STRING,
        UserIPAddress               STRING,
        UserWebAgentSettings        STRING,
        
        Files_To_Make                      BIGINT,

       ReferrerCategory        STRING COMMENT '',
        ReferrerSubCategory     STRING COMMENT '',

        URLCategory                STRING COMMENT '',
        URLSubCategory             STRING COMMENT '',
        
        ClientPingTime_UNIX        BIGINT,
        V_R_SessionCounters             STRING,
        PageView_ID                STRING,
        Group_Visitor_ID            STRING,
        Group_Tracking_ID           STRING,
        
        V_d_SearchQuery                 STRING,
        V_d_SearchQueryCategorisatiON   STRING,
        V_d_ElementCategorisatiON       STRING
        
  )
  PARTITIONED BY (SiteID string, RecordDate string)
  ROW FORMAT DELIMITED
  FIELDS TERMINATED BY '\t'
  -- STORED AS INPUTFORMAT "com.hadoop.mapred.DeprecatedLzoTextInputFormat"
  -- OUTPUTFORMAT "org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat"
  -- LOCATION 's3n://${DATA_MODEL_BUCKET}/data/table/'
;
-- -----------------------------------------------------------------------------
ALTER TABLE TABLE ADD COLUMNS (
  V_d_GeoLocatiON    STRING COMMENT '2 letter country code based ON IP of user'
)
;
-- ----------------------------------------------------------------------------
hive> describe extended table_sample;
OK
composite_id    string  TABLE level identifier, combining visitor id, tracking id and record date
visitor_id  string  
server_side_time    bigint  
url string  
referrer_url    string  
browser_side_page_number    bigint  as generated in the javascript
custom_values   string  
user_web_agent  string  
user_ip_address string  
user_web_agent_settings string  
...
canonical_referrer_url  string  
raw_time_on_page    bigint  calculated as the time FROM 1st to last ping
entrance_referrer_url   string  
tracking_id string  
record_date string  
         
Detailed Table InformatiON  Table(tableName:table_sample, dbName:database, owner:batchrunner, CREATETime:1350985067, lastAccessTime:0, retention:0, sd:StorageDescriptor(cols:[FieldSchema(name:composite_id, type:string, comment:TABLE level identifier, combining visitor id, tracking id and record date), FieldSchema(name:visitor_id, type:string, comment:null), FieldSchema(name:server_side_time, type:bigint, comment:null), FieldSchema(name:url, type:string, comment:null), FieldSchema(name:referrer_url, type:string, comment:null), FieldSchema(name:browser_side_page_number, type:bigint, comment:as generated in the javascript), FieldSchema(name:custom_values, type:string, comment:null), FieldSchema(name:user_web_agent, type:string, comment:null), FieldSchema(name:user_ip_address, type:string, comment:null), FieldSchema(name:user_web_agent_settings, type:string, comment:null), FieldSchema(name:client_side_time, type:bigint, comment:null), FieldSchema(name:qtracker_version, type:string, comment:null), FieldSchema(name:session_counters, type:string, comment:null), FieldSchema(name:table_id, type:string, comment:null), FieldSchema(name:group_visitor_id, type:string, comment:null), FieldSchema(name:group_tracking_id, type:string, comment:null), FieldSchema(name:page_time, type:bigint, comment:calculated as the difference between successive table_sample), FieldSchema(name:url_category, type:string, comment:null), FieldSchema(name:url_subcategory, type:string, comment:null), FieldSchema(name:user_agent_categorisation, type:string, comment:null), FieldSchema(name:geo_location, type:string, comment:null), FieldSchema(name:clean_search_query, type:string, comment:null), FieldSchema(name:search_categorisation, type:string, comment:null), FieldSchema(name:page_num_in_visit, type:int, comment:null), FieldSchema(name:visit_id, type:string, comment:null), FieldSchema(name:visit_number, type:bigint, comment:null), FieldSchema(name:visit_referrer_type, type:string, comment:null), FieldSchema(name:visit_referrer_details, type:string, comment:null), FieldSchema(name:visit_referrer_label, type:string, comment:null), FieldSchema(name:page_num_in_entrance, type:int, comment:null), FieldSchema(name:entrance_id, type:string, comment:null), FieldSchema(name:entrance_number, type:bigint, comment:null), FieldSchema(name:entrance_referrer_type, type:string, comment:null), FieldSchema(name:entrance_referrer_details, type:string, comment:null), FieldSchema(name:entrance_referrer_label, type:string, comment:null), FieldSchema(name:page_num_in_session, type:int, comment:null), FieldSchema(name:session_id, type:string, comment:null), FieldSchema(name:session_number, type:bigint, comment:null), FieldSchema(name:canonical_url, type:string, comment:null), FieldSchema(name:canonical_referrer_url, type:string, comment:null), FieldSchema(name:raw_time_on_page, type:bigint, comment:calculated as the time FROM 1st to last ping), FieldSchema(name:entrance_referrer_url, type:string, comment:null), FieldSchema(name:tracking_id, type:string, comment:null), FieldSchema(name:record_date, type:string, comment:null)], location:s3n://name-model-2/data/table_sample, inputFormat:com.hadoop.mapred.DeprecatedLzoTextInputFormat, outputFormat:org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat, compressed:false, numBuckets:-1, serdeInfo:SerDeInfo(name:null, serializationLib:org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe, parameters:{serialization.format= , field.delim=
Time taken: 0.458 seconds
hive> 
-- -----------------------------------------------------------------------------
LOAD DATA 
  LOCAL 
  INPATH '/q/.../tablereducer.out' 
  OVERWRITE 
  INTO TABLE 
  TABLE 
  PARTITION (
    SiteID='test', 
    RecordDate='2012/09/13'
  )
;
-- -----------------------------------------------------------------------------
SELECT 
    Visitor_ID
  FROM
    table
  WHERE 
    SiteID = 'test' 
    AND RecordDate >= '2012/08/18' 
    AND RecordDate <= '2012/09/20'
    AND Visitor_ID != ""
    AND NOT(Visitor_ID IS NULL)
;
-- -----------------------------------------------------------------------------
SELECT COUNT(DISTINCT vid) 
  FROM pv 
LATERAL VIEW json_tuple(pv.user_agent_categorisation, "bt") b as browser_type 
  WHERE 
    b.browser_type != 'robot' 
    AND pv.tid = 'harveysfurniture' 
    AND pv.dat >= '2013-02-01' 
    AND pv.dat <= '2013-05-13';
-- -----------------------------------------------------------------------------
