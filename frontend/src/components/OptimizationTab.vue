<template>
  <div class="space-y-6">
    <div
      class="flex justify-between items-center border-b pb-4 border-gray-100"
    >
      <h2 class="text-2xl font-bold text-gray-800">
        {{ $t("optimization.title") }}
      </h2>
      <button
        class="btn-optimize inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-xl shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 transition-all"
        :disabled="loading"
        @click="optimize"
      >
        <span
          v-if="loading"
          class="mr-2 animate-spin text-lg italic inline-block"
        >
          ◌
        </span>
        {{ loading ? $t("common.loading") : $t("optimization.optimize") }}
      </button>
    </div>

    <div v-if="lastResult" class="animate-fade-in space-y-6">
      <div class="bg-blue-50 border-l-4 border-blue-400 p-4 rounded-r-lg">
        <p class="text-sm text-blue-700 font-medium">
          {{ lastResult.message }}
        </p>
      </div>

      <div v-if="lastResult.suggestions.length > 0" class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-700 px-1">
          {{ $t("optimization.suggestions") }}
        </h3>

        <div
          class="overflow-hidden border border-gray-200 rounded-xl shadow-sm"
        >
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th
                  class="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider"
                >
                  {{ $t("optimization.productName") }}
                </th>
                <th
                  class="px-6 py-3 text-center text-xs font-bold text-gray-500 uppercase tracking-wider"
                >
                  {{ $t("optimization.quantityToProduce") }}
                </th>
                <th
                  class="px-6 py-3 text-right text-xs font-bold text-gray-500 uppercase tracking-wider"
                >
                  {{ $t("optimization.totalValue") }}
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr
                v-for="suggestion in lastResult.suggestions"
                :key="suggestion.productId"
                class="hover:bg-gray-50 transition-colors"
              >
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">
                    {{ suggestion.productName }}
                  </div>
                  <div class="text-xs text-gray-500">
                    {{ suggestion.productCode }}
                  </div>
                </td>
                <td
                  class="px-6 py-4 whitespace-nowrap text-center text-sm text-gray-600 font-mono"
                >
                  {{ suggestion.quantityToProduce }}
                </td>
                <td
                  class="px-6 py-4 whitespace-nowrap text-right text-sm font-bold text-indigo-600"
                >
                  {{ formatValue(lastResult.totalValue) }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div
          class="bg-indigo-600 rounded-xl p-6 text-white shadow-lg shadow-indigo-200 flex justify-between items-center"
        >
          <span class="text-indigo-100 font-medium">{{
            $t("optimization.totalProduction")
          }}</span>
          <span class="text-3xl font-black">
            {{ formatValue(lastResult.totalValue) }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ProductionAPI } from "../services/api";

export default {
  name: "OptimizationTab",
  props: ['exchangeRate'],
  data() {
    return {
      loading: false,
      lastResult: null,
    };
  },
  methods: {
    async optimize() {
      this.loading = true;
      try {
        const response = await ProductionAPI.optimize();
        this.lastResult = response.data;
      } catch (error) {
        console.error("Error optimizing production:", error);
        alert("Error optimizing production");
      } finally {
        this.loading = false;
      }
    },
    formatValue(value) {
      if (!value) value = 0;
      
      const converted = this.$i18n.locale === 'pt' 
        ? value * (this.exchangeRate || 5.21)
        : value;

      return this.$n(converted, 'currency');
    },
  },
};
</script>
