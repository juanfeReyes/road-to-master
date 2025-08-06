--liquibase formatted sql

--changeset juan:1
CREATE TABLE workers (
    id VARCHAR(36),
    name VARCHAR(100),
    lastname VARCHAR(100),
    birthday DATE,
    years_experience INT
);
--rollback DROP TABLE workers;
