CREATE DATABASE shipment_db;
\connect shipment_db

CREATE TABLE IF NOT EXISTS SHIPMENT (
    id SERIAL PRIMARY KEY UNIQUE,
    source  VARCHAR(100) NOT NULL ,
    destination VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS PRODUCT (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL ,
    description VARCHAR(100) NOT NULL,
    price float NOT NULL  
);

CREATE TABLE IF NOT EXISTS PRODUCT_SHIPMENT (
    id SERIAL PRIMARY KEY,
    product_key integer REFERENCES PRODUCT(id),
    shipment_key integer REFERENCES SHIPMENT(id)
);

INSERT INTO SHIPMENT (id, source, destination)  
VALUES 
    (1, 'Cali', 'Jamundi'),
    (2, 'Istambul', 'Pretoria'),
    (3, 'Hamburg', 'New york'),
    (4, 'Houston', 'Rio do Janeiro');

INSERT INTO PRODUCT (id, name, description, price)  
VALUES 
    (1, 'Blender', 'Extreme Blender mixer', 35.55),
    (2, 'CoolFlask', '32 Oz Thermo regulated water container', 99.99),
    (3, 'LG Display', '40 Inch Oled ultrawide screen', 1520.22),
    (4, 'Altoids', 'Pepper mints', 1.20);

INSERT INTO PRODUCT_SHIPMENT (id, product_key, shipment_key)  
VALUES 
    (1, 2, 3),
    (2, 4, 1);

CREATE DATABASE warehouse_db;
\connect warehouse_db

CREATE TABLE IF NOT EXISTS SHIPMENT (
    id SERIAL PRIMARY KEY UNIQUE,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL
);

