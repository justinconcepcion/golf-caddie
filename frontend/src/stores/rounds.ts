import { defineStore } from 'pinia'
import { ref } from 'vue'
import { roundsApi } from '@/api/rounds'
import { adviceApi } from '@/api/advice'
import type { RoundResponse, ShotResponse, LogShotRequest, AdviceResponse } from '@/api/types'

export const useRoundsStore = defineStore('rounds', () => {
  const activeRound = ref<RoundResponse | null>(null)
  const shots = ref<ShotResponse[]>([])
  const lastAdvice = ref<AdviceResponse | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function startRound(courseId: string): Promise<void> {
    loading.value = true
    error.value = null
    try {
      activeRound.value = await roundsApi.create({ courseId })
      shots.value = []
      lastAdvice.value = null
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to start round'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function addShot(body: LogShotRequest): Promise<ShotResponse> {
    if (!activeRound.value) {
      throw new Error('No active round')
    }
    loading.value = true
    error.value = null
    try {
      const shot = await roundsApi.logShot(activeRound.value.id, body)
      shots.value.push(shot)
      return shot
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to log shot'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function requestAdvice(
    holeNumber: number,
    lat: number,
    lng: number,
    shotNumber?: number,
  ): Promise<AdviceResponse> {
    if (!activeRound.value) {
      throw new Error('No active round')
    }
    loading.value = true
    error.value = null
    try {
      const result = await adviceApi.request({
        courseId: activeRound.value.courseId,
        holeNumber,
        lat,
        lng,
        shotNumber,
      })
      lastAdvice.value = result
      return result
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to get advice'
      throw err
    } finally {
      loading.value = false
    }
  }

  function endRound(): void {
    activeRound.value = null
    shots.value = []
    lastAdvice.value = null
    error.value = null
  }

  return {
    activeRound,
    shots,
    lastAdvice,
    loading,
    error,
    startRound,
    addShot,
    requestAdvice,
    endRound,
  }
})
