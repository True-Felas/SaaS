import React, { useEffect, useState } from 'react';
import api from '../services/api';

const accionBadge = (accion) => {
  if (!accion) return 'badge-info';
  const a = accion.toString().toUpperCase();
  if (a === 'ADD' || a === 'INSERT') return 'badge-success';
  if (a === 'MOD' || a === 'UPDATE') return 'badge-warning';
  if (a === 'DEL' || a === 'DELETE') return 'badge-danger';
  return 'badge-info';
};

const Auditoria = ({ usuarioActual }) => {
  const [auditoria, setAuditoria] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!usuarioActual?.id) return;
    api.get(`/admin/auditoria?requesterId=${usuarioActual.id}`)
      .then(res => {
        setAuditoria(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.response?.data?.error || 'Error al cargar la auditoría');
        setLoading(false);
      });
  }, [usuarioActual]);

  return (
    <div className="container">
      <div className="page-title">
        <h1>Panel de Auditoría</h1>
        <p>Historial completo de cambios en suscripciones y planes</p>
      </div>

      {error && <div className="alert alert-error" role="alert">{error}</div>}

      {loading ? (
        <div className="loading"><div className="spinner"></div></div>
      ) : auditoria.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
          <p style={{ color: '#718096' }}>No hay registros de auditoría disponibles.</p>
        </div>
      ) : (
        <div className="card" style={{ padding: '0' }}>
          <table>
            <thead>
              <tr>
                <th>Entidad</th>
                <th>Acción</th>
                <th>Usuario</th>
                <th>Plan</th>
                <th>Estado</th>
                <th>Fecha</th>
                <th>Detalles</th>
              </tr>
            </thead>
            <tbody>
              {auditoria.map((a, idx) => (
                <tr key={idx}>
                  <td><span className="badge badge-info">{a.entidad}</span></td>
                  <td>
                    <span className={`badge ${accionBadge(a.accion)}`}>
                      {a.accion}
                    </span>
                  </td>
                  <td>{a.usuario}</td>
                  <td>{a.plan || '—'}</td>
                  <td>{a.estado || '—'}</td>
                  <td style={{ whiteSpace: 'nowrap' }}>{a.fecha}</td>
                  <td style={{ maxWidth: '200px', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                    {a.detalles}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default Auditoria;
