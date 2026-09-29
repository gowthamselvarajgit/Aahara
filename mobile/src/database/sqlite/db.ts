import * as SQLite from 'expo-sqlite';
import { runMigrations } from '../migrations';

let dbInstance: any | null = null;

export const getDb = async (): Promise<any> => {
  if (dbInstance) return dbInstance;
  
  const rawDb = await SQLite.openDatabaseAsync('aahara.db');
  
  const wrapper = {
    executeSql: async (query: string, params: any[] = []) => {
      try {
        if (query.trim().toLowerCase().startsWith('select') || query.trim().toLowerCase().startsWith('pragma')) {
          const result = await rawDb.getAllAsync(query, params);
          return [{
             insertId: undefined,
             rowsAffected: 0,
             rows: {
                length: result.length,
                item: (index: number) => result[index]
             }
          }];
        } else {
          const result = await rawDb.runAsync(query, params);
          return [{
             insertId: result.lastInsertRowId,
             rowsAffected: result.changes,
             rows: {
                length: 0,
                item: () => null
             }
          }];
        }
      } catch (e) {
         throw e;
      }
    },
    transaction: async (cb: (tx: any) => Promise<void> | void) => {
      await rawDb.withTransactionAsync(async () => {
         const txWrapper = {
            executeSql: async (query: string, params: any[] = [], successCb?: any, errorCb?: any) => {
               try {
                  let r;
                  if (query.trim().toLowerCase().startsWith('select') || query.trim().toLowerCase().startsWith('pragma')) {
                    const result = await rawDb.getAllAsync(query, params);
                    r = {
                       insertId: undefined,
                       rowsAffected: 0,
                       rows: {
                          length: result.length,
                          item: (index: number) => result[index]
                       }
                    };
                  } else {
                    const result = await rawDb.runAsync(query, params);
                    r = {
                       insertId: result.lastInsertRowId,
                       rowsAffected: result.changes,
                       rows: {
                          length: 0,
                          item: () => null
                       }
                    };
                  }
                  if (successCb) {
                     successCb(txWrapper, r);
                  }
               } catch (e) {
                  if (errorCb) {
                     errorCb(txWrapper, e);
                  } else {
                     throw e;
                  }
               }
            }
         };
         await cb(txWrapper);
      });
    }
  };

  await rawDb.execAsync('PRAGMA foreign_keys = ON;');
  
  dbInstance = wrapper;
  return wrapper;
};

export const initDb = async (): Promise<void> => {
  const db = await getDb();
  await runMigrations(db);
};
