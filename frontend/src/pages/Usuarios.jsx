import { useState, useEffect } from 'react';
import { getPublicUsuarios } from '../services/api';

function Usuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      const response = await getPublicUsuarios();
      setUsuarios(response.data);
    } catch (error) {
      console.error('Error cargando usuarios:', error);
    } finally {
      setLoading(false);
    }
  };

  const getEstadoBadge = (estado) => {
    const badges = {
      ACTIVA: 'badge-success',
      CANCELADA: 'badge-danger',
      MOROSA: 'badge-danger',
      PAUSADA: 'badge-warning',
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

  return (
    <div className="container">
      <div className="page-title">
        <h1>Nuestros Usuarios</h1>
        <p>Comunidad BetterDrive: Usuarios que confían en nosotros</p>
      </div>

      <div className="card">
        {usuarios.length === 0 ? (
          <p style={{ color: '#718096', textAlign: 'center' }}>
            No hay usuarios registrados todavía
          </p>
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Plan Actual</th>
                  <th>Estado</th>
                  <th>Miembro desde</th>
                </tr>
              </thead>
              <tbody>
                {usuarios.map(usuario => (
                  <tr key={usuario.id}>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                        <div className="user-avatar" style={{ width: '30px', height: '30px', fontSize: '0.75rem' }}>
                          {usuario.nombre.charAt(0).toUpperCase()}
                        </div>
                        {usuario.nombre} {usuario.apellido}
                      </div>
                    </td>
                    <td>
                      <span className="badge badge-info">
                        {usuario.planNombre}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${getEstadoBadge(usuario.estadoSuscripcion)}`}>
                        {usuario.estadoSuscripcion}
                      </span>
                    </td>
                    <td>
                      {usuario.fechaRegistro
                        ? new Date(usuario.fechaRegistro).toLocaleDateString('es-ES')
                        : 'N/A'
                      }
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="dashboard-grid" style={{ marginTop: '2rem' }}>
        <div className="stat-card">
          <h3>Total Usuarios</h3>
          <div className="value primary">{usuarios.length}</div>
        </div>
        <div className="stat-card">
          <h3>Comunidad Activa</h3>
          <div className="value">
            {usuarios.filter(u => u.estadoSuscripcion === 'ACTIVA').length}
          </div>
        </div>
        <div className="stat-card">
          <h3>Nuevos miembros</h3>
          <div className="value">
            {usuarios.filter(u => {
              const joinDate = new Date(u.fechaRegistro);
              const thirtyDaysAgo = new Date();
              thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
              return joinDate > thirtyDaysAgo;
            }).length}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Usuarios;
