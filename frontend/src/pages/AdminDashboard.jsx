import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    getAdminStats,
    getAdminSuscripciones,
    getAdminUsuarios
} from '../services/api';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
    PieChart, Pie, Cell, LineChart, Line, Legend
} from 'recharts';
import { formatCurrency, CURRENCIES } from '../utils/currencyUtils';

function AdminDashboard({ usuarioActual, currency }) {
    const [stats, setStats] = useState(null);
    const [suscripciones, setSuscripciones] = useState([]);
    const [usuarios, setUsuarios] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8'];

    useEffect(() => {
        if (!usuarioActual || usuarioActual.role !== 'ADMIN') {
            navigate('/dashboard');
            return;
        }
        cargarDatos();
    }, [usuarioActual, navigate]);

    const cargarDatos = async () => {
        try {
            setLoading(true);
            const [statsRes, subsRes, usersRes] = await Promise.all([
                getAdminStats(usuarioActual.id),
                getAdminSuscripciones(usuarioActual.id),
                getAdminUsuarios(usuarioActual.id)
            ]);

            setStats(statsRes.data);
            setSuscripciones(subsRes.data);
            setUsuarios(usersRes.data);
        } catch (err) {
            console.error('Error cargando datos admin:', err);
            setError('No se pudieron cargar los datos de administración.');
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <div className="container"><div className="loading">Cargando panel de control...</div></div>;
    if (error) return <div className="container"><div className="alert alert-error">{error}</div></div>;

    // Preparar datos para pie chart de planes
    const dataPlanes = Object.keys(stats?.distribucionPlanes || {}).map(nombre => ({
        name: nombre,
        value: stats.distribucionPlanes[nombre]
    }));

    return (
        <div className="container" style={{ maxWidth: '1200px' }}>
            <div className="page-title">
                <h1>Panel de Administración</h1>
                <p>Visión global del sistema y gestión de usuarios</p>
            </div>

            {
                // KPIs
            }
            <div className="dashboard-grid">
                <div className="stat-card">
                    <h3>Total Usuarios</h3>
                    <div className="value primary">{stats?.totalUsuarios}</div>
                </div>
                <div className="stat-card">
                    <h3>Suscripciones Activas</h3>
                    <div className="value success">{stats?.suscripcionesActivas}</div>
                </div>
                <div className="stat-card">
                    <h3>Ingresos Totales</h3>
                    <div className="value">{formatCurrency(stats?.ingresosTotales || 0, currency)}</div>
                </div>
                <div className="stat-card">
                    <h3>Tasa de Conversión</h3>
                    <div className="value info">
                        {stats?.totalUsuarios > 0
                            ? ((stats.suscripcionesActivas / stats.totalUsuarios) * 100).toFixed(1)
                            : 0}%
                    </div>
                </div>
            </div>

            {
                // Gráficas
            }
            <div className="dashboard-grid" style={{ gridTemplateColumns: 'repeat(auto-fit, minmax(500px, 1fr))', marginTop: '2rem' }}>
                <div className="card">
                    <h3>Ingresos Mensuales</h3>
                    <div style={{ width: '100%', height: 300 }}>
                        <ResponsiveContainer>
                            <LineChart data={stats?.ingresosMensuales}>
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="mes" />
                                <YAxis />
                                <Tooltip formatter={(value) => formatCurrency(value, currency)} />
                                <Legend />
                                <Line type="monotone" dataKey="monto" name={`Ingresos (${CURRENCIES[currency].symbol})`} stroke="#8884d8" activeDot={{ r: 8 }} />
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                <div className="card">
                    <h3>Distribución de Planes</h3>
                    <div style={{ width: '100%', height: 300 }}>
                        <ResponsiveContainer>
                            <PieChart>
                                <Pie
                                    data={dataPlanes}
                                    cx="50%"
                                    cy="50%"
                                    labelLine={false}
                                    label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                                    outerRadius={100}
                                    fill="#8884d8"
                                    dataKey="value"
                                >
                                    {dataPlanes.map((entry, index) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Pie>
                                <Tooltip />
                            </PieChart>
                        </ResponsiveContainer>
                    </div>
                </div>
            </div>

            {
                // Gestión de Suscripciones
            }
            <div className="card" style={{ marginTop: '2rem' }}>
                <h3>Gestión de Suscripciones</h3>
                <div className="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Usuario</th>
                                <th>Plan</th>
                                <th>Estado</th>
                                <th>Inicio</th>
                                <th>Próximo Cobro</th>
                                <th>Auto-Renovar</th>
                            </tr>
                        </thead>
                        <tbody>
                            {suscripciones.map(sub => (
                                <tr key={sub.id}>
                                    <td>
                                        <div style={{ fontWeight: '600' }}>{sub.usuarioNombre}</div>
                                        <div style={{ fontSize: '0.75rem', color: '#718096' }}>ID: {sub.usuarioId}</div>
                                    </td>
                                    <td>{sub.planNombre}</td>
                                    <td>
                                        <span className={`badge badge-${sub.estado === 'ACTIVA' ? 'success' : 'danger'}`}>
                                            {sub.estado}
                                        </span>
                                    </td>
                                    <td>{sub.fechaInicio}</td>
                                    <td>{sub.fechaProximoCobro}</td>
                                    <td>{sub.autoRenovacion ? 'Sí' : 'No'}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            {
                // Lista de Usuarios
            }
            <div className="card" style={{ marginTop: '2rem' }}>
                <h3>Usuarios del Sistema</h3>
                <div className="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>Rol</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            {usuarios.map(user => (
                                <tr key={user.id}>
                                    <td>{user.id}</td>
                                    <td>{user.nombre} {user.apellido}</td>
                                    <td>{user.email}</td>
                                    <td>
                                        <span className={`badge ${user.role === 'ADMIN' ? 'badge-info' : 'badge-secondary'}`}>
                                            {user.role}
                                        </span>
                                    </td>
                                    <td>
                                        <span className={`badge ${user.activo ? 'badge-success' : 'badge-danger'}`}>
                                            {user.activo ? 'Activo' : 'Inactivo'}
                                        </span>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default AdminDashboard;
