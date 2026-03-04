<template>
  <div class="min-h-screen bg-gray-50 font-sans antialiased text-gray-900">
    <header
      class="bg-white border-b border-gray-200 sticky top-0 z-10 shadow-sm"
    >
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center gap-2">
            <span class="text-2xl">🏭</span>
            <h1
              class="text-xl font-bold tracking-tight text-indigo-600 uppercase"
            >
              {{ $t("app.title") }}
            </h1>
          </div>

          <button
            class="inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 transition-all"
            @click="toggleLanguage"
          >
            {{ $i18n.locale === "en" ? "🇧🇷 PT" : "🇺🇸 EN" }}
          </button>
        </div>
      </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <nav class="flex space-x-2 bg-gray-200/50 p-1 rounded-xl mb-8 w-fit">
        <button
          v-for="tab in tabs"
          :key="tab.id"
          :class="[
            activeTab === tab.id
              ? 'bg-white text-indigo-600 shadow-sm'
              : 'text-gray-500 hover:text-gray-700 hover:bg-gray-100',
            'px-4 py-2 text-sm font-semibold rounded-lg transition-all',
          ]"
          @click="activeTab = tab.id"
        >
          {{ $t(tab.label) }}
        </button>
      </nav>

      <div
        class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 min-h-[500px]"
      >
        <transition
          mode="out-in"
          enter-active-class="transition duration-200 ease-out"
          enter-from-class="opacity-0 translate-y-2"
          enter-to-class="opacity-100 translate-y-0"
          leave-active-class="transition duration-150 ease-in"
          leave-from-class="opacity-100 translate-y-0"
          leave-to-class="opacity-0 translate-y-2"
        >
          <component
            :is="activeComponent"
            :key="activeTab"
            :exchange-rate="exchangeRate"
            @notify="triggerToast"
          />
        </transition>
      </div>
    </main>

    <ToastNotification ref="toast" />
  </div>
</template>

<script>
import RawMaterialsTab from "./components/RawMaterialsTab.vue";
import ProductsTab from "./components/ProductsTab.vue";
import OptimizationTab from "./components/OptimizationTab.vue";
import ToastNotification from "./components/ToastNotification.vue";
import { CurrencyService } from "./services/currency";

export default {
  name: "App",
  components: {
    RawMaterialsTab,
    ProductsTab,
    OptimizationTab,
    ToastNotification,
  },

  data() {
    return {
      activeTab: "raw",
      tabs: [
        { id: "raw", label: "menu.rawMaterials" },
        { id: "prod", label: "menu.products" },
        { id: "optimization", label: "menu.optimization" },
      ],
      exchangeRate: 1,
    };
  },
  computed: {
    activeComponent() {
      const map = {
        raw: "RawMaterialsTab",
        prod: "ProductsTab",
        optimization: "OptimizationTab",
      };
      return map[this.activeTab];
    },
  },
  async mounted() {
    this.exchangeRate = await CurrencyService.getUsdBrlRate();
  },
  methods: {
    toggleLanguage() {
      this.$i18n.locale = this.$i18n.locale === "en" ? "pt" : "en";
    },
    triggerToast({ message, type }) {
      this.$refs.toast.add(message, type);
    },
  },
};
</script>
