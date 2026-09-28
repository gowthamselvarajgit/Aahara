import { getDb } from '../sqlite/db';
import { v4 as uuidv4 } from 'uuid';

export class WorkoutSessionRepository {
  async createLocal(data: any): Promise<string> {
    const db = await getDb();
    const localId = uuidv4();
    const now = new Date().toISOString();
    
    // Abstracted base creation pattern for local_workout_sessions
    const keys = Object.keys(data);
    const columns = ['local_id', 'sync_status', 'created_at', 'updated_at', ...keys];
    const placeholders = ['?', '?', '?', '?', ...keys.map(() => '?')];
    const values = [localId, 'pending_create', now, now, ...keys.map(k => data[k])];
    
    await db.executeSql(
      `INSERT INTO local_workout_sessions (${columns.join(', ')}) VALUES (${placeholders.join(', ')})`,
      values
    );
    return localId;
  }

  async getById(localId: string): Promise<any> {
    const db = await getDb();
    const [results] = await db.executeSql(
      `SELECT * FROM local_workout_sessions WHERE local_id = ? AND deleted_at IS NULL`,
      [localId]
    );
    return results.rows.length ? results.rows.item(0) : null;
  }

  async updateLocal(localId: string, data: any): Promise<void> {
    const db = await getDb();
    const now = new Date().toISOString();
    
    const keys = Object.keys(data);
    const sets = keys.map(k => `${k} = ?`);
    sets.push("sync_status = 'pending_update'");
    sets.push("updated_at = ?");
    
    const values = [...keys.map(k => data[k]), now, localId];
    
    await db.executeSql(
      `UPDATE local_workout_sessions SET ${sets.join(', ')} WHERE local_id = ?`,
      values
    );
  }

  async markAsDeleted(localId: string): Promise<void> {
    const db = await getDb();
    const now = new Date().toISOString();
    await db.executeSql(
      `UPDATE local_workout_sessions SET deleted_at = ?, sync_status = 'pending_delete', updated_at = ? WHERE local_id = ?`,
      [now, now, localId]
    );
  }
}
