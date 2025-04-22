import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080/api/v1",
  headers: {
    "Content-Type": "application/json",
    "X-USER-ID": "test-user-123",
  },
});

export const getMemberships = () => API.get("/memberships");
export const getMembership = (id) => API.get(`/memberships/${id}`);
export const addMembership = (data) => API.post("/memberships", data);
export const deleteMembership = (id) => API.delete(`/memberships/${id}`);
export const addPoint = (id, data) => API.patch(`/memberships/${id}`, data);
