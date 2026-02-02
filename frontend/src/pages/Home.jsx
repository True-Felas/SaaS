import { Link } from 'react-router-dom';

function Home() {
  return (
    <>
      {/* Hero Section */}
      <div style={{ 
        background: 'linear-gradient(135deg, #03363d 0%, #17494d 100%)', 
        padding: '5rem 2rem',
        textAlign: 'center',
        color: 'white'
      }}>
        <div style={{ maxWidth: '900px', margin: '0 auto' }}>
          <h1 style={{ 
            fontSize: '3.5rem', 
            fontWeight: 600, 
            marginBottom: '1.5rem',
            letterSpacing: '-1px',
            lineHeight: 1.1
          }}>
            La plataforma más completa para gestionar tu negocio
          </h1>
          <p style={{ 
            fontSize: '1.375rem', 
            marginBottom: '2.5rem',
            opacity: 0.95,
            lineHeight: 1.6
          }}>
            Potencia tu empresa con herramientas avanzadas de gestión y automatización. 
            Facturación inteligente, planes flexibles y soporte 24/7.
          </p>
          
          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center', flexWrap: 'wrap', marginBottom: '3rem' }}>
            <Link to="/registro" className="btn" style={{
              background: 'white',
              color: '#03363d',
              padding: '1rem 2rem',
              fontSize: '1rem',
              fontWeight: 600,
              boxShadow: '0 4px 12px rgba(0,0,0,0.15)'
            }}>
              Inicia tu prueba gratis
            </Link>
            <Link to="/planes" className="btn" style={{
              background: 'transparent',
              border: '2px solid white',
              color: 'white',
              padding: '1rem 2rem',
              fontSize: '1rem',
              fontWeight: 600
            }}>
              Ver planes
            </Link>
          </div>

          <p style={{ fontSize: '0.875rem', opacity: 0.8 }}>
            ✓ Prueba de 14 días, 100% gratis. No se requiere tarjeta de crédito.
          </p>
        </div>
      </div>

      {/* Trust Badges */}
      <div style={{ background: 'white', padding: '2rem', borderBottom: '1px solid #e8eaed' }}>
        <div className="container">
          <p style={{ textAlign: 'center', color: '#5f6f7a', fontSize: '0.875rem', marginBottom: '1.5rem', textTransform: 'uppercase', letterSpacing: '1px', fontWeight: 600 }}>
            MÁS DE 1,000 EMPRESAS CONFÍAN EN NOSOTROS
          </p>
          <div style={{ display: 'flex', justifyContent: 'center', gap: '3rem', flexWrap: 'wrap', alignItems: 'center' }}>
            {['Empresa A', 'Empresa B', 'Empresa C', 'Empresa D', 'Empresa E'].map((empresa, idx) => (
              <div key={idx} style={{ color: '#9ca3af', fontWeight: 600, fontSize: '1.125rem' }}>
                {empresa}
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="container" style={{ paddingTop: '4rem', paddingBottom: '4rem' }}>
        <div style={{ textAlign: 'center', maxWidth: '800px', margin: '0 auto 3rem' }}>
          <h2 style={{ fontSize: '2.5rem', fontWeight: 600, marginBottom: '1rem', color: '#2f3941' }}>
            Ofrezca resultados rápidos con herramientas avanzadas
          </h2>
          <p style={{ fontSize: '1.125rem', color: '#5f6f7a', lineHeight: 1.6 }}>
            Todo lo que necesitas para gestionar tu negocio en una sola plataforma
          </p>
        </div>

        <div className="dashboard-grid">
          <div className="stat-card">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>📊</div>
            <h3 style={{ fontSize: '1.25rem', color: '#2f3941', marginBottom: '0.75rem', fontWeight: 600 }}>
              Analytics en tiempo real
            </h3>
            <p style={{ color: '#5f6f7a', lineHeight: 1.6 }}>
              Métricas detalladas y reportes personalizables para tomar mejores decisiones de negocio
            </p>
          </div>
          
          <div className="stat-card">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>🔒</div>
            <h3 style={{ fontSize: '1.25rem', color: '#2f3941', marginBottom: '0.75rem', fontWeight: 600 }}>
              Seguridad empresarial
            </h3>
            <p style={{ color: '#5f6f7a', lineHeight: 1.6 }}>
              Protección de datos con encriptación de nivel bancario y cumplimiento GDPR
            </p>
          </div>
          
          <div className="stat-card">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>⚡</div>
            <h3 style={{ fontSize: '1.25rem', color: '#2f3941', marginBottom: '0.75rem', fontWeight: 600 }}>
              Alto rendimiento
            </h3>
            <p style={{ color: '#5f6f7a', lineHeight: 1.6 }}>
              Infraestructura cloud de última generación con 99.9% de disponibilidad
            </p>
          </div>
          
          <div className="stat-card">
            <div style={{ fontSize: '2.5rem', marginBottom: '1rem' }}>💬</div>
            <h3 style={{ fontSize: '1.25rem', color: '#2f3941', marginBottom: '0.75rem', fontWeight: 600 }}>
              Soporte dedicado
            </h3>
            <p style={{ color: '#5f6f7a', lineHeight: 1.6 }}>
              Equipo de expertos disponible 24/7 para resolver tus dudas y ayudarte
            </p>
          </div>
        </div>
      </div>

      {/* CTA Section */}
      <div style={{ 
        background: '#f8f9fa', 
        padding: '4rem 2rem',
        textAlign: 'center',
        borderTop: '1px solid #e8eaed'
      }}>
        <div className="container">
          <h2 style={{ fontSize: '2.25rem', fontWeight: 600, marginBottom: '1rem', color: '#2f3941' }}>
            ¿Listo para transformar tu negocio?
          </h2>
          <p style={{ fontSize: '1.125rem', color: '#5f6f7a', marginBottom: '2rem' }}>
            Únete a miles de empresas que ya confían en nuestra plataforma
          </p>
          <Link to="/registro" className="btn btn-primary" style={{ 
            padding: '1rem 2.5rem',
            fontSize: '1rem'
          }}>
            Comenzar ahora
          </Link>
        </div>
      </div>
    </>
  );
}

export default Home;
