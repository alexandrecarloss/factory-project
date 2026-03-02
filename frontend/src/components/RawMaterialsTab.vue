<template>
  <div class="card">
    <div class="card-header">
      <h5>{{ $t("rawMaterials.title") }}</h5>
    </div>
    <div class="card-body">
      <button class="btn btn-primary mb-3" @click="showForm = !showForm">
        {{ $t("rawMaterials.add") }}
      </button>

      <!-- Form -->
      <div v-if="showForm" class="card mb-3">
        <div class="card-body">
          <form @submit.prevent="saveRawMaterial">
            <div class="mb-3">
              <label class="form-label">{{ $t("rawMaterials.code") }}</label>
              <input v-model="form.code" class="form-control" required />
            </div>
            <div class="mb-3">
              <label class="form-label">{{ $t("rawMaterials.name") }}</label>
              <input v-model="form.name" class="form-control" required />
            </div>
            <div class="mb-3">
              <label class="form-label">{{
                $t("rawMaterials.stockQuantity")
              }}</label>
              <input
                v-model.number="form.stockQuantity"
                class="form-control"
                type="number"
                required
              />
            </div>
            <div class="mb-3">
              <label class="form-label">{{ $t("rawMaterials.unit") }}</label>
              <input v-model="form.unit" class="form-control" required />
            </div>
            <div class="mb-3">
              <label class="form-label">{{
                $t("rawMaterials.description")
              }}</label>
              <textarea
                v-model="form.description"
                class="form-control"
              ></textarea>
            </div>
            <button type="submit" class="btn btn-success">
              {{ $t("rawMaterials.save") }}
            </button>
            <button type="button" class="btn btn-secondary" @click="resetForm">
              {{ $t("rawMaterials.cancel") }}
            </button>
          </form>
        </div>
      </div>

      <!-- List -->
      <div v-if="materials.length > 0" class="table-responsive">
        <table class="table table-striped">
          <thead>
            <tr>
              <th>{{ $t("rawMaterials.code") }}</th>
              <th>{{ $t("rawMaterials.name") }}</th>
              <th>{{ $t("rawMaterials.stockQuantity") }}</th>
              <th>{{ $t("rawMaterials.unit") }}</th>
              <th>{{ $t("rawMaterials.actions") }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="material in materials" :key="material.id">
              <td>{{ material.code }}</td>
              <td>{{ material.name }}</td>
              <td>{{ material.stockQuantity }}</td>
              <td>{{ material.unit }}</td>
              <td>
                <button
                  class="btn btn-sm btn-info me-2"
                  @click="editMaterial(material)"
                >
                  {{ $t("rawMaterials.edit") }}
                </button>
                <button
                  class="btn btn-sm btn-danger"
                  @click="deleteMaterial(material.id)"
                >
                  {{ $t("rawMaterials.delete") }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else class="alert alert-info">No raw materials found</div>
    </div>
  </div>
</template>

<script>
import { RawMaterialAPI } from "../services/api";

export default {
  name: "RawMaterialsTab",
  data() {
    return {
      materials: [],
      showForm: false,
      form: {
        code: "",
        name: "",
        stockQuantity: 0,
        unit: "",
        description: "",
      },
      editingId: null,
    };
  },
  mounted() {
    this.loadMaterials();
  },
  methods: {
    async loadMaterials() {
      try {
        const response = await RawMaterialAPI.getAll();
        this.materials = response.data;
      } catch (error) {
        console.error("Error loading materials:", error);
        alert("Error loading materials");
      }
    },
    async saveRawMaterial() {
      try {
        if (this.editingId) {
          await RawMaterialAPI.update(this.editingId, this.form);
        } else {
          await RawMaterialAPI.create(this.form);
        }
        this.resetForm();
        this.loadMaterials();
        alert(this.$t("messages.success"));
      } catch (error) {
        console.error("Error saving material:", error);
        alert("Error saving material");
      }
    },
    editMaterial(material) {
      this.form = { ...material };
      this.editingId = material.id;
      this.showForm = true;
    },
    async deleteMaterial(id) {
      if (confirm(this.$t("messages.confirmDelete"))) {
        try {
          await RawMaterialAPI.delete(id);
          this.loadMaterials();
          alert(this.$t("messages.success"));
        } catch (error) {
          console.error("Error deleting material:", error);
          alert("Error deleting material");
        }
      }
    },
    resetForm() {
      this.form = {
        code: "",
        name: "",
        stockQuantity: 0,
        unit: "",
        description: "",
      };
      this.editingId = null;
      this.showForm = false;
    },
  },
};
</script>
