import { getDb } from '../sqlite/db';
import { DiaryEntry } from '../../types/models';
import 'react-native-get-random-values';
import { v4 as uuidv4 } from 'uuid';

export class DiaryRepository {
  async createLocal(entry: Omit<DiaryEntry, 'id' | 'syncId' | 'version' | 'clientId'>): Promise<string> {
    const db = await getDb();
    const localId = uuidv4();
    const now = new Date().toISOString();
    
    await db.executeSql(
      `INSERT INTO local_diary_entries 
        (local_id, userId, entryDate, mealType, foodId, portionId, quantity, nutrientsSnapshotJson, sync_status, created_at, updated_at) 
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'pending_create', ?, ?)`,
      [
        localId,
        entry.userId,
        entry.entryDate,
        entry.mealType,
        entry.foodId,
        entry.portionId || null,
        entry.quantity,
        entry.nutrientsSnapshotJson,
        now,
        now
      ]
    );
    return localId;
  }

  async getByDate(userId: string, date: string): Promise<any[]> {
    const db = await getDb();
    const [results] = await db.executeSql(
      `SELECT * FROM local_diary_entries WHERE userId = ? AND entryDate = ? AND deleted_at IS NULL`,
      [userId, date]
    );
    
    const entries = [];
    for (let i = 0; i < results.rows.length; i++) {
      entries.push(results.rows.item(i));
    }
    return entries;
  }

  async markAsDeleted(localId: string): Promise<void> {
    const db = await getDb();
    const now = new Date().toISOString();
    
    // Tombstone behavior
    await db.executeSql(
      `UPDATE local_diary_entries SET deleted_at = ?, sync_status = 'pending_delete', updated_at = ? WHERE local_id = ?`,
      [now, now, localId]
    );
  }
}
