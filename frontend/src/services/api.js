import axios from "axios";

const API_BASE_URL = "/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const RawMaterialAPI = {
  getAll: () => api.get("/raw-materials"),
  getById: (id) => api.get(`/raw-materials/${id}`),
  create: (data) => api.post("/raw-materials", data),
  update: (id, data) => api.put(`/raw-materials/${id}`, data),
  delete: (id) => api.delete(`/raw-materials/${id}`),
};

export const ProductAPI = {
  getAll: () => api.get("/products"),
  getById: (id) => api.get(`/products/${id}`),
  create: (data) => api.post("/products", data),
  update: (id, data) => api.put(`/products/${id}`, data),
  delete: (id) => api.delete(`/products/${id}`),
};

export const ProductionAPI = {
  optimize: () => api.get("/production/optimize"),
};

export default api;
