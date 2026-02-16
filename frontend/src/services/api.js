import axios from 'axios';

const API_URL = '/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Añadir interceptor para inyectar Authorization desde localStorage
api.interceptors.request.use((config) => {
    try {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers = config.headers || {};
            config.headers['Authorization'] = `Bearer ${token}`;
        }
    } catch (e) {
        // no-op en entornos sin localStorage
    }
    return config;
});
// Planes
export const getPlanes = () => api.get('/planes');
export const getPlan = (id) => api.get(`/planes/${id}`);

// Usuarios
export const registrarUsuario = (data) => api.post('/usuarios/registro', data);
export const loginUsuario = (data) => api.post('/usuarios/login', data);
export const getUsuarios = (requesterId) => api.get(`/usuarios${requesterId ? `?requesterId=${requesterId}` : ''}`);
export const getUsuario = (id) => api.get(`/usuarios/${id}`);
export const getUsuarioPorEmail = (email) => api.get(`/usuarios/email/${email}`);

// Suscripciones
export const crearSuscripcion = (usuarioId, planId, requesterId) =>
    api.post(`/suscripciones?requesterId=${requesterId || usuarioId}`, { usuarioId, planId });
export const getSuscripcionActiva = (usuarioId, requesterId) =>
    api.get(`/suscripciones/usuario/${usuarioId}?requesterId=${requesterId || usuarioId}`);
export const cambiarPlan = (suscripcionId, nuevoPlanId, requesterId) =>
    api.post(`/suscripciones/cambiar-plan?requesterId=${requesterId}`, { suscripcionId, nuevoPlanId });
export const cancelarSuscripcion = (suscripcionId, motivo, requesterId) =>
    api.post(`/suscripciones/${suscripcionId}/cancelar?requesterId=${requesterId}`, { motivo });

// Facturas
export const getFacturasUsuario = (usuarioId, requesterId) =>
    api.get(`/suscripciones/usuario/${usuarioId}/facturas?requesterId=${requesterId || usuarioId}`);
export const pagarFactura = (facturaId) =>
    api.post(`/suscripciones/facturas/${facturaId}/pagar`);

// Admin
export const getAdminStats = (requesterId) =>
    api.get(`/admin/stats?requesterId=${requesterId}`);
export const getAdminSuscripciones = (requesterId) =>
    api.get(`/admin/suscripciones?requesterId=${requesterId}`);
export const getAdminUsuarios = (requesterId) =>
    api.get(`/admin/usuarios?requesterId=${requesterId}`);
export const getAuditoria = (requesterId) =>
    api.get(`/admin/auditoria?requesterId=${requesterId}`);

// Rutas Públicas de Usuarios
export const getPublicUsuarios = () => api.get('/usuarios/publico');

export default api;
