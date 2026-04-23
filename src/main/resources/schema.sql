-- DDL for Weather entity table
-- Generated from Weather.java entity class

CREATE TABLE IF NOT EXISTS weather (
    id SERIAL PRIMARY KEY,
    date VARCHAR(10) NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    temperature DOUBLE PRECISION NOT NULL
);

-- Add constraints for data validation
ALTER TABLE weather 
ADD CONSTRAINT chk_date_format CHECK (date ~ '^\d{4}-\d{2}-\d{2}$'),
ADD CONSTRAINT chk_lat_range CHECK (lat >= -90.0 AND lat <= 90.0),
ADD CONSTRAINT chk_lon_range CHECK (lon >= -180.0 AND lon <= 180.0),
ADD CONSTRAINT chk_temp_range CHECK (temperature >= -100.0 AND temperature <= 100.0);

-- Create indexes for frequently queried columns
CREATE INDEX idx_weather_date ON weather(date);
CREATE INDEX idx_weather_city ON weather(city);
CREATE INDEX idx_weather_lat_lon ON weather(lat, lon);

-- Add unique constraint to prevent duplicate weather records for same date and location
CREATE UNIQUE INDEX idx_weather_unique_location_date ON weather(date, lat, lon, city);
