# Mobile Synchronization Strategy

## Local-First
The React Native app will use SQLite (e.g., via WatermelonDB or raw expo-sqlite). 

## Primary Keys
All IDs across the system are UUIDv4. This allows the mobile client to generate a final, globally unique ID immediately upon record creation without waiting for a server round-trip.

## Sync Markers
Every syncable entity contains:
- `created_at` (UTC timestamp)
- `updated_at` (UTC timestamp)
- `deleted_at` (UTC timestamp - soft delete)
- `version` (Integer, optimistic locking)

## Resolution
- **Last-Write-Wins (LWW)** is used based on `updated_at` for simple conflicts.
- Soft deletes propagate to the server, which then propagates to other devices. Hard deletes are cleaned up by backend background jobs after an expiration period (e.g., 30 days).
