--liquibase formatted sql

--changeset juanreyes:1
CREATE TABLE IF NOT EXISTS package (
    id text,
    version text,
    category:text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS package_package (
    parent_id text NOT NULL REFERENCES package(id)
    item_id text NOT NULL REFERENCES package(id)
);

--changeset juanreyes:2
CREATE TABLE IF NOT EXISTS part (
    id text,
    name text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS package_part (
    package_id text  NOT NULL REFERENCES package(id)
    part_id text NOT NULL REFERENCES part(id)
);