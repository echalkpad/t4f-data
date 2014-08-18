-- CREATE 
--   [UNIQUE] INDEX index-Name
-- ON 
--   table-Name ( Simple-column-Name [ ASC | DESC ]
--     [ , Simple-column-Name [ ASC | DESC ]] * )
    
CREATE INDEX MOBILE_USERAGENT_INDEX ON MOBILE (MOBILE_USER_AGENT ASC);
