import React, { useEffect, useState } from 'react';
import api from '../services/api';

const Facturacion = () => {
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/facturas')
      .then(res => {
        setFacturas(res.data);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  return (
    <div>
      <h2>Facturación</h2>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Número</th>
              <th>Fecha Emisión</th>
              <th>Subtotal</th>
              <th>Impuesto</th>
              <th>Total</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {facturas.map(f => (
              <tr key={f.id}>
                <td>{f.numeroFactura}</td>
                <td>{f.fechaEmision}</td>
                <td>{f.subtotal} €</td>
                <td>{f.impuesto} €</td>
                <td>{f.total} €</td>
                <td>{f.estado}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default Facturacion;
