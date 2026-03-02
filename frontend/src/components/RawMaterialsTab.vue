<template>
  <div class="space-y-6">
    <div
      class="flex justify-between items-center border-b pb-4 border-gray-100"
    >
      <h2 class="text-2xl font-bold text-gray-800">
        {{ $t("rawMaterials.title") }}
      </h2>
      <button
        class="bg-indigo-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-indigo-700 transition-all flex items-center gap-2 shadow-sm"
        @click="showForm = !showForm"
      >
        <span class="text-lg">{{ showForm ? "×" : "+" }}</span>
        {{ showForm ? $t("rawMaterials.cancel") : $t("rawMaterials.add") }}
      </button>
    </div>

    <div
      v-if="showForm"
      class="bg-gray-50 border border-indigo-100 p-6 rounded-2xl shadow-inner"
    >
      <form
        class="grid grid-cols-1 md:grid-cols-2 gap-6"
        @submit.prevent="saveRawMaterial"
      >
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("rawMaterials.code")
            }}</label>
            <input
              v-model="form.code"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              required
            >
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("rawMaterials.name")
            }}</label>
            <input
              v-model="form.name"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              required
            >
          </div>
        </div>

        <div class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-1">{{
                $t("rawMaterials.stockQuantity")
              }}</label>
              <input
                v-model.number="form.stockQuantity"
                type="number"
                class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
                required
              >
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-1">{{
                $t("rawMaterials.unit")
              }}</label>
              <input
                v-model="form.unit"
                class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
                required
              >
            </div>
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("rawMaterials.description")
            }}</label>
            <textarea
              v-model="form.description"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              rows="1"
            />
          </div>
        </div>

        <div class="col-span-full flex gap-3 pt-4 border-t border-gray-200">
          <button
            type="submit"
            class="bg-green-600 text-white px-8 py-2 rounded-lg font-bold hover:bg-green-700 transition-all shadow-md"
          >
            {{ $t("rawMaterials.save") }}
          </button>
        </div>
      </form>
    </div>

    <div
      class="overflow-hidden border border-gray-200 rounded-xl bg-white shadow-sm"
    >
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th
              class="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider italic"
            >
              Item
            </th>
            <th
              class="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider"
            >
              {{ $t("rawMaterials.stockQuantity") }}
            </th>
            <th
              class="px-6 py-3 text-right text-xs font-bold text-gray-500 uppercase tracking-wider"
            >
              {{ $t("rawMaterials.actions") }}
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr
            v-for="material in materials"
            :key="material.id"
            class="hover:bg-gray-50/50 transition-colors"
          >
            <td class="px-6 py-4">
              <div class="font-bold text-gray-900">
                {{ material.name }}
              </div>
              <div class="text-xs text-gray-400 font-mono">
                {{ material.code }}
              </div>
            </td>
            <td class="px-6 py-4">
              <span
                class="px-3 py-1 rounded-full text-sm font-bold bg-indigo-50 text-indigo-700 border border-indigo-100"
              >
                {{ material.stockQuantity }} {{ material.unit }}
              </span>
            </td>
            <td class="px-6 py-4 text-right space-x-2">
              <button
                class="text-blue-600 hover:bg-blue-50 p-2 rounded-lg transition-colors"
                @click="editMaterial(material)"
              >
                ✏️
              </button>
              <button
                class="text-red-600 hover:bg-red-50 p-2 rounded-lg transition-colors"
                @click="deleteMaterial(material.id)"
              >
                🗑
              </button>
            </td>
          </tr>
        </tbody>
      </table>
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
      form: { code: "", name: "", stockQuantity: 0, unit: "", description: "" },
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
        console.error(error);
      }
    },
    async saveRawMaterial() {
      try {
        if (this.editingId)
          await RawMaterialAPI.update(this.editingId, this.form);
        else await RawMaterialAPI.create(this.form);
        this.resetForm();
        this.loadMaterials();
        alert(this.$t("messages.success"));
      } catch (error) {
        alert(this.$t("messages.error"));
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
        } catch (error) {
          alert(this.$t("messages.error"));
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
