import { getAccessToken, setAccessToken, clearAccessToken } from '../src/auth_service/storage';
import * as SecureStore from 'expo-secure-store';

jest.mock('expo-secure-store', () => ({
  getItemAsync: jest.fn(),
  setItemAsync: jest.fn(),
  deleteItemAsync: jest.fn(),
}));

describe('Secure Token Storage', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('saves token securely via SecureStore', async () => {
    await setAccessToken('test-jwt');
    expect(SecureStore.setItemAsync).toHaveBeenCalledWith('com.aahara.auth', 'test-jwt');
  });

  it('retrieves token securely', async () => {
    (SecureStore.getItemAsync as jest.Mock).mockResolvedValue('saved-jwt');
    const token = await getAccessToken();
    expect(token).toBe('saved-jwt');
  });

  it('deletes token cleanly on logout', async () => {
    await clearAccessToken();
    expect(SecureStore.deleteItemAsync).toHaveBeenCalledWith('com.aahara.auth');
  });
});
