-- V4 Authentication Provider Columns
ALTER TABLE users ADD COLUMN auth_provider VARCHAR(50);
ALTER TABLE users ADD COLUMN auth_provider_id VARCHAR(255);
ALTER TABLE users ADD UNIQUE INDEX idx_users_provider (auth_provider, auth_provider_id);
