-- liquibase formatted sql

-- changeset JuanReyes:1
CREATE TABLE if not exists test_table
(
  test_id INT,
  test_column INT,
  PRIMARY KEY (test_id)
)

-- changeset JuanReyes:2
create materialized view if not exists shipment_view as
    select *
    from shipment
with data;

create unique index if not exists shipment_view_idx01 on shipment_view(id)
