import { apiClient } from '../src/api/client';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import { ApiError } from '../src/types/models';

const mock = new MockAdapter(apiClient);

describe('API Client', () => {
  afterEach(() => {
    mock.reset();
  });

  it('handles 401 Unauthorized structured errors', async () => {
    mock.onGet('/protected').reply(401, {
      status: 401,
      message: 'Token expired'
    });

    try {
      await apiClient.get('/protected');
      fail('Should have thrown an error');
    } catch (e: any) {
      const err = e as ApiError;
      expect(err.status).toBe(401);
      expect(err.message).toBe('Token expired');
    }
  });

  it('handles 409 Conflict structured errors', async () => {
    mock.onPut('/update').reply(409, {
      status: 409,
      message: 'Optimistic lock exception'
    });

    try {
      await apiClient.put('/update');
      fail('Should have thrown an error');
    } catch (e: any) {
      const err = e as ApiError;
      expect(err.status).toBe(409);
      expect(err.message).toBe('Optimistic lock exception');
    }
  });

  it('handles network errors', async () => {
    mock.onGet('/timeout').networkError();

    try {
      await apiClient.get('/timeout');
      fail('Should have thrown an error');
    } catch (e: any) {
      const err = e as ApiError;
      expect(err.status).toBe(0); // structured for network error
      expect(err.message).toBe('Network error or timeout');
    }
  });
});
