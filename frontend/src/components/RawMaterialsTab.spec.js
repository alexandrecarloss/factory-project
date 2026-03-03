import { describe, it, expect, beforeEach, vi } from "vitest";
import { mount } from "@vue/test-utils";
import RawMaterialsTab from "../components/RawMaterialsTab.vue";
import * as api from "../services/api";

vi.mock("../services/api");

describe("RawMaterialsTab.vue", () => {
  let wrapper;

  beforeEach(() => {
    api.RawMaterialAPI.getAll.mockResolvedValue({
      data: [{ id: 1, code: "FLOUR", name: "Flour", stockQuantity: 1000, unit: "g", description: "Wheat flour" }],
    });

    wrapper = mount(RawMaterialsTab, {
      global: {
        mocks: { $t: (key) => key },
      },
    });
  });

  it("renders component with add button", () => {
    expect(wrapper.find("button.btn-add-rawMaterial").exists()).toBe(true);
  });

  it("toggles form visibility", async () => {
    const button = wrapper.find("button.btn-add-rawMaterial");
    expect(wrapper.vm.showForm).toBe(false);

    await button.trigger("click");
    expect(wrapper.vm.showForm).toBe(true);
  });

  it("loads raw materials on mount", async () => {
    await wrapper.vm.$nextTick();
    expect(wrapper.vm.materials.length).toBeGreaterThan(0);
  });
});