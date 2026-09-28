-- V6__phase_3_nutrition_data_foundation.sql

-- 1. Provenance and User-Created Food Separation
ALTER TABLE foods ADD COLUMN owner_user_id VARCHAR(36) NULL;
ALTER TABLE foods ADD COLUMN dataset_version VARCHAR(50) NULL;
ALTER TABLE foods ADD COLUMN source_record_id VARCHAR(100) NULL;
ALTER TABLE foods ADD COLUMN license VARCHAR(255) NULL;
ALTER TABLE foods ADD COLUMN status VARCHAR(50) DEFAULT 'ACTIVE';

-- Prevent arbitrary deletion of user-created foods breaking historical diaries
ALTER TABLE foods ADD CONSTRAINT fk_foods_owner FOREIGN KEY (owner_user_id) REFERENCES users(id) ON DELETE SET NULL;

CREATE INDEX idx_foods_source_lookup ON foods (source, source_record_id, dataset_version);

-- 2. Expand NutrientType to allow all micronutrients
ALTER TABLE food_nutrients MODIFY COLUMN nutrient_type VARCHAR(50) NOT NULL;

-- 3. Search & Deterministic Aliases
CREATE TABLE food_aliases (
    id VARCHAR(36) PRIMARY KEY,
    food_id VARCHAR(36) NOT NULL,
    alias VARCHAR(255) NOT NULL,
    normalized_alias VARCHAR(255) NOT NULL,
    language VARCHAR(10) DEFAULT 'en',
    alias_type VARCHAR(50) DEFAULT 'SEARCH_ALIAS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE,
    INDEX idx_food_aliases_normalized (normalized_alias),
    INDEX idx_food_aliases_food_id (food_id)
);
