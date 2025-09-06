-- NEW FEATURE: expiry date - Database migration script
-- This script adds the expiry_date column to the url_mapping table

-- Add expiry_date column to url_mapping table
ALTER TABLE url_mapping ADD COLUMN expiry_date TIMESTAMP;

-- Create index on expiry_date for better performance when querying expired URLs
CREATE INDEX idx_url_mapping_expiry_date ON url_mapping(expiry_date);

-- Note: This migration is optional since Hibernate with ddl-auto=update will handle it automatically
-- Run this script manually if you prefer to control the database schema changes

