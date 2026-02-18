import { useState, useEffect } from 'react';
import api from '../services/api';
import { formatCurrency } from '../utils/currencyUtils';

const Facturacion = ({ currency }) => {
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    api.get('/facturas')
      .then(res => {
        setFacturas(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError(err.response?.data?.error || 'Error al cargar las facturas');
        setLoading(false);
      });
  }, []);

  return (
    <div className="container">
      <div className="page-title">
        <h1>Facturación</h1>
        <p>Historial de facturas y pagos</p>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {loading ? (
        <div className="loading"><div className="spinner"></div></div>
      ) : facturas.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
          <p style={{ color: '#718096' }}>No hay facturas disponibles.</p>
        </div>
      ) : (
        <div className="card" style={{ padding: '0' }}>
          <table>
            <thead>
              <tr>
                <th>Número</th>
                <th>Fecha Emisión</th>
                <th>Concepto</th>
                <th>Subtotal</th>
                <th>Impuesto</th>
                <th>Total</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {facturas.map(f => (
                <tr key={f.id}>
                  <td><strong>{f.numeroFactura}</strong></td>
                  <td>{f.fechaEmision}</td>
                  <td>{f.concepto}</td>
                  <td>{formatCurrency(f.subtotal, currency)}</td>
                  <td>{formatCurrency(f.impuesto, currency)} ({f.porcentajeImpuesto}%)</td>
                  <td><strong>{formatCurrency(f.total, currency)}</strong></td>
                  <td>
                    <span className={`badge ${
                      f.estado === 'PAGADA' ? 'badge-success' :
                      f.estado === 'PENDIENTE' ? 'badge-warning' : 'badge-danger'
                    }`}>
                      {f.estado}
                    </span>
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

export default Facturacion;
