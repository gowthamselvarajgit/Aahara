import { create } from 'zustand';
import { getAccessToken, setAccessToken, clearAccessToken } from '../auth_service/storage';

interface AuthState {
  isAuthenticated: boolean;
  isLoading: boolean;
  checkAuth: () => Promise<void>;
  login: (token: string) => Promise<void>;
  logout: () => Promise<void>;
}

export const useAuthStore = create<AuthState>((set) => ({
  isAuthenticated: false,
  isLoading: true,
  checkAuth: async () => {
    const token = await getAccessToken();
    set({ isAuthenticated: !!token, isLoading: false });
  },
  login: async (token: string) => {
    await setAccessToken(token);
    set({ isAuthenticated: true });
  },
  logout: async () => {
    await clearAccessToken();
    set({ isAuthenticated: false });
  }
}));
