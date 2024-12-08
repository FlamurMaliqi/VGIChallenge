-- CREATE DATABASE
DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'transport_data') THEN
        CREATE DATABASE transport_data;
    END IF;
END
$$;

-- SWITCH TO THE DATABASE
\c transport_data;

-- CREATE TABLES
CREATE TABLE stop (
    stop_id VARCHAR(255) PRIMARY KEY,
    stop_code VARCHAR(50),
    stop_name VARCHAR(100) NOT NULL,
    stop_desc TEXT,
    stop_lat DOUBLE PRECISION NOT NULL,
    stop_lon DOUBLE PRECISION NOT NULL,
    zone_id INTEGER,
    stop_url TEXT,
    location_type INTEGER,
    parent_station VARCHAR(50),
    platform_code VARCHAR(50),
    stop_timezone_header VARCHAR(50)
);

CREATE TABLE contact (
     contact_id BIGSERIAL PRIMARY KEY,
     email VARCHAR(100) NOT NULL UNIQUE,
     first_name VARCHAR(50) NOT NULL,
     last_name VARCHAR(50) NOT NULL,
     phone_number VARCHAR(30),
     institution VARCHAR(100) NOT NULL
);

CREATE TABLE route_block (
     route_block_id BIGSERIAL PRIMARY KEY,
     departure_time TIMESTAMP WITH TIME ZONE NOT NULL,
     arrival_time TIMESTAMP WITH TIME ZONE NOT NULL,
     short_name VARCHAR(10) NOT NULL,
     head_sign VARCHAR(100) NOT NULL,
     departure_stop_name VARCHAR(100) NOT NULL,
     departure_stop_latitude DOUBLE PRECISION NOT NULL CHECK (departure_stop_latitude BETWEEN 48 AND 50),
     departure_stop_longitude DOUBLE PRECISION NOT NULL CHECK (departure_stop_longitude BETWEEN 10 AND 13),
     arrival_stop_name VARCHAR(100) NOT NULL,
     arrival_stop_latitude DOUBLE PRECISION NOT NULL CHECK (arrival_stop_latitude BETWEEN 48 AND 50),
     arrival_stop_longitude DOUBLE PRECISION NOT NULL CHECK (arrival_stop_longitude BETWEEN 10 AND 13)
);

-- Composite index: The idx_route_block_time_range index combines the departure_time and arrival_time columns,
-- optimizing searches for records within a specific departure time range, and further improving performance
-- when also filtering by arrival_time in time-range queries.
CREATE INDEX idx_route_block_time_range ON route_block (departure_time, arrival_time);
-- This composite index will allow the database to quickly filter by shortName and headSign
-- before scanning the departureTime range, thus improving query performance significantly.
CREATE INDEX idx_route_block_search ON route_block (short_name, head_sign, departure_time);


CREATE TABLE booking (
    booking_id BIGSERIAL PRIMARY KEY,
    contact_id BIGSERIAL,
    pax INT,
    booking_hash VARCHAR(255) NOT NULL UNIQUE,
    FOREIGN KEY (contact_id) REFERENCES contact(contact_id) ON DELETE CASCADE -- Cascade delete contact if booking is deleted
);

-- Relational table for the relationship Booking <-> RouteBlock (1:n)
CREATE TABLE booking_route_block (
    booking_id BIGSERIAL,
    route_block_id BIGSERIAL,
    PRIMARY KEY (booking_id, route_block_id),
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE, -- Cascade delete booking_route_block if booking is deleted
    FOREIGN KEY (route_block_id) REFERENCES route_block(route_block_id) ON DELETE CASCADE -- Cascade delete route_block if booking is deleted
);

CREATE TABLE route (
    route_id BIGINT PRIMARY KEY,
    agency_id BIGINT NOT NULL,
    route_short_name VARCHAR(10) NOT NULL,
    route_long_name VARCHAR(100),
    route_desc TEXT,
    route_type INTEGER,
    route_url TEXT,
    route_color VARCHAR(50),
    route_text_color VARCHAR(50)
);

CREATE TABLE trip (
    trip_id VARCHAR(255) PRIMARY KEY,
    route_id BIGINT,
    service_id BIGINT NOT NULL,
    trip_headsign VARCHAR(100),
    trip_short_name VARCHAR(10),
    direction_id INTEGER,
    block_id BIGINT,
    shape_id BIGINT,
    trip_type INTEGER
);

-- COPY DATA INTO THE TABLES
COPY stop(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, zone_id, stop_url, location_type, parent_station, platform_code, stop_timezone_header)
FROM '/csv/stops.csv' DELIMITER ',' CSV HEADER;

COPY route(route_id, agency_id, route_short_name, route_long_name, route_desc, route_type, route_url, route_color, route_text_color)
FROM '/csv/routes.csv' DELIMITER ',' CSV HEADER;

COPY trip(route_id, service_id, trip_id, trip_headsign, trip_short_name, direction_id, block_id, shape_id, trip_type)
FROM '/csv/trips.csv' DELIMITER ',' CSV HEADER;