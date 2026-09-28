-- V3 Audit Corrections

-- 1. Fix Dangerous Cascades on Historical Data & Expand Historical Nutrient Snapshots
-- Since dropping unnamed foreign keys behaves differently across MySQL and H2,
-- we safely rebuild the tables (which are currently empty in production).

DROP TABLE IF EXISTS workout_sets;
DROP TABLE IF EXISTS diary_entries;

CREATE TABLE diary_entries (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    entry_date DATE NOT NULL,
    meal_type ENUM('BREAKFAST', 'LUNCH', 'SNACK', 'DINNER', 'OTHER') NOT NULL,
    food_id VARCHAR(36) NOT NULL,
    portion_id VARCHAR(36) NULL,
    quantity DECIMAL(8,2) NOT NULL,
    
    food_name_snapshot VARCHAR(255),
    portion_name_snapshot VARCHAR(255),
    
    -- Backward compatibility for basic aggregations
    calories_snapshot DECIMAL(8,2),
    protein_snapshot DECIMAL(8,2),
    carbs_snapshot DECIMAL(8,2),
    fat_snapshot DECIMAL(8,2),
    fiber_snapshot DECIMAL(8,2),
    
    -- New robust historical snapshot supporting 26+ nutrients immutably
    nutrients_snapshot_json JSON,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    sync_id VARCHAR(36),
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE RESTRICT,
    FOREIGN KEY (portion_id) REFERENCES food_portions(id) ON DELETE RESTRICT
);


CREATE TABLE workout_sets (
    id VARCHAR(36) PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL,
    exercise_id VARCHAR(36) NOT NULL,
    set_number INT NOT NULL,
    set_type ENUM('NORMAL', 'WARM_UP', 'DROP_SET', 'FAILURE') DEFAULT 'NORMAL',
    weight_kg DECIMAL(6,2),
    reps INT,
    duration_seconds INT,
    distance_meters DECIMAL(8,2),
    rpe DECIMAL(3,1),
    is_completed BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    
    FOREIGN KEY (session_id) REFERENCES workout_sessions(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE RESTRICT
);
