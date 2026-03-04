<template>
  <div class="space-y-6">
    <div
      class="flex justify-between items-center border-b pb-4 border-gray-100"
    >
      <div class="space-y-6">
        <ConfirmModal
          :show="showConfirm"
          :title="$t('messages.confirmDeleteTitle')"
          :message="$t('messages.confirmDelete')"
          @cancel="showConfirm = false"
          @confirm="handleDelete"
        />
      </div>
      <h2 class="text-2xl font-bold text-gray-800">
        {{ $t("products.title") }}
      </h2>
      <button
        class="btn-add-product bg-indigo-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-indigo-700 transition-all flex items-center gap-2 shadow-sm"
        @click="showForm = !showForm"
      >
        <span class="text-lg">{{ showForm ? "×" : "+" }}</span>
        {{ showForm ? $t("products.cancel") : $t("products.add") }}
      </button>
    </div>

    <div
      v-if="showForm"
      class="bg-gray-50 border border-indigo-100 p-6 rounded-2xl shadow-inner"
    >
      <form
        class="grid grid-cols-1 md:grid-cols-2 gap-6"
        @submit.prevent="saveProduct"
      >
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("products.code")
            }}</label>
            <input
              v-model="form.code"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              required
            >
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("products.name")
            }}</label>
            <input
              v-model="form.name"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              required
            >
          </div>
        </div>

        <div class="space-y-4">
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("products.price")
            }}</label>
            <input
              v-model.number="form.price"
              type="number"
              step="0.01"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              required
            >
          </div>
          <div>
            <label class="block text-sm font-bold text-gray-700 mb-1">{{
              $t("products.description")
            }}</label>
            <textarea
              v-model="form.description"
              class="w-full px-4 py-2 rounded-lg border-gray-300 border focus:ring-2 focus:ring-indigo-500 outline-none transition-all"
              rows="1"
            />
          </div>
        </div>

        <div
          class="col-span-full bg-white p-4 rounded-xl border border-gray-200"
        >
          <div class="flex justify-between items-center mb-4">
            <h4 class="font-bold text-gray-700">
              {{ $t("products.composition") }}
            </h4>
            <button
              type="button"
              class="bg-indigo-50 text-indigo-600 px-3 py-1 rounded-md text-sm font-bold hover:bg-indigo-100 transition-colors"
              @click="addComposition"
            >
              + {{ $t("products.addComposition") }}
            </button>
          </div>

          <div
            v-for="(comp, index) in form.composition"
            :key="index"
            class="flex gap-3 mb-3 items-center bg-gray-50 p-2 rounded-lg"
          >
            <div class="flex-1">
              <label
                class="block text-[10px] uppercase font-bold text-gray-400 ml-1"
              >
                {{ $t("products.rawMaterial") }}
              </label>
              <select
                v-model.number="comp.rawMaterialId"
                class="w-full bg-transparent px-2 py-1 focus:outline-none text-sm"
                required
              >
                <option 
                  :value="null" 
                  disabled
                >
                  {{ $t("products.rawMaterial") }}...
                </option>
                <option 
                  v-for="m in rawMaterials" 
                  :key="m.id" 
                  :value="m.id"
                >
                  {{ m.name }}
                </option>
              </select>
            </div>
            <div class="w-32 border-l border-gray-200 pl-3">
              <label
                class="block text-[10px] uppercase font-bold text-gray-400 ml-1"
              >
                {{ $t("products.quantity") }}
              </label>
              <input
                v-model.number="comp.quantityRequired"
                type="number"
                step="0.1"
                class="w-full bg-transparent px-2 py-1 focus:outline-none text-sm"
                required
              >
            </div>
            <button
              type="button"
              class="text-red-400 hover:text-red-600 p-2 transition-colors"
              @click="removeComposition(index)"
            >
              🗑
            </button>
          </div>
        </div>

        <div class="col-span-full flex gap-3 pt-4 border-t border-gray-200">
          <button
            type="submit"
            class="bg-green-600 text-white px-8 py-2 rounded-lg font-bold hover:bg-green-700 transition-all shadow-md"
          >
            {{ $t("products.save") }}
          </button>
          <button
            type="button"
            class="text-gray-500 px-4 py-2 hover:bg-gray-100 rounded-lg transition-all"
            @click="resetForm"
          >
            {{ $t("products.cancel") }}
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
              {{ $t("products.price") }}
            </th>
            <th
              class="px-6 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider"
            >
              {{ $t("products.composition") }}
            </th>
            <th
              class="px-6 py-3 text-right text-xs font-bold text-gray-500 uppercase tracking-wider"
            >
              {{ $t("products.actions") }}
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr
            v-for="product in products"
            :key="product.id"
            class="hover:bg-gray-50/50 transition-colors"
          >
            <td class="px-6 py-4">
              <div class="font-bold text-gray-900">
                {{ product.name }}
              </div>
              <div class="text-xs text-gray-400 font-mono">
                {{ product.code }}
              </div>
            </td>
            <td class="px-6 py-4 text-sm font-mono text-gray-700 font-semibold">
              ${{ product.price.toFixed(2) }}
            </td>
            <td class="px-6 py-4">
              <div class="flex flex-wrap gap-1">
                <span
                  v-for="c in product.composition"
                  :key="c.id"
                  class="bg-indigo-50 text-indigo-700 text-[10px] px-2 py-0.5 rounded-full border border-indigo-100 uppercase font-bold"
                >
                  {{ c.rawMaterialName }}: {{ c.quantityRequired }}
                </span>
              </div>
            </td>
            <td class="px-6 py-4 text-right space-x-2">
              <button
                class="text-blue-600 hover:bg-blue-50 p-2 rounded-lg transition-colors"
                @click="editProduct(product)"
              >
                ✏️
              </button>
              <button
                class="text-red-600 hover:bg-red-50 p-2 rounded-lg transition-colors"
                @click="confirmDelete(product.id)"
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
import { ProductAPI, RawMaterialAPI } from "../services/api";
import ConfirmModal from "./ConfirmModal.vue";

