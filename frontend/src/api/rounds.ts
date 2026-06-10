import api from './axios'
import type {
  CreateRoundRequest,
  RoundResponse,
  LogShotRequest,
  ShotResponse,
} from './types'

export const roundsApi = {
  async create(body: CreateRoundRequest): Promise<RoundResponse> {
    const response = await api.post<RoundResponse>('/rounds', body)
    return response.data
  },

  async logShot(roundId: number, body: LogShotRequest): Promise<ShotResponse> {
    const response = await api.post<ShotResponse>(`/rounds/${roundId}/shots`, body)
    return response.data
  },
}
