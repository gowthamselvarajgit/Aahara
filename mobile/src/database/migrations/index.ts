import { SQLiteDatabase, Transaction } from 'react-native-sqlite-storage';

export const runMigrations = async (db: SQLiteDatabase) => {
  await db.transaction(async (tx: Transaction) => {
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS sync_metadata (
        key TEXT PRIMARY KEY,
        value TEXT
      );
    `);

    tx.executeSql(
      'SELECT value FROM sync_metadata WHERE key = "db_version"',
      [],
      (tx, results) => {
        let version = 0;
        if (results.rows.length > 0) {
          version = parseInt(results.rows.item(0).value, 10);
        }
        applyMigrations(tx, version);
      }
    );
  });
};

const applyMigrations = (tx: Transaction, currentVersion: number) => {
  if (currentVersion < 1) {
    // local_user_profile
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_user_profile (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        targetCalories REAL,
        sync_status TEXT DEFAULT 'synced',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    // local_foods
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_foods (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        name TEXT NOT NULL,
        brand TEXT,
        foodState TEXT,
        source TEXT,
        datasetVersion TEXT,
        sourceRecordId TEXT,
        ownerUserId TEXT,
        sync_status TEXT DEFAULT 'synced',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);
    
    // local_food_portions
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_food_portions (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        foodId TEXT NOT NULL,
        description TEXT NOT NULL,
        gramWeight REAL NOT NULL,
        sync_status TEXT DEFAULT 'synced',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    // local_diary_entries
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_diary_entries (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        userId TEXT NOT NULL,
        entryDate TEXT NOT NULL,
        mealType TEXT NOT NULL,
        foodId TEXT NOT NULL,
        portionId TEXT,
        quantity REAL NOT NULL,
        nutrientsSnapshotJson TEXT NOT NULL,
        sync_status TEXT DEFAULT 'pending_create',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    // local_water_logs
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_water_logs (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        userId TEXT NOT NULL,
        logDate TEXT NOT NULL,
        amountMl INTEGER NOT NULL,
        sync_status TEXT DEFAULT 'pending_create',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    // local_workout_sessions
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_workout_sessions (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        userId TEXT NOT NULL,
        sessionDate TEXT NOT NULL,
        name TEXT,
        notes TEXT,
        sync_status TEXT DEFAULT 'pending_create',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    // local_workout_sets
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_workout_sets (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        sessionId TEXT NOT NULL,
        exerciseId TEXT NOT NULL,
        weight REAL,
        reps INTEGER,
        rpe REAL,
        sync_status TEXT DEFAULT 'pending_create',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    // local_routines
    tx.executeSql(`
      CREATE TABLE IF NOT EXISTS local_routines (
        local_id TEXT PRIMARY KEY,
        server_id TEXT UNIQUE,
        client_id TEXT,
        userId TEXT NOT NULL,
        name TEXT NOT NULL,
        description TEXT,
        sync_status TEXT DEFAULT 'pending_create',
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        deleted_at TEXT,
        server_version INTEGER
      );
    `);

    tx.executeSql('INSERT OR REPLACE INTO sync_metadata (key, value) VALUES ("db_version", "1")');
  }
};
