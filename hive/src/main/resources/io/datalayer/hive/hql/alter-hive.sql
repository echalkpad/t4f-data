ALTER 
  TABLE table_spl 
  add PARTITION (
    site_id='name', 
    ddate='2013-01-11'
  ) 
  LOCATION 's3n://.../site_id=name/date=2013-01-11';
