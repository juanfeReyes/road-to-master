--liquibase formatted sql

--changeset juan:1
CREATE SCHEMA IF NOT EXISTS logistics;
SET search_path to logistics;

CREATE SEQUENCE shipment_seq_id;

CREATE TABLE IF NOT EXISTS shipment (
    id INTEGER NOT NULL PRIMARY KEY,
    source VARCHAR(100),
    destination VARCHAR(100)
);


CREATE TABLE IF NOT EXISTS travel (
    id VARCHAR(40) NOT NULL PRIMARY KEY,
    address VARCHAR(100) NOT NULL,
    arrivalDate TIMESTAMP,
    mediaType VARCHAR(20) NOT NULL,
    shipment_id INTEGER,
    CONSTRAINT kf_shipment_id FOREIGN KEY(shipment_id) REFERENCES shipment(id)
);