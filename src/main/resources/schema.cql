DROP KEYSPACE IF EXISTS application;
CREATE KEYSPACE IF NOT EXISTS application WITH REPLICATION = { 'class' : 'org.apache.cassandra.locator.SimpleStrategy', 'replication_factor': '1' } AND
DURABLE_WRITES = true;

DROP TABLE IF EXISTS Product;
CREATE TABLE Product (
    id UUID,
    name text,
    price double,
    date timestamp,
    PRIMARY KEY (id)
);
