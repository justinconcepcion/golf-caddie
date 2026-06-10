import { describe, it, expect, vi, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useProfileStore } from '@/stores/profile'
import * as profileApiModule from '@/api/profile'
import type { PlayerProfileResponse } from '@/api/types'

const mockProfile: PlayerProfileResponse = {
  id: 1,
  handedness: 'RIGHT',
  clubDistancesYards: { DRIVER: 250, SEVEN_IRON: 150 },
}

describe('useProfileStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('fetchProfile_givenSuccessfulResponse_setsProfile', async () => {
    vi.spyOn(profileApiModule.profileApi, 'get').mockResolvedValue(mockProfile)

    const store = useProfileStore()
    await store.fetchProfile()

    expect(store.profile).toEqual(mockProfile)
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
  })

  it('fetchProfile_givenApiError_setsErrorMessage', async () => {
    vi.spyOn(profileApiModule.profileApi, 'get').mockRejectedValue(new Error('Network error'))

    const store = useProfileStore()
    await store.fetchProfile()

    expect(store.profile).toBeNull()
    expect(store.error).toBe('Network error')
    expect(store.loading).toBe(false)
  })

  it('saveProfile_givenSuccessfulResponse_updatesProfile', async () => {
    vi.spyOn(profileApiModule.profileApi, 'update').mockResolvedValue({
      ...mockProfile,
      handedness: 'LEFT',
    })

    const store = useProfileStore()
    await store.saveProfile({ handedness: 'LEFT', clubDistancesYards: {} })

    expect(store.profile?.handedness).toBe('LEFT')
    expect(store.loading).toBe(false)
  })
})
