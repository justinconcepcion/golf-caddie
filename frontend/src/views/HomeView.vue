<template>
  <div class="max-w-lg mx-auto py-16 px-4 text-center">
    <h1 class="text-4xl font-bold text-primary-700 mb-4">
      Golf Caddie
    </h1>
    <p class="text-gray-600 mb-8">
      AI-powered shot advice based on your position and bag.
    </p>
    <div class="flex flex-col sm:flex-row gap-4 justify-center">
      <RouterLink
        to="/round"
        class="inline-block bg-primary-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-primary-700 transition-colors"
      >
        Start Round
      </RouterLink>
      <RouterLink
        to="/profile"
        class="inline-block border border-primary-600 text-primary-600 px-6 py-3 rounded-lg font-semibold hover:bg-primary-50 transition-colors"
      >
        My Profile
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { RouterLink } from 'vue-router'
</script>
