SELECT EMAIL FROM COMPANY WHERE COMPANY_ID = 10930;

SELECT COMPANY_VALUE.VALUE_STRING FROM COMPANY_VALUE  AND COMPANY_VALUE.ATTRIBUTE='email1' AND COMPANY_VALUE.COMPANY_ID = 10930   

UPDATE COMPANY SET EMAIL = 'email',PASSWORD='email',LOGIN ='email' WHERE COMPANY_ID = {company_id};

UPDATE COMPANY SET COMPANY.EMAIL = ( 
  SELECT COMPANY_VALUE.VALUE_STRING FROM COMPANY_VALUE    
   WHERE COMPANY.EMAIL IS NULL  
     AND COMPANY.COMPANY_ID = 10930 
     AND  COMPANY_VALUE.COMPANY_ID  = 10930    
     AND COMPANY_VALUE.ATTRIBUTE='email1');

SELECT EMAIL FROM COMPANY WHERE COMPANY_ID = 10930 ;
