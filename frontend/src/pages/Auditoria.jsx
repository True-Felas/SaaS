import React, { useEffect, useState } from 'react';
import api from '../services/api';

const Auditoria = () => {
  const [auditoria, setAuditoria] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/admin/auditoria')
      .then(res => {
        setAuditoria(res.data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  return (
    <div>
      <h2>Panel de Auditoría</h2>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Entidad</th>
              <th>Acción</th>
              <th>Usuario</th>
              <th>Fecha</th>
              <th>Detalles</th>
            </tr>
          </thead>
          <tbody>
            {auditoria.map((a, idx) => (
              <tr key={idx}>
                <td>{a.entidad}</td>
                <td>{a.accion}</td>
                <td>{a.usuario}</td>
                <td>{a.fecha}</td>
                <td>{a.detalles}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Auditoria;
