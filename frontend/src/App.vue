<template>
  <div class="container-fluid">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">🏭 {{ $t("app.title") }}</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <button
                class="btn btn-sm btn-outline-light"
                @click="toggleLanguage"
              >
                {{ $i18n.locale === "en" ? "🇧🇷 PT" : "🇺🇸 EN" }}
              </button>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container-fluid mt-4">
      <ul class="nav nav-tabs mb-4">
        <li class="nav-item">
          <button
            class="nav-link"
            :class="{ active: activeTab === 'raw' }"
            @click="activeTab = 'raw'"
          >
            {{ $t("menu.rawMaterials") }}
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            :class="{ active: activeTab === 'prod' }"
            @click="activeTab = 'prod'"
          >
            {{ $t("menu.products") }}
          </button>
        </li>
        <li class="nav-item">
          <button
            class="nav-link"
            :class="{ active: activeTab === 'optimization' }"
            @click="activeTab = 'optimization'"
          >
            {{ $t("menu.optimization") }}
          </button>
        </li>
      </ul>

      <div class="tab-content">
        <div v-if="activeTab === 'raw'"><RawMaterialsTab /></div>
        <div v-if="activeTab === 'prod'"><ProductsTab /></div>
        <div v-if="activeTab === 'optimization'"><OptimizationTab /></div>
      </div>
    </div>
  </div>
</template>

<script>
import RawMaterialsTab from "./components/RawMaterialsTab.vue";
import ProductsTab from "./components/ProductsTab.vue";
import OptimizationTab from "./components/OptimizationTab.vue";

export default {
  name: "App",
  components: {
    RawMaterialsTab,
    ProductsTab,
    OptimizationTab,
  },
  data() {
    return {
      activeTab: "raw",
    };
  },
  methods: {
    toggleLanguage() {
      this.$i18n.locale = this.$i18n.locale === "en" ? "pt" : "en";
    },
  },
};
</script>

<style>
body {
  background-color: #f5f5f5;
}
</style>
