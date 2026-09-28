-- V5__phase_2b_user_data.sql
-- Add synchronization and idempotency fields for Phase 2B Offline Sync

-- Add client_id for idempotency and version for optimistic concurrency
ALTER TABLE diary_entries ADD COLUMN client_id VARCHAR(36) NULL;
ALTER TABLE diary_entries ADD COLUMN version INT DEFAULT 0;

ALTER TABLE water_logs ADD COLUMN client_id VARCHAR(36) NULL;
ALTER TABLE water_logs ADD COLUMN sync_id VARCHAR(36) NULL;
ALTER TABLE water_logs ADD COLUMN version INT DEFAULT 0;

ALTER TABLE routines ADD COLUMN client_id VARCHAR(36) NULL;
ALTER TABLE routines ADD COLUMN sync_id VARCHAR(36) NULL;
ALTER TABLE routines ADD COLUMN version INT DEFAULT 0;

ALTER TABLE workout_sessions ADD COLUMN client_id VARCHAR(36) NULL;
ALTER TABLE workout_sessions ADD COLUMN sync_id VARCHAR(36) NULL;
ALTER TABLE workout_sessions ADD COLUMN version INT DEFAULT 0;

ALTER TABLE workout_sets ADD COLUMN client_id VARCHAR(36) NULL;
ALTER TABLE workout_sets ADD COLUMN sync_id VARCHAR(36) NULL;
ALTER TABLE workout_sets ADD COLUMN version INT DEFAULT 0;

-- Idempotency constraints (a client_id should only be inserted once per user, avoiding duplicate retries)
ALTER TABLE diary_entries ADD CONSTRAINT uk_diary_client_id UNIQUE (user_id, client_id);
ALTER TABLE water_logs ADD CONSTRAINT uk_water_client_id UNIQUE (user_id, client_id);
ALTER TABLE routines ADD CONSTRAINT uk_routine_client_id UNIQUE (user_id, client_id);
ALTER TABLE workout_sessions ADD CONSTRAINT uk_session_client_id UNIQUE (user_id, client_id);
-- workout_sets belongs to session, but user_id is implicit. 
-- However, we don't have user_id on workout_sets. We'll skip UK for sets or use session_id + client_id
ALTER TABLE workout_sets ADD CONSTRAINT uk_set_client_id UNIQUE (session_id, client_id);

-- Add performance indexes for pagination and range filtering
CREATE INDEX idx_diary_user_date ON diary_entries (user_id, entry_date);
CREATE INDEX idx_water_user_date ON water_logs (user_id, entry_date);
CREATE INDEX idx_workout_user_date ON workout_sessions (user_id, start_time);
CREATE INDEX idx_routine_user ON routines (user_id);
CREATE INDEX idx_set_session ON workout_sets (session_id);
