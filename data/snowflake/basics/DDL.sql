-- Create DB
CREATE OR REPLACE DATABASE sf_tuts;

-- Get current DB and schema
SELECT CURRENT_DATABASE(), CURRENT_SCHEMA();

-- Create table
CREATE OR REPLACE TABLE emp_basic (
  first_name STRING,
  last_name STRING,
  email STRING,
  streetaddress STRING,
  city STRING,
  start_date DATE
);

-- See all tables 
SHOW TERSE TABLES;

-- Show tables by schema
SHOW TERSE TABLES in PUBLIC;

-- Show tables by database
SHOW TERSE TABLES in DATABASE SF_TUTS;

-- Create Virtual Warehouse (VWH)
CREATE OR REPLACE WAREHOUSE sf_tuts_wh WITH 
  WAREHOUSE_SIZE = 'XSMALL' -- size of instance
  AUTO_SUSPEND = 180 -- number of seconds of inactivity for warehouse to automatically suspended
  AUTO_RESUME = TRUE -- enable warehouse to resume processing when SQL statement is submitted
  INITIALLY_SUSPENDED = TRUE; -- initially suspende warehouse until SQL statement submitted


-- See current warehouse
SELECT CURRENT_WAREHOUSE();