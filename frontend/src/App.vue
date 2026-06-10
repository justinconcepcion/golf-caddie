<template>
  <div class="min-h-full flex flex-col">
    <nav class="bg-primary-700 text-white px-4 py-3 flex items-center gap-6">
      <RouterLink
        to="/"
        class="font-bold text-lg tracking-tight hover:text-primary-100"
      >
        Golf Caddie
      </RouterLink>
      <RouterLink
        to="/round"
        class="text-sm hover:text-primary-100"
      >
        Round
      </RouterLink>
      <RouterLink
        to="/profile"
        class="text-sm hover:text-primary-100"
      >
        Profile
      </RouterLink>
    </nav>
    <main class="flex-1">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
  import { RouterLink, RouterView } from 'vue-router'
</script>
