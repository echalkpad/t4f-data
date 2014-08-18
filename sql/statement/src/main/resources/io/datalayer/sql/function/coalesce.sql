SELECT 
    table1.tracking_id, table2.cluster_name AS query_cluster,
    table3.cluster_name AS upload_cluster, 
    COALESCE(table1.config_version, 'default') AS config_version
  FROM 
    table4.table4 table1 
  JOIN 
    table4.elasticsearch table2 ON (table2.server_id = table1.es_server)
  JOIN 
    table4.elasticsearch table3 ON (table3.server_id = COALESCE(table1.upload_server, table1.es_server))
  ORDER BY table1.tracking_id;
