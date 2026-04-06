--liquibase formatted sql

--changeset juanreyes:1
CREATE TABLE IF NOT EXISTS package (
    id text,
    version text,
    category text,
    PRIMARY KEY (id)
);

--changeset juanreyes:2
CREATE TABLE IF NOT EXISTS part (
    id text,
    name text,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS package_content (
    package_id text,
    content_type text,
    content_id text
);