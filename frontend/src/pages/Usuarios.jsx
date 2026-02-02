import { useState, useEffect } from 'react';
import { getUsuarios } from '../services/api';

function Usuarios() {
  const [usuarios, setUsuarios] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    cargarUsuarios();
  }, []);

  const cargarUsuarios = async () => {
    try {
      const response = await getUsuarios();
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
        <h1>Usuarios Registrados</h1>
        <p>Lista de todos los usuarios de la plataforma</p>
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
                  <th>ID</th>
                  <th>Nombre</th>
                  <th>Email</th>
                  <th>Plan</th>
                  <th>Estado Suscripción</th>
                  <th>Fecha Registro</th>
                </tr>
              </thead>
              <tbody>
                {usuarios.map(usuario => (
                  <tr key={usuario.id}>
                    <td>{usuario.id}</td>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                        <div className="user-avatar" style={{ width: '30px', height: '30px', fontSize: '0.75rem' }}>
                          {usuario.nombre.charAt(0).toUpperCase()}
                        </div>
                        {usuario.nombre} {usuario.apellido}
                      </div>
                    </td>
                    <td>{usuario.email}</td>
                    <td>
                      {usuario.suscripcionActiva ? (
                        <span className="badge badge-info">
                          {usuario.suscripcionActiva.planNombre}
                        </span>
                      ) : (
                        <span style={{ color: '#718096' }}>Sin plan</span>
                      )}
                    </td>
                    <td>
                      {usuario.suscripcionActiva ? (
                        <span className={`badge ${getEstadoBadge(usuario.suscripcionActiva.estado)}`}>
                          {usuario.suscripcionActiva.estado}
                        </span>
                      ) : (
                        <span className="badge badge-warning">Sin suscripción</span>
                      )}
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
          <h3>Con Suscripción Activa</h3>
          <div className="value">
            {usuarios.filter(u => u.suscripcionActiva?.estado === 'ACTIVA').length}
          </div>
        </div>
        <div className="stat-card">
          <h3>Sin Suscripción</h3>
          <div className="value">
            {usuarios.filter(u => !u.suscripcionActiva).length}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Usuarios;
