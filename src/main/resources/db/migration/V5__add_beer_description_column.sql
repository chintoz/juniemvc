-- Database Migration Script for Beer Description Column
-- This script adds the description column to the beer table

-- Add description column to beer table
ALTER TABLE beer ADD COLUMN description VARCHAR(1000);

-- Add comment to explain the purpose of this migration script
-- This script adds a description column to the beer table to store information about the beer's flavor profile, characteristics, etc.