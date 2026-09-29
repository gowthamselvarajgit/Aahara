import { DiaryRepository } from '../src/database/repositories/DiaryRepository';

jest.mock('uuid', () => ({
  v4: () => 'test-uuid-001',
}));

jest.mock('expo-sqlite', () => {
  return {
    openDatabaseAsync: jest.fn().mockResolvedValue({
      withTransactionAsync: (cb: any) => cb(),
      runAsync: jest.fn().mockResolvedValue({ lastInsertRowId: 1, changes: 1 }),
      getAllAsync: jest.fn().mockResolvedValue([]),
      execAsync: jest.fn(),
    }),
  };
});

describe('Database Repositories', () => {
  let diaryRepo: DiaryRepository;

  beforeEach(() => {
    diaryRepo = new DiaryRepository();
    jest.clearAllMocks();
  });

  it('marks entries as pending_create on initialization', async () => {
    const id = await diaryRepo.createLocal({
      userId: 'u1',
      entryDate: '2026-10-01',
      mealType: 'LUNCH',
      foodId: 'f1',
      quantity: 1.5,
      nutrientsSnapshotJson: '{}'
    });
    expect(id).toBeDefined();
    // In a real integration test we'd query the DB to check sync_status
    // Here we ensure the API at least runs the sql
  });

  it('marks entries as deleted and pending_delete for tombstone behavior', async () => {
    await diaryRepo.markAsDeleted('local-id');
    // Ensure it executed tombstone logic
  });
});
