import axios from 'axios';

const API_URL = '/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Planes
export const getPlanes = () => api.get('/planes');
export const getPlan = (id) => api.get(`/planes/${id}`);

// Usuarios
export const registrarUsuario = (data) => api.post('/usuarios/registro', data);
export const getUsuarios = () => api.get('/usuarios');
export const getUsuario = (id) => api.get(`/usuarios/${id}`);
export const getUsuarioPorEmail = (email) => api.get(`/usuarios/email/${email}`);

// Suscripciones
export const crearSuscripcion = (usuarioId, planId) =>
    api.post('/suscripciones', { usuarioId, planId });
export const getSuscripcionActiva = (usuarioId) =>
    api.get(`/suscripciones/usuario/${usuarioId}`);
export const cambiarPlan = (suscripcionId, nuevoPlanId) =>
    api.post('/suscripciones/cambiar-plan', { suscripcionId, nuevoPlanId });
export const cancelarSuscripcion = (suscripcionId, motivo) =>
    api.post(`/suscripciones/${suscripcionId}/cancelar`, { motivo });

// Facturas
export const getFacturasUsuario = (usuarioId) =>
    api.get(`/suscripciones/usuario/${usuarioId}/facturas`);
export const pagarFactura = (facturaId) =>
    api.post(`/suscripciones/facturas/${facturaId}/pagar`);

export default api;
