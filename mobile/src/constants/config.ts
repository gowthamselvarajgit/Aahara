// We use 10.0.2.2 for Android emulator to hit host's localhost
// In production, this would be injected via react-native-config or similar
export const API_BASE_URL = __DEV__ ? 'http://10.0.2.2:8080' : 'https://api.aahara.com';
export const ENVIRONMENT = __DEV__ ? 'development' : 'production';
