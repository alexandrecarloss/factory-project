<template>
  <div class="fixed bottom-5 right-5 z-50 space-y-3">
    <transition-group 
      enter-active-class="transform transition duration-300 ease-out"
      enter-from-class="translate-y-10 opacity-0"
      enter-to-class="translate-y-0 opacity-100"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div 
        v-for="toast in toasts" 
        :key="toast.id"
        :class="[
          'px-6 py-3 rounded-xl shadow-lg text-white font-medium flex items-center gap-3 min-w-[250px]',
          toast.type === 'success' ? 'bg-green-600' : 'bg-red-600'
        ]"
      >
        <span>{{ toast.type === 'success' ? '✅' : '❌' }}</span>
        {{ toast.message }}
      </div>
    </transition-group>
  </div>
</template>

<script>
export default {
  data() {
    return { toasts: [] };
  },
  methods: {
    add(message, type = 'success') {
      const id = Date.now();
      this.toasts.push({ id, message, type });
      setTimeout(() => {
        this.toasts = this.toasts.filter(t => t.id !== id);
      }, 3000);
    }
  }
};
</script>