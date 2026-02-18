import { Routes, Route, NavLink } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { CURRENCIES } from './utils/currencyUtils';
import Home from './pages/Home';
import Planes from './pages/Planes';
import Registro from './pages/Registro';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Usuarios from './pages/Usuarios';
import AdminDashboard from './pages/AdminDashboard';
import Auditoria from './pages/Auditoria';

function App() {
  const [usuarioActual, setUsuarioActual] = useState(null);
  const [currency, setCurrency] = useState(() => localStorage.getItem('currency') || 'EUR');
  const [showCurrencyDropdown, setShowCurrencyDropdown] = useState(false);

  useEffect(() => {
    localStorage.setItem('currency', currency);
  }, [currency]);

  useEffect(() => {
    const stored = localStorage.getItem('usuarioActual');
    const token = localStorage.getItem('token');
    if (stored) {
      setUsuarioActual(JSON.parse(stored));
    }
    if (token) {
      // token is stored for API requests via interceptor
    }
  }, []);

  const handleLogin = (data) => {
    // data can be either the usuario object or { token, usuario }
    if (!data) return;
    if (data.token && data.usuario) {
      localStorage.setItem('token', data.token);
      setUsuarioActual(data.usuario);
      localStorage.setItem('usuarioActual', JSON.stringify(data.usuario));
    } else if (data.usuario) {
      localStorage.setItem('token', data.token || '');
      setUsuarioActual(data.usuario);
      localStorage.setItem('usuarioActual', JSON.stringify(data.usuario));
    } else {
      // assume data is usuario
      setUsuarioActual(data);
      localStorage.setItem('usuarioActual', JSON.stringify(data));
    }
  };

  const handleLogout = () => {
    setUsuarioActual(null);
    localStorage.removeItem('usuarioActual');
    localStorage.removeItem('token');
  };

  return (
    <div className="app">
      <header className="header">
        <div className="logo">BetterDrive</div>
        <nav className="nav-links">
          <NavLink to="/" className={({ isActive }) => isActive ? 'active' : ''}>
            Inicio
          </NavLink>
          <NavLink to="/planes" className={({ isActive }) => isActive ? 'active' : ''}>
            Planes
          </NavLink>
          <NavLink to="/usuarios" className={({ isActive }) => isActive ? 'active' : ''}>
            Usuarios
          </NavLink>
          {!usuarioActual && (
            <>
              <NavLink to="/login" className={({ isActive }) => isActive ? 'active' : ''}>
                Iniciar sesión
              </NavLink>
              <NavLink to="/registro" className={({ isActive }) => isActive ? 'active' : ''}>
                Registrarse
              </NavLink>
            </>
          )}
          {usuarioActual && (
            <>
              <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'active' : ''}>
                Mi Panel
              </NavLink>
              {usuarioActual.role === 'ADMIN' && (
                <>
                  <NavLink to="/admin" className={({ isActive }) => isActive ? 'active' : ''}>
                    Admin
                  </NavLink>
                  <NavLink to="/auditoria" className={({ isActive }) => isActive ? 'active' : ''}>
                    Auditoría
                  </NavLink>
                </>
              )}
            </>
          )}
        </nav>

        <div className="header-actions">
          {
            // Selector de Moneda
          }
          <div className="currency-selector">
            <button
              className="btn btn-outline-white"
              onClick={() => setShowCurrencyDropdown(!showCurrencyDropdown)}
            >
              <span>{CURRENCIES[currency].symbol} {currency}</span>
            </button>

            {showCurrencyDropdown && (
              <div className="dropdown-menu">
                {Object.keys(CURRENCIES).map(code => (
                  <div
                    key={code}
                    className={`dropdown-item ${currency === code ? 'active' : ''}`}
                    onClick={() => {
                      setCurrency(code);
                      setShowCurrencyDropdown(false);
                    }}
                  >
                    <span>{CURRENCIES[code].label}</span>
                    <span>Símbolo: {CURRENCIES[code].symbol}</span>
                  </div>
                ))}
              </div>
            )}
          </div>

          {usuarioActual ? (
            <div className="user-info">
              <div className="user-avatar">
                {usuarioActual.nombre.charAt(0).toUpperCase()}
              </div>
              <span style={{ color: '#e8eaed', fontSize: '0.9375rem' }}>{usuarioActual.nombre}</span>
              <button className="btn btn-outline-white" onClick={handleLogout} style={{ padding: '0.4rem 0.8rem', fontSize: '0.8125rem' }}>
                Salir
              </button>
            </div>
          ) : (
            <NavLink to="/login" className="btn btn-primary" style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}>
              Prueba Gratis
            </NavLink>
          )}
        </div>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/planes" element={<Planes usuarioActual={usuarioActual} onLogin={handleLogin} currency={currency} />} />
          <Route path="/registro" element={<Registro onLogin={handleLogin} currency={currency} />} />
          <Route path="/login" element={<Login onLogin={handleLogin} />} />
          <Route path="/dashboard" element={<Dashboard usuarioActual={usuarioActual} setUsuarioActual={setUsuarioActual} currency={currency} />} />
          <Route path="/usuarios" element={<Usuarios currency={currency} />} />
          <Route path="/admin" element={<AdminDashboard usuarioActual={usuarioActual} currency={currency} />} />
          <Route path="/auditoria" element={<Auditoria usuarioActual={usuarioActual} />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
