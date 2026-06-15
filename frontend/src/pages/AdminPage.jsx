import { useState, useEffect } from 'react';
import {
  listarRecogidas,
  crearUsuario,
  listarUsuarios,
  obtenerEstadisticas,
  getCurrentUser
} from '../services/apiService';
import '../styles/AdminPage.css';

export default function AdminPage() {
  const [usuario, setUsuario] = useState(null);
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);

  // Estados para usuarios
  const [usuarios, setUsuarios] = useState([]);
  const [mostrarFormUsuario, setMostrarFormUsuario] = useState(false);
  const [formUsuario, setFormUsuario] = useState({
    email: '',
    nombreCompleto: '',
    password: '',
    rol: 'USER',
  });

  // Estados para recogidas
  const [recogidas, setRecogidas] = useState([]);

  // Estados para estadísticas
  const [estadisticas, setEstadisticas] = useState(null);

  // Tab activo
  const [tabActiva, setTabActiva] = useState('dashboard'); // dashboard, recogidas, usuarios

  // Cargar usuario actual
  useEffect(() => {
    const user = getCurrentUser();
    setUsuario(user);
  }, []);

  // Cargar datos iniciales
  useEffect(() => {
    cargarDatos();
  }, [tabActiva]);

  const cargarDatos = async () => {
    setCargando(true);
    setError('');

    try {
      if (tabActiva === 'dashboard') {
        const stats = await obtenerEstadisticas();
        setEstadisticas(stats);
      } else if (tabActiva === 'recogidas') {
        const datos = await listarRecogidas();
        setRecogidas(datos);
      } else if (tabActiva === 'usuarios') {
        const datos = await listarUsuarios();
        setUsuarios(datos);
      }
    } catch (err) {
      setError('Error cargando datos: ' + err.message);
      console.error(err);
    } finally {
      setCargando(false);
    }
  };

  const handleInputChangeUsuario = (e) => {
    const { name, value } = e.target;
    setFormUsuario(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCrearUsuario = async (e) => {
    e.preventDefault();
    setError('');
    setCargando(true);

    try {
      const dataToSend = {
        email: formUsuario.email,
        nombreCompleto: formUsuario.nombreCompleto,
        password: formUsuario.password,
        rol: formUsuario.rol,
      };

      await crearUsuario(dataToSend);
      alert('Usuario creado exitosamente');
      setFormUsuario({
        email: '',
        nombreCompleto: '',
        password: '',
        rol: 'USER',
      });
      setMostrarFormUsuario(false);
      await cargarDatos();
    } catch (err) {
      setError('Error al crear usuario: ' + err.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="admin-page">
      <header className="page-header admin-header">
        <h1>Panel de Administración</h1>
        {usuario && (
          <p className="usuario-info">
            Admin: {usuario.nombreCompleto} ({usuario.email})
          </p>
        )}
      </header>

      {error && <div className="alert alert-error">{error}</div>}

      {/* Navegación de tabs */}
      <nav className="admin-tabs">
        <button
          className={`tab-btn ${tabActiva === 'dashboard' ? 'active' : ''}`}
          onClick={() => setTabActiva('dashboard')}
        >
          📊 Dashboard
        </button>
        <button
          className={`tab-btn ${tabActiva === 'recogidas' ? 'active' : ''}`}
          onClick={() => setTabActiva('recogidas')}
        >
          🚗 Recogidas
        </button>
        <button
          className={`tab-btn ${tabActiva === 'usuarios' ? 'active' : ''}`}
          onClick={() => setTabActiva('usuarios')}
        >
          👥 Usuarios
        </button>
      </nav>

      <div className="admin-content">
        {/* ==================== TAB: DASHBOARD ==================== */}
        {tabActiva === 'dashboard' && (
          <section className="tab-section">
            <h2>Dashboard</h2>

            {cargando && <p className="loading">Cargando estadísticas...</p>}

            {estadisticas && !cargando && (
              <div className="stats-grid">
                <div className="stat-card">
                  <div className="stat-number">{estadisticas.totalRecogidas || 0}</div>
                  <div className="stat-label">Total Recogidas</div>
                </div>

                <div className="stat-card">
                  <div className="stat-number">{estadisticas.recogidaEntregadas || 0}</div>
                  <div className="stat-label">Entregadas</div>
                </div>

                <div className="stat-card">
                  <div className="stat-number">{estadisticas.recogidaDevueltas || 0}</div>
                  <div className="stat-label">Devueltas</div>
                </div>

                <div className="stat-card">
                  <div className="stat-number">{estadisticas.totalUsuarios || 0}</div>
                  <div className="stat-label">Usuarios Activos</div>
                </div>

                <div className="stat-card">
                  <div className="stat-number">{estadisticas.empresas?.length || 0}</div>
                  <div className="stat-label">Empresas</div>
                </div>
              </div>
            )}
          </section>
        )}

        {/* ==================== TAB: RECOGIDAS ==================== */}
        {tabActiva === 'recogidas' && (
          <section className="tab-section">
            <div className="section-header">
              <h2>Gestión de Recogidas</h2>
              <button
                className="btn btn-primary"
                onClick={cargarDatos}
                disabled={cargando}
              >
                {cargando ? 'Cargando...' : '🔄 Actualizar'}
              </button>
            </div>

            {cargando && <p className="loading">Cargando recogidas...</p>}

            {!cargando && recogidas.length === 0 && (
              <p className="empty-state">No hay recogidas registradas</p>
            )}

            {recogidas.length > 0 && (
              <div className="table-responsive">
                <table className="admin-table">
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Ficha</th>
                      <th>VIN</th>
                      <th>Empresa</th>
                      <th>Vehículo</th>
                      <th>Matrícula</th>
                      <th>Estado</th>
                      <th>Usuario</th>
                      <th>Fecha</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recogidas.map((recogida) => (
                      <tr key={recogida.id}>
                        <td>{recogida.id}</td>
                        <td>{recogida.ficha}</td>
                        <td className="vin">{recogida.vin}</td>
                        <td>{recogida.empresa}</td>
                        <td>{recogida.marca} {recogida.modelo}</td>
                        <td>{recogida.matricula}</td>
                        <td>
                          <span className={`badge badge-${recogida.estado?.toLowerCase() || 'unknown'}`}>
                            {recogida.estado || 'N/A'}
                          </span>
                        </td>
                        <td>{recogida.usuarioEmail}</td>
                        <td className="fecha">
                          {recogida.fechaHora
                            ? new Date(recogida.fechaHora).toLocaleDateString('es-ES')
                            : 'N/A'
                          }
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </section>
        )}

        {/* ==================== TAB: USUARIOS ==================== */}
        {tabActiva === 'usuarios' && (
          <section className="tab-section">
            <div className="section-header">
              <h2>Gestión de Usuarios</h2>
              <button
                className="btn btn-success"
                onClick={() => setMostrarFormUsuario(!mostrarFormUsuario)}
              >
                {mostrarFormUsuario ? 'Cancelar' : '+ Nuevo Usuario'}
              </button>
            </div>

            {/* Formulario crear usuario */}
            {mostrarFormUsuario && (
              <div className="form-section">
                <h3>Crear Nuevo Usuario</h3>
                <form onSubmit={handleCrearUsuario} className="form-grid">
                  <div className="form-group">
                    <label>Email *</label>
                    <input
                      type="email"
                      name="email"
                      value={formUsuario.email}
                      onChange={handleInputChangeUsuario}
                      required
                      className="input-field"
                      placeholder="usuario@ejemplo.com"
                    />
                  </div>

                  <div className="form-group">
                    <label>Nombre Completo *</label>
                    <input
                      type="text"
                      name="nombreCompleto"
                      value={formUsuario.nombreCompleto}
                      onChange={handleInputChangeUsuario}
                      required
                      className="input-field"
                      placeholder="Juan Pérez"
                    />
                  </div>

                  <div className="form-group">
                    <label>Contraseña *</label>
                    <input
                      type="password"
                      name="password"
                      value={formUsuario.password}
                      onChange={handleInputChangeUsuario}
                      required
                      className="input-field"
                      placeholder="••••••••"
                    />
                  </div>

                  <div className="form-group">
                    <label>Rol *</label>
                    <select
                      name="rol"
                      value={formUsuario.rol}
                      onChange={handleInputChangeUsuario}
                      className="input-field"
                    >
                      <option value="USER">Usuario Regular</option>
                      <option value="ADMIN">Administrador</option>
                    </select>
                  </div>

                  <button type="submit" className="btn btn-success" disabled={cargando}>
                    {cargando ? 'Creando...' : 'Crear Usuario'}
                  </button>
                </form>
              </div>
            )}

            {/* Lista de usuarios */}
            <div className="users-section">
              <div className="section-header">
                <h3>Usuarios Registrados</h3>
                <button
                  className="btn btn-primary"
                  onClick={cargarDatos}
                  disabled={cargando}
                >
                  {cargando ? 'Cargando...' : '🔄'}
                </button>
              </div>

              {cargando && <p className="loading">Cargando usuarios...</p>}

              {!cargando && usuarios.length === 0 && (
                <p className="empty-state">No hay usuarios registrados</p>
              )}

              {usuarios.length > 0 && (
                <div className="table-responsive">
                  <table className="admin-table">
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>Email</th>
                        <th>Nombre</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Fecha Creación</th>
                        <th>Último Login</th>
                      </tr>
                    </thead>
                    <tbody>
                      {usuarios.map((user) => (
                        <tr key={user.id}>
                          <td>{user.id}</td>
                          <td>{user.email}</td>
                          <td>{user.nombreCompleto}</td>
                          <td>
                            <span className={`badge badge-${user.rol?.toLowerCase()}`}>
                              {user.rol}
                            </span>
                          </td>
                          <td>
                            <span className={`badge ${user.activo ? 'badge-success' : 'badge-danger'}`}>
                              {user.activo ? 'Activo' : 'Inactivo'}
                            </span>
                          </td>
                          <td className="fecha">
                            {user.fechaCreacion
                              ? new Date(user.fechaCreacion).toLocaleDateString('es-ES')
                              : 'N/A'
                            }
                          </td>
                          <td className="fecha">
                            {user.ultimoLogin
                              ? new Date(user.ultimoLogin).toLocaleDateString('es-ES')
                              : 'Nunca'
                            }
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </section>
        )}
      </div>
    </div>
  );
}
