import { defineStore } from 'pinia'
import { ref } from 'vue'
import { profileApi } from '@/api/profile'
import type { PlayerProfileResponse, UpdateProfileRequest } from '@/api/types'

export const useProfileStore = defineStore('profile', () => {
  const profile = ref<PlayerProfileResponse | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchProfile(): Promise<void> {
    loading.value = true
    error.value = null
    try {
      profile.value = await profileApi.get()
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to load profile'
    } finally {
      loading.value = false
    }
  }

  async function saveProfile(data: UpdateProfileRequest): Promise<void> {
    loading.value = true
    error.value = null
    try {
      profile.value = await profileApi.update(data)
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to save profile'
      throw err
    } finally {
      loading.value = false
    }
  }

  return { profile, loading, error, fetchProfile, saveProfile }
})
