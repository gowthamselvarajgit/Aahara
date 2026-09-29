import * as SecureStore from 'expo-secure-store';

const SERVICE_NAME = 'com.aahara.auth';

export const getAccessToken = async (): Promise<string | null> => {
  try {
    const token = await SecureStore.getItemAsync(SERVICE_NAME);
    if (token) {
      return token;
    }
    return null;
  } catch (e) {
    console.error('Error reading token from secure storage');
    return null;
  }
};

export const setAccessToken = async (token: string): Promise<void> => {
  try {
    await SecureStore.setItemAsync(SERVICE_NAME, token);
  } catch (e) {
    console.error('Error saving token to secure storage');
  }
};

export const clearAccessToken = async (): Promise<void> => {
  try {
    await SecureStore.deleteItemAsync(SERVICE_NAME);
  } catch (e) {
    console.error('Error removing token from secure storage');
  }
};
