<template>
  <div class="card">
    <div class="card-header">
      <h5>{{ $t("products.title") }}</h5>
    </div>
    <div class="card-body">
      <button class="btn btn-primary mb-3" @click="showForm = !showForm">
        {{ $t("products.add") }}
      </button>

      <!-- Form -->
      <div v-if="showForm" class="card mb-3">
        <div class="card-body">
          <form @submit.prevent="saveProduct">
            <div class="mb-3">
              <label class="form-label">{{ $t("products.code") }}</label>
              <input v-model="form.code" class="form-control" required />
            </div>
            <div class="mb-3">
              <label class="form-label">{{ $t("products.name") }}</label>
              <input v-model="form.name" class="form-control" required />
            </div>
            <div class="mb-3">
              <label class="form-label">{{ $t("products.price") }}</label>
              <input
                v-model.number="form.price"
                class="form-control"
                type="number"
                step="0.01"
                required
              />
            </div>
            <div class="mb-3">
              <label class="form-label">{{ $t("products.description") }}</label>
              <textarea
                v-model="form.description"
                class="form-control"
              ></textarea>
            </div>

            <!-- Composition -->
            <div class="mb-3">
              <h6>{{ $t("products.composition") }}</h6>
              <div
                v-for="(comp, index) in form.composition"
                :key="index"
                class="mb-2"
              >
                <div class="row">
                  <div class="col-md-6">
                    <select
                      v-model.number="comp.rawMaterialId"
                      class="form-select"
                    >
                      <option value="">Select Raw Material</option>
                      <option
                        v-for="material in rawMaterials"
                        :key="material.id"
                        :value="material.id"
                      >
                        {{ material.name }}
                      </option>
                    </select>
                  </div>
                  <div class="col-md-4">
                    <input
                      v-model.number="comp.quantityRequired"
                      class="form-control"
                      type="number"
                      placeholder="Quantity"
                    />
                  </div>
                  <div class="col-md-2">
                    <button
                      type="button"
                      class="btn btn-sm btn-danger"
                      @click="removeComposition(index)"
                    >
                      Remove
                    </button>
                  </div>
                </div>
              </div>
              <button
                type="button"
                class="btn btn-sm btn-secondary"
                @click="addComposition"
              >
                {{ $t("products.addComposition") }}
              </button>
            </div>

            <button type="submit" class="btn btn-success">
              {{ $t("products.save") }}
            </button>
            <button type="button" class="btn btn-secondary" @click="resetForm">
              {{ $t("products.cancel") }}
            </button>
          </form>
        </div>
      </div>

      <!-- List -->
      <div v-if="products.length > 0" class="table-responsive">
        <table class="table table-striped">
          <thead>
            <tr>
              <th>{{ $t("products.code") }}</th>
              <th>{{ $t("products.name") }}</th>
              <th>{{ $t("products.price") }}</th>
              <th>{{ $t("products.composition") }}</th>
              <th>{{ $t("products.actions") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="product in products" :key="product.id">
              <td>{{ product.code }}</td>
              <td>{{ product.name }}</td>
              <td>${{ product.price.toFixed(2) }}</td>
              <td>
                <small v-for="comp in product.composition" :key="comp.id">
                  {{ comp.rawMaterialName }} ({{ comp.quantityRequired }})<br />
                </small>
              </td>
              <td>
                <button
                  class="btn btn-sm btn-info me-2"
                  @click="editProduct(product)"
                >
                  {{ $t("products.edit") }}
                </button>
                <button
                  class="btn btn-sm btn-danger"
                  @click="deleteProduct(product.id)"
                >
                  {{ $t("products.delete") }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else class="alert alert-info">No products found</div>
    </div>
  </div>
</template>

<script>
import { ProductAPI, RawMaterialAPI } from "../services/api";

export default {
  name: "ProductsTab",
  data() {
    return {
      products: [],
      rawMaterials: [],
      showForm: false,
      form: {
        code: "",
        name: "",
        price: 0,
        description: "",
        composition: [],
      },
      editingId: null,
    };
  },
  mounted() {
    this.loadProducts();
    this.loadRawMaterials();
  },
  methods: {
    async loadProducts() {
      try {
        const response = await ProductAPI.getAll();
        this.products = response.data;
      } catch (error) {
        console.error("Error loading products:", error);
        alert("Error loading products");
      }
    },
    async loadRawMaterials() {
      try {
        const response = await RawMaterialAPI.getAll();
        this.rawMaterials = response.data;
      } catch (error) {
        console.error("Error loading materials:", error);
      }
    },
    async saveProduct() {
      try {
        if (this.editingId) {
          await ProductAPI.update(this.editingId, this.form);
        } else {
          await ProductAPI.create(this.form);
        }
        this.resetForm();
        this.loadProducts();
        alert(this.$t("messages.success"));
      } catch (error) {
        console.error("Error saving product:", error);
        alert("Error saving product");
      }
    },
    editProduct(product) {
      this.form = {
        ...product,
        composition: product.composition ? [...product.composition] : [],
      };
      this.editingId = product.id;
      this.showForm = true;
    },
    async deleteProduct(id) {
      if (confirm(this.$t("messages.confirmDelete"))) {
        try {
          await ProductAPI.delete(id);
          this.loadProducts();
          alert(this.$t("messages.success"));
        } catch (error) {
          console.error("Error deleting product:", error);
          alert("Error deleting product");
        }
      }
    },
    addComposition() {
      this.form.composition.push({
        rawMaterialId: null,
        quantityRequired: 0,
      });
    },
    removeComposition(index) {
      this.form.composition.splice(index, 1);
    },
    resetForm() {
      this.form = {
        code: "",
        name: "",
        price: 0,
        description: "",
        composition: [],
      };
      this.editingId = null;
      this.showForm = false;
    },
  },
};
</script>
