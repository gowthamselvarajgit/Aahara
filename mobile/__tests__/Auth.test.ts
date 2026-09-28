import { getAccessToken, setAccessToken, clearAccessToken } from '../src/auth_service/storage';
import * as Keychain from 'react-native-keychain';

jest.mock('react-native-keychain', () => ({
  getGenericPassword: jest.fn(),
  setGenericPassword: jest.fn(),
  resetGenericPassword: jest.fn(),
}));

describe('Secure Token Storage', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('saves token securely via Keychain', async () => {
    await setAccessToken('test-jwt');
    expect(Keychain.setGenericPassword).toHaveBeenCalledWith('aahara_user', 'test-jwt', { service: 'com.aahara.auth' });
  });

  it('retrieves token securely', async () => {
    (Keychain.getGenericPassword as jest.Mock).mockResolvedValue({ password: 'saved-jwt' });
    const token = await getAccessToken();
    expect(token).toBe('saved-jwt');
  });

  it('deletes token cleanly on logout', async () => {
    await clearAccessToken();
    expect(Keychain.resetGenericPassword).toHaveBeenCalledWith({ service: 'com.aahara.auth' });
  });
});
