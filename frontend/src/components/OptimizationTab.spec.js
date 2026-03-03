import { describe, it, expect, beforeEach, vi } from "vitest";
import { mount } from "@vue/test-utils";
import OptimizationTab from "../components/OptimizationTab.vue";
import * as api from "../services/api";

vi.mock("../services/api");

describe("OptimizationTab.vue", () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(OptimizationTab, {
      global: {
        mocks: { $t: (key) => key },
      },
    });
  });

  it("renders component with optimize button", () => {
    expect(wrapper.find("button.btn-optimize").exists()).toBe(true);
  });

  it("shows loading spinner while optimizing", async () => {
    let resolveApi;
    const delayedPromise = new Promise((resolve) => { resolveApi = resolve; });

    api.ProductionAPI.optimize.mockReturnValue(delayedPromise);

    await wrapper.find("button.btn-optimize").trigger("click");

    expect(wrapper.vm.loading).toBe(true);

    resolveApi({ data: { suggestions: [], totalValue: 0, message: "Done" } });
    await wrapper.vm.$nextTick();
    await wrapper.vm.$nextTick();
    expect(wrapper.vm.loading).toBe(false);
  });

  it("displays results after optimization", async () => {
    const mockData = {
      suggestions: [{ productId: 1, productCode: "BREAD", productName: "Bread", price: 10.0, quantityToProduce: 5, totalValue: 50.0 }],
      totalValue: 50.0,
      message: "Production optimized successfully",
    };

    api.ProductionAPI.optimize.mockResolvedValue({ data: mockData });

    await wrapper.find("button.btn-optimize").trigger("click");
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.lastResult).toEqual(mockData);
    expect(wrapper.text()).toContain("Production optimized successfully");
  });
});