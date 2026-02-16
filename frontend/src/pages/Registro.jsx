import { useState, useEffect } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { registrarUsuario, getPlanes } from '../services/api';
import { formatCurrency } from '../utils/currencyUtils';

function Registro({ onLogin, currency }) {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    nombre: '',
    apellido: '',
    planId: ''
  });
  const [planes, setPlanes] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    cargarPlanes();
    // Si viene con un plan preseleccionado
    if (location.state?.planId) {
      setFormData(prev => ({ ...prev, planId: location.state.planId }));
    }
  }, [location.state]);

  const cargarPlanes = async () => {
    try {
      const response = await getPlanes();
      setPlanes(response.data);
    } catch (error) {
      console.error('Error cargando planes:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const dataToSend = {
        ...formData,
        planId: formData.planId ? parseInt(formData.planId) : null
      };

      const response = await registrarUsuario(dataToSend);
      onLogin(response.data);
      navigate('/dashboard');
    } catch (error) {
      setError(error.response?.data?.error || 'Error al registrar usuario');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="page-title">
        <h1>Crear Cuenta</h1>
        <p>Únete a BetterDrive</p>
      </div>

      <div className="card" style={{ maxWidth: '500px', margin: '2rem auto' }}>
        <form onSubmit={handleSubmit}>
          {error && (
            <div className="alert alert-error">
              {error}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="nombre">Nombre *</label>
            <input
              type="text"
              id="nombre"
              name="nombre"
              value={formData.nombre}
              onChange={handleChange}
              required
              placeholder="Tu nombre"
            />
          </div>

          <div className="form-group">
            <label htmlFor="apellido">Apellido</label>
            <input
              type="text"
              id="apellido"
              name="apellido"
              value={formData.apellido}
              onChange={handleChange}
              placeholder="Tu apellido"
            />
          </div>

          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="tu@email.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Contraseña *</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              minLength="6"
              placeholder="Mínimo 6 caracteres"
            />
          </div>

          <div className="form-group">
            <label htmlFor="planId">Seleccionar Plan</label>
            <select
              id="planId"
              name="planId"
              value={formData.planId}
              onChange={handleChange}
            >
              <option value="">-- Sin plan por ahora --</option>
              {planes.map(plan => (
                <option key={plan.id} value={plan.id}>
                  {plan.nombre} - {formatCurrency(plan.precioMensual, currency)}/mes
                </option>
              ))}
            </select>
          </div>

          <button
            type="submit"
            className="btn btn-primary btn-full"
            disabled={loading}
          >
            {loading ? 'Registrando...' : 'Crear Cuenta'}
          </button>
          <div style={{ marginTop: '1rem', textAlign: 'center' }}>
            ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Registro;
