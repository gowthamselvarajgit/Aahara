# Aahara Architecture

## Overview
Aahara is a local-first, API-driven nutrition and fitness tracking application. The architecture is split into a Spring Boot (Java 21) backend providing REST APIs and a React Native mobile application utilizing SQLite for local-first operations.

## Stack
- **Backend:** Java 21, Spring Boot 4.0.8, Maven, MySQL 8.x
- **Database Migrations:** Flyway
- **Mobile (Future):** React Native, TypeScript, SQLite
- **Communication:** REST APIs with JSON

## Principles
1. **Deterministic Calculation:** All nutritional and fitness metrics are calculated transparently without AI black-boxes.
2. **Historical Immutability:** Food and workout logs capture snapshots of data at the time of entry. Updates to master data do not retroactively alter user history.
3. **Local-First Synchronization:** The database schema employs UUIDs (v4) for all primary keys to facilitate offline creation and conflict-free synchronization.
4. **Data Provenance:** Every piece of master food and exercise data retains a trace to its origin (e.g., IFCT, USDA).
