import api from './axios'
import type { PlayerProfileResponse, UpdateProfileRequest } from './types'

export const profileApi = {
  async get(): Promise<PlayerProfileResponse> {
    const response = await api.get<PlayerProfileResponse>('/profile')
    return response.data
  },

  async update(body: UpdateProfileRequest): Promise<PlayerProfileResponse> {
    const response = await api.put<PlayerProfileResponse>('/profile', body)
    return response.data
  },
}
