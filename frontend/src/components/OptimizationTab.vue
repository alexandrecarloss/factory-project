<template>
  <div class="card">
    <div class="card-header">
      <h5>{{ $t("optimization.title") }}</h5>
    </div>
    <div class="card-body">
      <button class="btn btn-lg btn-success mb-4" @click="optimize">
        {{ $t("optimization.optimize") }}
      </button>

      <!-- Results -->
      <div v-if="lastResult" class="mb-4">
        <div class="alert alert-info">
          {{ lastResult.message }}
        </div>

        <div v-if="lastResult.suggestions.length > 0">
          <h6>{{ $t("optimization.suggestions") }}</h6>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>{{ $t("optimization.productCode") }}</th>
                  <th>{{ $t("optimization.productName") }}</th>
                  <th>{{ $t("optimization.unitPrice") }}</th>
                  <th>{{ $t("optimization.quantityToProce") }}</th>
                  <th>{{ $t("optimization.totalValue") }}</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="suggestion in lastResult.suggestions"
                  :key="suggestion.productId"
                >
                  <td>{{ suggestion.productCode }}</td>
                  <td>{{ suggestion.productName }}</td>
                  <td>${{ suggestion.price.toFixed(2) }}</td>
                  <td>{{ suggestion.quantityToProduce }}</td>
                  <td class="fw-bold">
                    ${{ suggestion.totalValue.toFixed(2) }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="alert alert-success mt-3">
            <strong>{{ $t("optimization.totalProduction") }}:</strong> ${{
              lastResult.totalValue.toFixed(2)
            }}
          </div>
        </div>
        <div v-else class="alert alert-warning">
          {{ $t("optimization.noResults") }}
        </div>
      </div>

      <div v-if="loading" class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
</template>

<script>
import { ProductionAPI } from "../services/api";

export default {
  name: "OptimizationTab",
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
  },
};
</script>
