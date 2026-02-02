import { Routes, Route, NavLink } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Home from './pages/Home';
import Planes from './pages/Planes';
import Registro from './pages/Registro';
import Dashboard from './pages/Dashboard';
import Usuarios from './pages/Usuarios';

function App() {
  const [usuarioActual, setUsuarioActual] = useState(null);

  useEffect(() => {
    const stored = localStorage.getItem('usuarioActual');
    if (stored) {
      setUsuarioActual(JSON.parse(stored));
    }
  }, []);

  const handleLogin = (usuario) => {
    setUsuarioActual(usuario);
    localStorage.setItem('usuarioActual', JSON.stringify(usuario));
  };

  const handleLogout = () => {
    setUsuarioActual(null);
    localStorage.removeItem('usuarioActual');
  };

  return (
    <div className="app">
      <header className="header">
        <div className="logo">SaaS Platform</div>
        <nav className="nav-links">
          <NavLink to="/" className={({isActive}) => isActive ? 'active' : ''}>
            Inicio
          </NavLink>
          <NavLink to="/planes" className={({isActive}) => isActive ? 'active' : ''}>
            Planes
          </NavLink>
          {!usuarioActual && (
            <NavLink to="/registro" className={({isActive}) => isActive ? 'active' : ''}>
              Registrarse
            </NavLink>
          )}
          {usuarioActual && (
            <>
              <NavLink to="/dashboard" className={({isActive}) => isActive ? 'active' : ''}>
                Mi Panel
              </NavLink>
              <NavLink to="/usuarios" className={({isActive}) => isActive ? 'active' : ''}>
                Usuarios
              </NavLink>
            </>
          )}
        </nav>
        {usuarioActual ? (
          <div className="user-info">
            <div className="user-avatar">
              {usuarioActual.nombre.charAt(0).toUpperCase()}
            </div>
            <span style={{color: '#e8eaed', fontSize: '0.9375rem'}}>{usuarioActual.nombre}</span>
            <button className="btn btn-secondary" onClick={handleLogout} style={{padding: '0.5rem 1rem', borderColor: '#e8eaed', color: '#e8eaed', fontSize: '0.875rem'}}>
              Salir
            </button>
          </div>
        ) : (
          <div>
            <NavLink to="/registro" className="btn btn-primary" style={{padding: '0.625rem 1.25rem', fontSize: '0.875rem'}}>
              Comenzar prueba gratis
            </NavLink>
          </div>
        )}
      </header>

      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/planes" element={<Planes usuarioActual={usuarioActual} onLogin={handleLogin} />} />
          <Route path="/registro" element={<Registro onLogin={handleLogin} />} />
          <Route path="/dashboard" element={<Dashboard usuarioActual={usuarioActual} setUsuarioActual={setUsuarioActual} />} />
          <Route path="/usuarios" element={<Usuarios />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
