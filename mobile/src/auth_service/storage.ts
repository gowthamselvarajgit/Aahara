import * as Keychain from 'react-native-keychain';

const SERVICE_NAME = 'com.aahara.auth';
const DUMMY_USERNAME = 'aahara_user';

export const getAccessToken = async (): Promise<string | null> => {
  try {
    const credentials = await Keychain.getGenericPassword({ service: SERVICE_NAME });
    if (credentials) {
      return credentials.password;
    }
    return null;
  } catch (e) {
    // We intentionally do not log the token or sensitive errors in production
    console.error('Error reading token from secure storage');
    return null;
  }
};

export const setAccessToken = async (token: string): Promise<void> => {
  try {
    await Keychain.setGenericPassword(DUMMY_USERNAME, token, { service: SERVICE_NAME });
  } catch (e) {
    console.error('Error saving token to secure storage');
  }
};

export const clearAccessToken = async (): Promise<void> => {
  try {
    await Keychain.resetGenericPassword({ service: SERVICE_NAME });
  } catch (e) {
    console.error('Error removing token from secure storage');
  }
};
