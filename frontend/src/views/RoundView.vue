<template>
  <div class="max-w-2xl mx-auto py-8 px-4 space-y-8">
    <h2 class="text-2xl font-bold text-gray-900">
      Round
    </h2>

    <!-- Start round panel -->
    <section
      v-if="!roundsStore.activeRound"
      class="space-y-4"
    >
      <p class="text-gray-600">
        No active round. Start one to begin logging shots.
      </p>
      <div class="flex gap-3 items-end">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Course ID</label>
          <input
            v-model="courseId"
            type="text"
            placeholder="pebble-creek"
            class="border border-gray-300 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary-500"
          >
        </div>
        <button
          :disabled="roundsStore.loading || !courseId.trim()"
          class="bg-primary-600 text-white px-5 py-2 rounded-lg font-semibold hover:bg-primary-700 disabled:opacity-50 transition-colors"
          @click="onStartRound"
        >
          Start Round
        </button>
      </div>
      <p
        v-if="roundsStore.error"
        class="text-red-600 text-sm"
      >
        {{ roundsStore.error }}
      </p>
    </section>

    <!-- Active round panel -->
    <section
      v-else
      class="space-y-6"
    >
      <div class="flex items-center justify-between">
        <div>
          <span class="text-sm text-gray-500">Round #{{ roundsStore.activeRound.id }}</span>
          <span class="ml-3 text-sm text-gray-500">Course: {{ roundsStore.activeRound.courseId }}</span>
        </div>
        <button
          class="text-sm text-red-600 border border-red-300 px-3 py-1 rounded hover:bg-red-50 transition-colors"
          @click="roundsStore.endRound()"
        >
          End Round
        </button>
      </div>

      <!-- Log shot form -->
      <div class="border border-gray-200 rounded-lg p-4 space-y-4">
        <h3 class="font-semibold text-gray-800">
          Log Shot
        </h3>
        <div class="grid grid-cols-3 gap-3">
          <div>
            <label class="block text-xs text-gray-600 mb-1">Hole</label>
            <input
              v-model.number="shotForm.holeNumber"
              type="number"
              min="1"
              max="18"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
          <div>
            <label class="block text-xs text-gray-600 mb-1">Latitude</label>
            <input
              v-model.number="shotForm.lat"
              type="number"
              step="any"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
          <div>
            <label class="block text-xs text-gray-600 mb-1">Longitude</label>
            <input
              v-model.number="shotForm.lng"
              type="number"
              step="any"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
        </div>
        <button
          :disabled="roundsStore.loading || !shotFormValid"
          class="bg-primary-600 text-white px-4 py-2 rounded-lg text-sm font-semibold hover:bg-primary-700 disabled:opacity-50 transition-colors"
          @click="onLogShot"
        >
          Log Shot
        </button>
        <p
          v-if="roundsStore.error"
          class="text-red-600 text-sm"
        >
          {{ roundsStore.error }}
        </p>
      </div>

      <!-- Get advice form -->
      <div class="border border-gray-200 rounded-lg p-4 space-y-4">
        <h3 class="font-semibold text-gray-800">
          Get Advice
        </h3>
        <div class="grid grid-cols-2 sm:grid-cols-4 gap-3">
          <div>
            <label class="block text-xs text-gray-600 mb-1">Hole</label>
            <input
              v-model.number="adviceForm.holeNumber"
              type="number"
              min="1"
              max="18"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
          <div>
            <label class="block text-xs text-gray-600 mb-1">Latitude</label>
            <input
              v-model.number="adviceForm.lat"
              type="number"
              step="any"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
          <div>
            <label class="block text-xs text-gray-600 mb-1">Longitude</label>
            <input
              v-model.number="adviceForm.lng"
              type="number"
              step="any"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
          <div>
            <label class="block text-xs text-gray-600 mb-1">Shot # (opt)</label>
            <input
              v-model.number="adviceForm.shotNumber"
              type="number"
              min="1"
              class="w-full border border-gray-300 rounded px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500"
            >
          </div>
        </div>
        <button
          :disabled="roundsStore.loading || !adviceFormValid"
          class="bg-primary-700 text-white px-4 py-2 rounded-lg text-sm font-semibold hover:bg-primary-800 disabled:opacity-50 transition-colors"
          @click="onRequestAdvice"
        >
          Ask Caddie
        </button>
      </div>

      <!-- Advice result -->
      <div
        v-if="roundsStore.lastAdvice"
        class="border border-primary-200 bg-primary-50 rounded-lg p-4 space-y-2"
      >
        <div class="flex gap-4 text-sm text-gray-700">
          <span>Hole {{ roundsStore.lastAdvice.holeNumber }}</span>
          <span>Lie: {{ roundsStore.lastAdvice.lie }}</span>
          <span>Distance: {{ roundsStore.lastAdvice.distanceToPinYards.toFixed(0) }} yds</span>
        </div>
        <p class="text-gray-900">
          {{ roundsStore.lastAdvice.advice }}
        </p>
      </div>

      <!-- Shot log -->
      <div v-if="roundsStore.shots.length > 0">
        <h3 class="font-semibold text-gray-800 mb-3">
          Shots This Round
        </h3>
        <div class="overflow-x-auto">
          <table class="min-w-full text-sm border-collapse">
            <thead>
              <tr class="bg-gray-100 text-gray-600 text-left">
                <th class="px-3 py-2 font-medium">
                  Hole
                </th>
                <th class="px-3 py-2 font-medium">
                  #
                </th>
                <th class="px-3 py-2 font-medium">
                  Lie
                </th>
                <th class="px-3 py-2 font-medium">
                  Dist (yds)
                </th>
                <th class="px-3 py-2 font-medium">
                  Lat
                </th>
                <th class="px-3 py-2 font-medium">
                  Lng
                </th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="shot in roundsStore.shots"
                :key="shot.id"
                class="border-t border-gray-200 hover:bg-gray-50"
              >
                <td class="px-3 py-2">
                  {{ shot.holeNumber }}
                </td>
                <td class="px-3 py-2">
                  {{ shot.shotNumber }}
                </td>
                <td class="px-3 py-2">
                  {{ shot.lie }}
                </td>
                <td class="px-3 py-2">
                  {{ shot.distanceToPinYards.toFixed(0) }}
                </td>
                <td class="px-3 py-2">
                  {{ shot.lat.toFixed(5) }}
                </td>
                <td class="px-3 py-2">
                  {{ shot.lng.toFixed(5) }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, computed } from 'vue'
  import { useRoundsStore } from '@/stores/rounds'

  const roundsStore = useRoundsStore()

  const courseId = ref('pebble-creek')

  const shotForm = reactive<{ holeNumber: number; lat: number | null; lng: number | null }>({
    holeNumber: 1,
    lat: null,
    lng: null,
  })
  const adviceForm = reactive<{
    holeNumber: number
    lat: number | null
    lng: number | null
    shotNumber: number | undefined
  }>({
    holeNumber: 1,
    lat: null,
    lng: null,
    shotNumber: undefined,
  })

  function isCoordinate(value: number | null): value is number {
    return typeof value === 'number' && Number.isFinite(value)
  }

  const shotFormValid = computed(
    () => shotForm.holeNumber >= 1 && isCoordinate(shotForm.lat) && isCoordinate(shotForm.lng),
  )
  const adviceFormValid = computed(
    () => adviceForm.holeNumber >= 1 && isCoordinate(adviceForm.lat) && isCoordinate(adviceForm.lng),
  )

  async function onStartRound(): Promise<void> {
    try {
      await roundsStore.startRound(courseId.value.trim())
    } catch {
      // surfaced via roundsStore.error
    }
  }

  async function onLogShot(): Promise<void> {
    if (!isCoordinate(shotForm.lat) || !isCoordinate(shotForm.lng)) return
    try {
      await roundsStore.addShot({
        holeNumber: shotForm.holeNumber,
        lat: shotForm.lat,
        lng: shotForm.lng,
      })
      shotForm.lat = null
      shotForm.lng = null
    } catch {
      // surfaced via roundsStore.error
    }
  }

  async function onRequestAdvice(): Promise<void> {
    if (!isCoordinate(adviceForm.lat) || !isCoordinate(adviceForm.lng)) return
    try {
      await roundsStore.requestAdvice(
        adviceForm.holeNumber,
        adviceForm.lat,
        adviceForm.lng,
        adviceForm.shotNumber,
      )
    } catch {
      // surfaced via roundsStore.error
    }
  }
</script>
