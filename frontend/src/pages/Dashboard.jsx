import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  getUsuario, 
  getPlanes, 
  getFacturasUsuario, 
  cambiarPlan, 
  pagarFactura,
  cancelarSuscripcion 
} from '../services/api';

function Dashboard({ usuarioActual, setUsuarioActual }) {
  const [usuario, setUsuario] = useState(null);
  const [planes, setPlanes] = useState([]);
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [mensaje, setMensaje] = useState(null);
  const [showCambiarPlan, setShowCambiarPlan] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (!usuarioActual) {
      navigate('/registro');
      return;
    }
    cargarDatos();
  }, [usuarioActual, navigate]);

  const cargarDatos = async () => {
    try {
      const [usuarioRes, planesRes, facturasRes] = await Promise.all([
        getUsuario(usuarioActual.id),
        getPlanes(),
        getFacturasUsuario(usuarioActual.id)
      ]);
      setUsuario(usuarioRes.data);
      setPlanes(planesRes.data);
      setFacturas(facturasRes.data);
      
      // Actualizar usuario en localStorage
      localStorage.setItem('usuarioActual', JSON.stringify(usuarioRes.data));
      setUsuarioActual(usuarioRes.data);
    } catch (error) {
      console.error('Error cargando datos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCambiarPlan = async (nuevoPlanId) => {
    if (!usuario?.suscripcionActiva) return;
    
    try {
      await cambiarPlan(usuario.suscripcionActiva.id, nuevoPlanId);
      setMensaje({ tipo: 'success', texto: '¡Plan cambiado exitosamente! Se ha generado una factura de prorrateo.' });
      setShowCambiarPlan(false);
      cargarDatos();
    } catch (error) {
      setMensaje({ tipo: 'error', texto: error.response?.data?.error || 'Error al cambiar plan' });
    }
  };

  const handlePagarFactura = async (facturaId) => {
    try {
      await pagarFactura(facturaId);
      setMensaje({ tipo: 'success', texto: '¡Factura pagada exitosamente!' });
      cargarDatos();
    } catch (error) {
      setMensaje({ tipo: 'error', texto: error.response?.data?.error || 'Error al pagar factura' });
    }
  };

  const handleCancelarSuscripcion = async () => {
    if (!usuario?.suscripcionActiva) return;
    if (!confirm('¿Estás seguro de cancelar tu suscripción?')) return;

    try {
      await cancelarSuscripcion(usuario.suscripcionActiva.id, 'Cancelación por el usuario');
      setMensaje({ tipo: 'success', texto: 'Suscripción cancelada' });
      cargarDatos();
    } catch (error) {
      setMensaje({ tipo: 'error', texto: error.response?.data?.error || 'Error al cancelar' });
    }
  };

  const getEstadoBadge = (estado) => {
    const badges = {
      ACTIVA: 'badge-success',
      PENDIENTE: 'badge-warning',
      PAGADA: 'badge-success',
      VENCIDA: 'badge-danger',
      CANCELADA: 'badge-danger',
      MOROSA: 'badge-danger',
      TRIAL: 'badge-info'
    };
    return badges[estado] || 'badge-info';
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

  if (!usuario) {
    return null;
  }

  const suscripcion = usuario.suscripcionActiva;

  return (
    <div className="container">
      <div className="page-title">
        <h1>Mi Panel</h1>
        <p>Gestiona tu suscripción y facturas</p>
      </div>

      {mensaje && (
        <div className={`alert alert-${mensaje.tipo}`}>
          {mensaje.texto}
          <button 
            onClick={() => setMensaje(null)} 
            style={{ float: 'right', background: 'none', border: 'none', cursor: 'pointer' }}
          >
            ✕
          </button>
        </div>
      )}

      {/* Tarjetas de estadísticas */}
      <div className="dashboard-grid">
        <div className="stat-card">
          <h3>Plan Actual</h3>
          <div className="value primary">
            {suscripcion ? suscripcion.planNombre : 'Sin plan'}
          </div>
        </div>
        <div className="stat-card">
          <h3>Estado</h3>
          <div className="value">
            {suscripcion ? (
              <span className={`badge ${getEstadoBadge(suscripcion.estado)}`}>
                {suscripcion.estado}
              </span>
            ) : (
              <span className="badge badge-warning">Sin suscripción</span>
            )}
          </div>
        </div>
        <div className="stat-card">
          <h3>Próximo Cobro</h3>
          <div className="value">
            {suscripcion?.fechaProximoCobro || 'N/A'}
          </div>
        </div>
        <div className="stat-card">
          <h3>Total Facturas</h3>
          <div className="value primary">{facturas.length}</div>
        </div>
      </div>

      {/* Acciones de suscripción */}
      {suscripcion && suscripcion.estado === 'ACTIVA' && (
        <div className="card" style={{ marginBottom: '2rem' }}>
          <h3 style={{ marginBottom: '1rem' }}>Gestionar Suscripción</h3>
          <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
            <button 
              className="btn btn-primary"
              onClick={() => setShowCambiarPlan(true)}
            >
              Cambiar Plan
            </button>
            <button 
              className="btn btn-danger"
              onClick={handleCancelarSuscripcion}
            >
              Cancelar Suscripción
            </button>
          </div>
        </div>
      )}

      {!suscripcion && (
        <div className="card" style={{ marginBottom: '2rem', textAlign: 'center' }}>
          <h3 style={{ marginBottom: '1rem' }}>No tienes una suscripción activa</h3>
          <button 
            className="btn btn-primary"
            onClick={() => navigate('/planes')}
          >
            Ver Planes Disponibles
          </button>
        </div>
      )}

      {/* Tabla de facturas */}
      <div className="card">
        <h3 style={{ marginBottom: '1rem' }}>Mis Facturas</h3>
        {facturas.length === 0 ? (
          <p style={{ color: '#718096' }}>No tienes facturas todavía</p>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Nº Factura</th>
                  <th>Tipo</th>
                  <th>Concepto</th>
                  <th>Total</th>
                  <th>Estado</th>
                  <th>Fecha</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {facturas.map(factura => (
                  <tr key={factura.id}>
                    <td>{factura.numeroFactura}</td>
                    <td>
                      <span className={`badge ${factura.tipo === 'PRORRATEO' ? 'badge-info' : 'badge-success'}`}>
                        {factura.tipo}
                      </span>
                    </td>
                    <td>{factura.concepto}</td>
                    <td><strong>€{factura.total?.toFixed(2)}</strong></td>
                    <td>
                      <span className={`badge ${getEstadoBadge(factura.estado)}`}>
                        {factura.estado}
                      </span>
                    </td>
                    <td>{factura.fechaEmision}</td>
                    <td>
                      {factura.estado === 'PENDIENTE' && (
                        <button 
                          className="btn btn-success"
                          style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}
                          onClick={() => handlePagarFactura(factura.id)}
                        >
                          Pagar
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal Cambiar Plan */}
      {showCambiarPlan && (
        <div className="modal-overlay" onClick={() => setShowCambiarPlan(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>Cambiar Plan</h2>
            <p style={{ color: '#718096', marginBottom: '1.5rem' }}>
              Si eliges un plan superior, se generará una factura de prorrateo 
              por la diferencia de los días restantes.
            </p>
            
            {planes.filter(p => p.id !== suscripcion?.planId).map(plan => (
              <div 
                key={plan.id}
                style={{
                  padding: '1rem',
                  border: '2px solid #e2e8f0',
                  borderRadius: '10px',
                  marginBottom: '1rem',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center'
                }}
              >
                <div>
                  <strong>{plan.nombre}</strong>
                  <div style={{ color: '#718096', fontSize: '0.875rem' }}>
                    €{plan.precioMensual}/mes
                  </div>
                </div>
                <button
                  className="btn btn-primary"
                  style={{ padding: '0.5rem 1rem' }}
                  onClick={() => handleCambiarPlan(plan.id)}
                >
                  Seleccionar
                </button>
              </div>
            ))}
            
            <div className="modal-actions">
              <button 
                className="btn btn-secondary"
                onClick={() => setShowCambiarPlan(false)}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
