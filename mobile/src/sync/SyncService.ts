export type SyncStatus = 'synced' | 'pending_create' | 'pending_update' | 'pending_delete' | 'failed' | 'conflict';

/**
 * SyncService creates the foundation for the Phase 5/6 remote synchronization engine.
 * Responsibilities:
 * - Orchestrate uploads to backend using apiClient
 * - Orchestrate downloads of remote changes since last sync
 * - Handle conflicts and tombstones
 * - Update sync_status in SQLite
 */
export class SyncService {
  async sync(): Promise<void> {
    console.log('SyncEngine: Starting synchronization...');
    // Future sync process:
    // 1. Check network connectivity.
    // 2. Query all local records with sync_status IN ('pending_create', 'pending_update', 'pending_delete')
    // 3. Batch push changes to the server API.
    // 4. Await 200 OK from server.
    // 5. Query server API for changes newer than local metadata 'last_sync_timestamp'.
    // 6. Merge server changes locally.
    // 7. If HTTP 409 Conflict occurs, mark local record sync_status as 'conflict' and retain server_version.
    // 8. If successful, mark local records sync_status as 'synced' and update 'last_sync_timestamp'.
    console.warn('SyncService foundation established. Full bidirectional synchronization deferred to future phase.');
  }

  async resolveConflict(localId: string, resolutionData: any): Promise<void> {
    console.warn('Conflict resolution logic deferred.');
  }
}

export const syncService = new SyncService();
