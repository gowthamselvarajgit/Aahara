import SQLite, { SQLiteDatabase } from 'react-native-sqlite-storage';
import { runMigrations } from '../migrations';

SQLite.enablePromise(true);

let dbInstance: SQLiteDatabase | null = null;

export const getDb = async (): Promise<SQLiteDatabase> => {
  if (dbInstance) return dbInstance;
  
  dbInstance = await SQLite.openDatabase({
    name: 'aahara.db',
    location: 'default',
  });
  
  // Enforce foreign keys
  await dbInstance.executeSql('PRAGMA foreign_keys = ON;');
  
  return dbInstance;
};

export const initDb = async (): Promise<void> => {
  const db = await getDb();
  await runMigrations(db);
};
