import { describe, it, expect, beforeEach, vi } from "vitest";
import { mount } from "@vue/test-utils";
import ProductsTab from "../components/ProductsTab.vue";
import * as api from "../services/api";

vi.mock("../services/api");

describe("ProductsTab.vue", () => {
  let wrapper;

  beforeEach(() => {
    api.ProductAPI.getAll.mockResolvedValue({
      data: [{ id: 1, code: "BREAD", name: "Bread", price: 10.0, description: "White bread", composition: [] }],
    });

    api.RawMaterialAPI.getAll.mockResolvedValue({
      data: [{ id: 1, code: "FLOUR", name: "Flour", stockQuantity: 1000, unit: "g" }],
    });

    wrapper = mount(ProductsTab, {
      global: {
        mocks: { $t: (key) => key },
      },
    });
  });

  it("renders component with add button", () => {
    expect(wrapper.find("button.btn-add-product").exists()).toBe(true);
  });

  it("loads products on mount", async () => {
    await wrapper.vm.$nextTick();
    expect(wrapper.vm.products.length).toBeGreaterThan(0);
  });

  it("adds composition field", async () => {
    const initialLength = wrapper.vm.form.composition.length;
    wrapper.vm.addComposition();
    expect(wrapper.vm.form.composition.length).toBe(initialLength + 1);
  });

  it("removes composition field", async () => {
    wrapper.vm.form.composition.push({ rawMaterialId: 1, quantityRequired: 100 });
    const initialLength = wrapper.vm.form.composition.length;
    wrapper.vm.removeComposition(0);
    expect(wrapper.vm.form.composition.length).toBe(initialLength - 1);
  });
});