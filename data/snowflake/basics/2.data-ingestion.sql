
-- ingest data into stage storage
-- PUT file://<file-path> @<namespace>.%<table_name>;
PUT file://D:\develop\road-to-master\data\snowflake\basics\data\employees0*.csv @sf_tuts.public.%emp_basic;

-- See staged files
LIST @sf_tuts.public.%emp_basic;

-- Copy data into target tables
COPY INTO emp_basic
  FROM @%emp_basic
  FILE_FORMAT = (type = csv field_optionally_enclosed_by='"')
  PATTERN = '.*employees0[1-5].csv.gz'
  ON_ERROR = 'skip_file';

