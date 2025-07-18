
-- create stage
CREATE OR REPLACE STAGE tasty_bytes_sample_data.public.blob_stage
  url = 's3://sfquickstarts/tastybytes/'
  file_format = (type = csv);

-- verify stage storage is created
LIST @tasty_bytes_sample_data.public.blob_stage/raw_pos/menu/;

-- insert data into table
COPY INTO tasty_bytes_sample_data.raw_pos.menu
FROM @tasty_bytes_sample_data.public.blob_stage/raw_pos/menu/;

