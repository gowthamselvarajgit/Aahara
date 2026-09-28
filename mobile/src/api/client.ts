import axios, { AxiosError, AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import { ApiError } from '../types/models';
import { getAccessToken } from '../auth_service/storage';
import { API_BASE_URL } from '../constants/config';

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use(
  async (config: InternalAxiosRequestConfig) => {
    const token = await getAccessToken();
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError) => {
    // Structure error for the app
    if (!error.response) {
      return Promise.reject({ status: 0, message: 'Network error or timeout' } as ApiError);
    }
    
    const data = error.response.data as any;
    const apiError: ApiError = {
      status: error.response.status,
      message: data?.message || error.message,
      details: data?.details,
      timestamp: data?.timestamp,
    };
    
    if (apiError.status === 401) {
      // Trigger logout or refresh logic here later
      console.warn('API returned 401 Unauthorized.');
    } else if (apiError.status === 409) {
      // Trigger conflict state
      console.warn('API returned 409 Conflict. Optimistic locking failed.');
    }
    
    return Promise.reject(apiError);
  }
);
