SELECT 
  visits, 
  sum(CASE when num_v_sites > 2 then 1 else 0 end), 
  sum(CASE when num_v_sites = 2 then 1 else 0 end), 
  sum(CASE when num_v_sites = 1 then 1 else 0 end), 
  sum(CASE when num_v_sites <= 0 then 1 else 0 end) 
from (
  SELECT 
    v_id, 
    MAX(host) as visits, 
    COUNT(distinct host) as num_v_sites 
  from (
    SELECT 
      v_id as v_id, 
      CASE 
        when url RLIKE '.*url2\.com' then 1 
        when url RLIKE '.*url2\.com' then 0 
        else -1 end 
      as host, 
      qb_pv_r_url 
    from 
      table_name
    where 
      sit_di='sit_id' 
      and r_date='2013-01-16'
      and type=0 
  ) a 
  GROUP BY uvid
) b 
GROUP BY visits
;

-- -1  0   0   167 0
-- 0   0   18  31136   0
-- 1   0   292 76  0
