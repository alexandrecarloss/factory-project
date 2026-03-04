<template>
  <transition
    enter-active-class="transition duration-200 ease-out"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="transition duration-150 ease-in"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="show"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm"
    >
      <div
        class="bg-white rounded-2xl p-6 max-w-sm w-full shadow-2xl transform transition-all scale-100"
      >
        <h3 class="text-xl font-bold text-gray-900 mb-2">
          {{ title }}
        </h3>
        <p class="text-gray-600 mb-6">
          {{ message }}
        </p>
        <div class="flex justify-end gap-3">
          <button
            class="px-4 py-2 text-gray-500 hover:bg-gray-100 rounded-lg font-semibold transition-colors"
            @click="$emit('cancel')"
          >
            {{ $t("messages.cancel") }}
          </button>
          <button
            class="px-4 py-2 bg-red-600 text-white hover:bg-red-700 rounded-lg font-bold shadow-md transition-colors"
            @click="$emit('confirm')"
          >
            {{ $t("messages.confirm") }}
          </button>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: ["show", "title", "message"],
  emits: ['confirm', 'cancel']
};
</script>
