<template>
  <div class="max-w-2xl mx-auto py-8 px-4">
    <h2 class="text-2xl font-bold text-gray-900 mb-6">
      My Profile
    </h2>

    <div
      v-if="profileStore.loading"
      class="text-gray-500"
    >
      Loading…
    </div>

    <div
      v-else-if="profileStore.error"
      class="text-red-600 mb-4"
    >
      {{ profileStore.error }}
    </div>

    <form
      v-else
      class="space-y-6"
      @submit.prevent="onSubmit"
    >
      <!-- Handedness -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Handedness</label>
        <select
          v-model="form.handedness"
          class="w-full border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
        >
          <option value="RIGHT">
            Right
          </option>
          <option value="LEFT">
            Left
          </option>
        </select>
      </div>

      <!-- Club distances -->
      <div>
        <h3 class="text-lg font-semibold text-gray-800 mb-3">
          Club Distances (yards)
        </h3>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <div
            v-for="club in ALL_CLUBS"
            :key="club"
            class="flex items-center gap-3"
          >
            <label class="w-40 text-sm text-gray-700 shrink-0">{{ formatClub(club) }}</label>
            <input
              v-model.number="form.clubDistancesYards[club]"
              type="number"
              min="0"
              max="400"
              placeholder="—"
              class="w-full border border-gray-300 rounded-md px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
        </div>
      </div>

      <div class="flex items-center gap-4">
        <button
          type="submit"
          :disabled="profileStore.loading"
          class="bg-primary-600 text-white px-6 py-2 rounded-lg font-semibold hover:bg-primary-700 disabled:opacity-50 transition-colors"
        >
          Save Profile
        </button>
        <span
          v-if="saved"
          class="text-primary-600 text-sm"
        >Saved!</span>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, watch } from 'vue'
  import { useProfileStore } from '@/stores/profile'
  import type { Club, Handedness } from '@/api/types'

  const ALL_CLUBS: Club[] = [
    'DRIVER',
    'THREE_WOOD',
    'FIVE_WOOD',
    'HYBRID',
    'FOUR_IRON',
    'FIVE_IRON',
    'SIX_IRON',
    'SEVEN_IRON',
    'EIGHT_IRON',
    'NINE_IRON',
    'PITCHING_WEDGE',
    'GAP_WEDGE',
    'SAND_WEDGE',
    'LOB_WEDGE',
    'PUTTER',
  ]

  const CLUB_LABELS: Record<Club, string> = {
    DRIVER: 'Driver',
    THREE_WOOD: '3 Wood',
    FIVE_WOOD: '5 Wood',
    HYBRID: 'Hybrid',
    FOUR_IRON: '4 Iron',
    FIVE_IRON: '5 Iron',
    SIX_IRON: '6 Iron',
    SEVEN_IRON: '7 Iron',
    EIGHT_IRON: '8 Iron',
    NINE_IRON: '9 Iron',
    PITCHING_WEDGE: 'PW',
    GAP_WEDGE: 'GW',
    SAND_WEDGE: 'SW',
    LOB_WEDGE: 'LW',
    PUTTER: 'Putter',
  }

  function formatClub(club: Club): string {
    return CLUB_LABELS[club]
  }

  const profileStore = useProfileStore()
  const saved = ref(false)

  const form = reactive<{
    handedness: Handedness
    clubDistancesYards: Partial<Record<Club, number | undefined>>
  }>({
    handedness: 'RIGHT',
    clubDistancesYards: {},
  })

  // Populate form once profile loads
  watch(
    () => profileStore.profile,
    (profile) => {
      if (!profile) return
      form.handedness = profile.handedness
      for (const club of ALL_CLUBS) {
        form.clubDistancesYards[club] = profile.clubDistancesYards[club]
      }
    },
    { immediate: true },
  )

  async function onSubmit(): Promise<void> {
    saved.value = false
    // Strip undefined/null entries before sending
    const distances: Partial<Record<Club, number>> = {}
    for (const club of ALL_CLUBS) {
      const val = form.clubDistancesYards[club]
      if (val !== undefined && val !== null && val > 0) {
        distances[club] = val
      }
    }
    try {
      await profileStore.saveProfile({ handedness: form.handedness, clubDistancesYards: distances })
      saved.value = true
      setTimeout(() => {
        saved.value = false
      }, 3000)
    } catch {
      // surfaced via profileStore.error
    }
  }

  onMounted(() => {
    profileStore.fetchProfile()
  })
</script>
