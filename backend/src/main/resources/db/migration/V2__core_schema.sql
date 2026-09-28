-- Drop the dummy table safely
DROP TABLE IF EXISTS dummy;

-- Core Schema for Aahara

-- 1. Identity & Profiles
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    version INT DEFAULT 0
);

CREATE TABLE user_profiles (
    user_id VARCHAR(36) PRIMARY KEY,
    date_of_birth DATE,
    sex ENUM('MALE', 'FEMALE', 'OTHER'),
    height_cm DECIMAL(5,2),
    current_weight_kg DECIMAL(5,2),
    target_weight_kg DECIMAL(5,2),
    goal ENUM('LOSE_WEIGHT', 'MAINTAIN_WEIGHT', 'GAIN_WEIGHT'),
    activity_level ENUM('SEDENTARY', 'LIGHTLY_ACTIVE', 'MODERATELY_ACTIVE', 'VERY_ACTIVE', 'EXTRA_ACTIVE'),
    target_calories INT,
    target_protein_g INT,
    target_carbs_g INT,
    target_fat_g INT,
    target_water_ml INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 2. Nutrition Master Data
CREATE TABLE foods (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    tamil_name VARCHAR(255),
    search_aliases TEXT,
    brand VARCHAR(255),
    category VARCHAR(100),
    food_state ENUM('RAW', 'COOKED', 'BOILED', 'FRIED', 'STEAMED', 'BAKED', 'PREPARED', 'PACKAGED', 'UNKNOWN') DEFAULT 'UNKNOWN',
    source VARCHAR(100),
    source_url VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version INT DEFAULT 0,
    INDEX idx_food_name (name),
    INDEX idx_food_category (category)
);

CREATE TABLE food_portions (
    id VARCHAR(36) PRIMARY KEY,
    food_id VARCHAR(36) NOT NULL,
    description VARCHAR(255) NOT NULL,
    gram_weight DECIMAL(8,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE
);

CREATE TABLE food_nutrients (
    id VARCHAR(36) PRIMARY KEY,
    food_id VARCHAR(36) NOT NULL,
    nutrient_type ENUM('CALORIES', 'PROTEIN', 'CARBOHYDRATE', 'FAT', 'FIBER', 'SUGAR', 'SODIUM', 'POTASSIUM', 'CALCIUM', 'IRON') NOT NULL,
    amount_per_100g DECIMAL(10,3) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE,
    UNIQUE KEY uk_food_nutrient (food_id, nutrient_type)
);

-- 3. User Nutrition (Diary)
CREATE TABLE diary_entries (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    entry_date DATE NOT NULL,
    meal_type ENUM('BREAKFAST', 'LUNCH', 'SNACK', 'DINNER', 'OTHER') NOT NULL,
    food_id VARCHAR(36) NOT NULL,
    portion_id VARCHAR(36) NULL,
    quantity DECIMAL(8,2) NOT NULL,
    -- Snapshots for historical immutability
    food_name_snapshot VARCHAR(255),
    portion_name_snapshot VARCHAR(255),
    calories_snapshot DECIMAL(8,2),
    protein_snapshot DECIMAL(8,2),
    carbs_snapshot DECIMAL(8,2),
    fat_snapshot DECIMAL(8,2),
    fiber_snapshot DECIMAL(8,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    sync_id VARCHAR(36),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES foods(id),
    FOREIGN KEY (portion_id) REFERENCES food_portions(id)
);

CREATE TABLE water_logs (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    entry_date DATE NOT NULL,
    amount_ml INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Fitness Master Data
CREATE TABLE exercises (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    muscle_group VARCHAR(100),
    equipment VARCHAR(100),
    tracking_type ENUM('WEIGHT_REPS', 'BODYWEIGHT_REPS', 'DURATION', 'DISTANCE', 'TIME', 'REPS_ONLY') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 5. User Fitness
CREATE TABLE routines (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE workout_sessions (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    routine_id VARCHAR(36) NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (routine_id) REFERENCES routines(id) ON DELETE SET NULL
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
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
);
