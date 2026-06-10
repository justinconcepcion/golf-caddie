import api from './axios'
import type { AdviceRequest, AdviceResponse } from './types'

export const adviceApi = {
  async request(body: AdviceRequest): Promise<AdviceResponse> {
    const response = await api.post<AdviceResponse>('/advice', body)
    return response.data
  },
}