export default {
  name: "ProductsTab",
  components: { ConfirmModal },
emits: ['notify'],
  data() {
    return {
      products: [],
      rawMaterials: [],
      showForm: false,
      form: { code: "", name: "", price: 0, description: "", composition: [] },
      editingId: null,
      showConfirm: false,
      itemToDelete: null,
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
        console.error(error);
      }
    },
    async loadRawMaterials() {
      try {
        const response = await RawMaterialAPI.getAll();
        this.rawMaterials = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async saveProduct() {
      try {
        if (this.editingId) await ProductAPI.update(this.editingId, this.form);
        else await ProductAPI.create(this.form);
        
        this.resetForm();
        this.loadProducts();
        
        this.$emit('notify', { message: this.$t("messages.product.successSaveProduct"), type: 'success' });
      } catch (error) {
        this.$emit('notify', { message: this.$t("messages.product.errorSaveProduct"), type: 'error' });
      }
    },
    confirmDelete(id) {
      this.itemToDelete = id;
      this.showConfirm = true;
    },
    editProduct(product) {
      this.form = {
        ...product,
        composition: product.composition ? [...product.composition] : [],
      };
      this.editingId = product.id;
      this.showForm = true;
    },
    async handleDelete() {
      try {
        await ProductAPI.delete(this.itemToDelete);
        this.loadProducts();
        this.$emit('notify', { message: this.$t("messages.product.successDeleteProduct"), type: 'success' });
      } catch (e) {
        this.$emit('notify', { message: this.$t("messages.product.errorDeleteProduct"), type: 'error' });
      } finally {
        this.showConfirm = false;
        this.itemToDelete = null;
      }
    },
    addComposition() {
      this.form.composition.push({ rawMaterialId: null, quantityRequired: 0 });
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
