import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getPlanes, crearSuscripcion } from '../services/api';
import { formatCurrency } from '../utils/currencyUtils';

function Planes({ usuarioActual, onLogin, currency }) {
  const [planes, setPlanes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [mensaje, setMensaje] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    cargarPlanes();
  }, []);

  const cargarPlanes = async () => {
    try {
      const response = await getPlanes();
      setPlanes(response.data);
    } catch (error) {
      console.error('Error cargando planes:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSeleccionarPlan = async (planId) => {
    if (!usuarioActual) {
      navigate('/registro', { state: { planId } });
      return;
    }

    if (usuarioActual.suscripcionActiva) {
      navigate('/dashboard');
      return;
    }

    try {
      await crearSuscripcion(usuarioActual.id, planId);
      setMensaje({ tipo: 'success', texto: '¡Suscripción creada exitosamente!' });
      setTimeout(() => navigate('/dashboard'), 1500);
    } catch (error) {
      setMensaje({ tipo: 'error', texto: error.response?.data?.error || 'Error al crear suscripción' });
    }
  };

  const getFeatures = (plan) => {
    return [
      { texto: `${plan.maxUsuarios || 'Ilimitados'} usuarios`, activo: true },
      { texto: `${plan.almacenamientoGb || 'Ilimitado'} GB almacenamiento`, activo: true },
      { texto: `${plan.maxProyectos || 'Ilimitados'} proyectos`, activo: true },
      { texto: 'Soporte prioritario', activo: plan.soportePrioritario },
      { texto: 'Acceso API', activo: plan.apiAcceso },
      { texto: 'Analytics avanzados', activo: plan.analyticsAvanzados },
      { texto: 'Integraciones externas', activo: plan.integracionesExternas },
    ];
  };

  if (loading) {
    return (
      <div className="container">
        <div className="loading">
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  return (
    <>
      {
        // Hero Section for Planes
      }
      <div style={{
        background: '#f8f9fa',
        padding: '3rem 2rem',
        borderBottom: '1px solid #e8eaed'
      }}>
        <div className="container">
          <div style={{ textAlign: 'center', maxWidth: '800px', margin: '0 auto' }}>
            <h1 style={{ fontSize: '3rem', fontWeight: 600, marginBottom: '1rem', color: '#2f3941', letterSpacing: '-0.5px' }}>
              Elige el plan perfecto para tu negocio
            </h1>
            <p style={{ fontSize: '1.125rem', color: '#5f6f7a', lineHeight: 1.6 }}>
              Planes flexibles con facturación mensual y prorrateo inteligente. Cancela cuando quieras.
            </p>
          </div>
        </div>
      </div>

      <div className="container" style={{ paddingTop: '3rem', paddingBottom: '4rem' }}>
        {mensaje && (
          <div className={`alert alert-${mensaje.tipo}`}>
            {mensaje.texto}
          </div>
        )}

        <div className="plans-grid">
          {planes.map((plan, index) => (
            <div
              key={plan.id}
              className={`plan-card ${index === 1 ? 'popular' : ''}`}
            >
              <span className="plan-type">{plan.tipo}</span>
              <h2 className="plan-name">{plan.nombre}</h2>
              <div className="plan-price">
                {formatCurrency(plan.precioMensual, currency)}
                <span>/mes</span>
              </div>
              <p className="plan-description">{plan.descripcion}</p>

              <ul className="plan-features">
                {getFeatures(plan).map((feature, i) => (
                  <li key={i} className={feature.activo ? '' : 'disabled'}>
                    {feature.texto}
                  </li>
                ))}
              </ul>

              <button
                className={`btn btn-full ${index === 1 ? 'btn-primary' : 'btn-secondary'}`}
                onClick={() => handleSeleccionarPlan(plan.id)}
              >
                {usuarioActual?.suscripcionActiva
                  ? 'Ya tienes suscripción'
                  : usuarioActual
                    ? 'Seleccionar Plan'
                    : 'Comenzar prueba gratis'}
              </button>
            </div>
          ))}
        </div>

        <div className="card" style={{ marginTop: '3rem', textAlign: 'center', background: '#f8f9fa', border: '1px solid #e8eaed' }}>
          <h3 style={{ marginBottom: '1rem', fontSize: '1.25rem', color: '#2f3941', fontWeight: 600 }}>
            💡 Prorrateo Inteligente
          </h3>
          <p style={{ color: '#5f6f7a', maxWidth: '700px', margin: '0 auto', lineHeight: 1.6 }}>
            Al cambiar a un plan superior, solo pagarás la diferencia proporcional
            a los días restantes de tu ciclo de facturación. Sin costos ocultos, sin sorpresas.
          </p>
        </div>
      </div>
    </>
  );
}

export default Planes;
