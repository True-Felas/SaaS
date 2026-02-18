import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { loginUsuario } from '../services/api';

function Login({ onLogin }) {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const validate = () => {
    const errors = {};
    if (!formData.email) errors.email = 'El email es obligatorio';
    else if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(formData.email))
      errors.email = 'Introduce un email válido';
    if (!formData.password) errors.password = 'La contraseña es obligatoria';
    return errors;
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    // Limpiar error del campo al escribir
    if (fieldErrors[e.target.name]) {
      setFieldErrors({ ...fieldErrors, [e.target.name]: null });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const errors = validate();
    if (Object.keys(errors).length > 0) {
      setFieldErrors(errors);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const response = await loginUsuario(formData);
      onLogin(response.data);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.error || 'Credenciales incorrectas. Verifica tu email y contraseña.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="page-title">
        <h1>Iniciar sesión</h1>
        <p>Accede con tu cuenta</p>
      </div>

      <div className="card" style={{ maxWidth: '420px', margin: '2rem auto' }}>
        <form onSubmit={handleSubmit} noValidate>
          {error && <div className="alert alert-error" role="alert">{error}</div>}

          <div className="form-group">
            <label htmlFor="email">Email *</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="tu@email.com"
              style={fieldErrors.email ? { borderColor: '#d93f4c' } : {}}
            />
            {fieldErrors.email && (
              <span style={{ color: '#d93f4c', fontSize: '0.8rem', marginTop: '0.25rem', display: 'block' }}>
                ⚠ {fieldErrors.email}
              </span>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="password">Contraseña *</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Tu contraseña"
              style={fieldErrors.password ? { borderColor: '#d93f4c' } : {}}
            />
            {fieldErrors.password && (
              <span style={{ color: '#d93f4c', fontSize: '0.8rem', marginTop: '0.25rem', display: 'block' }}>
                ⚠ {fieldErrors.password}
              </span>
            )}
          </div>

          <button type="submit" className="btn btn-primary btn-full" disabled={loading}>
            {loading ? 'Iniciando...' : 'Iniciar sesión'}
          </button>

          <div style={{ marginTop: '1rem', textAlign: 'center' }}>
            ¿No tienes cuenta? <Link to="/registro">Regístrate</Link>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;
