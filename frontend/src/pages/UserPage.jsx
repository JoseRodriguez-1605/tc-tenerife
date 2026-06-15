import { useState, useEffect } from 'react';
import { 
  listarRecogidas, 
  obtenerRecogidaPorVin, 
  crearRecogida,
  actualizarRecogida,
  cambiarEstadoRecogida,
  getCurrentUser 
} from '../services/apiService';
import '../styles/UserPage.css';

export default function UserPage() {
  const [recogidas, setRecogidas] = useState([]);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState('');
  const [usuario, setUsuario] = useState(null);
  const [modoFormulario, setModoFormulario] = useState(false);
  const [formularioData, setFormularioData] = useState({
    ficha: '',
    empresa: '',
    vin: '',
    marca: '',
    modelo: '',
    buque: '',
    matricula: '',
    idBarco: '',
  });
  const [filtroVin, setFiltroVin] = useState('');
  const [filtroEstado, setFiltroEstado] = useState('');
  const [recogidaSeleccionada, setRecogidaSeleccionada] = useState(null);

  // Cargar usuario actual
  useEffect(() => {
    const user = getCurrentUser();
    setUsuario(user);
  }, []);

  // Cargar recogidas al montar componente
  useEffect(() => {
    cargarRecogidas();
  }, []);

  const cargarRecogidas = async () => {
    setCargando(true);
    setError('');
    try {
      const filters = {};
      if (filtroEstado) filters.estado = filtroEstado;
      
      const datos = await listarRecogidas(filters);
      setRecogidas(datos);
    } catch (err) {
      setError('Error al cargar recogidas: ' + err.message);
      console.error(err);
    } finally {
      setCargando(false);
    }
  };

  const buscarPorVin = async (e) => {
    e.preventDefault();
    if (!filtroVin.trim()) {
      cargarRecogidas();
      return;
    }

    setCargando(true);
    setError('');
    try {
      const recogida = await obtenerRecogidaPorVin(filtroVin);
      setRecogidas(recogida ? [recogida] : []);
    } catch (err) {
      setError('VIN no encontrado: ' + err.message);
      setRecogidas([]);
    } finally {
      setCargando(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormularioData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleCrearRecogida = async (e) => {
    e.preventDefault();
    setError('');
    setCargando(true);

    try {
      const dataToSend = {
        ficha: parseInt(formularioData.ficha) || null,
        empresa: formularioData.empresa,
        vin: formularioData.vin.toUpperCase(),
        marca: formularioData.marca,
        modelo: formularioData.modelo,
        buque: formularioData.buque,
        matricula: formularioData.matricula,
        idBarco: parseInt(formularioData.idBarco) || null,
      };

      await crearRecogida(dataToSend);
      setError('');
      alert('Recogida creada exitosamente');
      setFormularioData({
        ficha: '',
        empresa: '',
        vin: '',
        marca: '',
        modelo: '',
        buque: '',
        matricula: '',
        idBarco: '',
      });
      setModoFormulario(false);
      await cargarRecogidas();
    } catch (err) {
      setError('Error al crear recogida: ' + err.message);
    } finally {
      setCargando(false);
    }
  };

  const handleActualizarEstado = async (recogidaId, nuevoEstado) => {
    setCargando(true);
    setError('');

    try {
      await cambiarEstadoRecogida(recogidaId, nuevoEstado);
      alert('Estado actualizado correctamente');
      await cargarRecogidas();
    } catch (err) {
      setError('Error al actualizar estado: ' + err.message);
    } finally {
      setCargando(false);
    }
  };

  return (
    <div className="user-page">
      <header className="page-header">
        <h1>Portal de Recogidas</h1>
        {usuario && <p className="usuario-info">Bienvenido, {usuario.nombreCompleto} ({usuario.email})</p>}
      </header>

      {error && <div className="alert alert-error">{error}</div>}

      <div className="page-content">
        {/* Panel de búsqueda y filtros */}
        <section className="search-section">
          <div className="search-form">
            <form onSubmit={buscarPorVin} className="form-row">
              <input
                type="text"
                placeholder="Buscar por VIN..."
                value={filtroVin}
                onChange={(e) => setFiltroVin(e.target.value)}
                className="input-field"
              />
              <button type="submit" className="btn btn-primary" disabled={cargando}>
                {cargando ? 'Buscando...' : 'Buscar'}
              </button>
              <button 
                type="button" 
                className="btn btn-secondary"
                onClick={() => {
                  setFiltroVin('');
                  cargarRecogidas();
                }}
              >
                Limpiar
              </button>
            </form>

            <div className="filter-row">
              <select 
                value={filtroEstado} 
                onChange={(e) => setFiltroEstado(e.target.value)}
                className="input-field"
              >
                <option value="">Todos los estados</option>
                <option value="ENTREGADO">Entregado</option>
                <option value="DEVUELTO">Devuelto</option>
              </select>
              <button 
                className="btn btn-primary"
                onClick={cargarRecogidas}
                disabled={cargando}
              >
                {cargando ? 'Cargando...' : 'Filtrar'}
              </button>
            </div>
          </div>

          <button 
            className="btn btn-success"
            onClick={() => setModoFormulario(!modoFormulario)}
          >
            {modoFormulario ? 'Cancelar' : '+ Nueva Recogida'}
          </button>
        </section>

        {/* Formulario de crear recogida */}
        {modoFormulario && (
          <section className="form-section">
            <h2>Registrar Nueva Recogida</h2>
            <form onSubmit={handleCrearRecogida} className="form-grid">
              <div className="form-group">
                <label>Ficha *</label>
                <input
                  type="number"
                  name="ficha"
                  value={formularioData.ficha}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                />
              </div>

              <div className="form-group">
                <label>Empresa *</label>
                <input
                  type="text"
                  name="empresa"
                  value={formularioData.empresa}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                />
              </div>

              <div className="form-group">
                <label>VIN *</label>
                <input
                  type="text"
                  name="vin"
                  value={formularioData.vin}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                  placeholder="ej: 1HGBH41JXMN109186"
                />
              </div>

              <div className="form-group">
                <label>Marca *</label>
                <input
                  type="text"
                  name="marca"
                  value={formularioData.marca}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                />
              </div>

              <div className="form-group">
                <label>Modelo *</label>
                <input
                  type="text"
                  name="modelo"
                  value={formularioData.modelo}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                />
              </div>

              <div className="form-group">
                <label>Buque *</label>
                <input
                  type="text"
                  name="buque"
                  value={formularioData.buque}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                />
              </div>

              <div className="form-group">
                <label>Matrícula *</label>
                <input
                  type="text"
                  name="matricula"
                  value={formularioData.matricula}
                  onChange={handleInputChange}
                  required
                  className="input-field"
                />
              </div>

              <div className="form-group">
                <label>ID Barco</label>
                <input
                  type="number"
                  name="idBarco"
                  value={formularioData.idBarco}
                  onChange={handleInputChange}
                  className="input-field"
                />
              </div>

              <button type="submit" className="btn btn-success" disabled={cargando}>
                {cargando ? 'Creando...' : 'Registrar Recogida'}
              </button>
            </form>
          </section>
        )}

        {/* Lista de recogidas */}
        <section className="recogidas-section">
          <h2>Recogidas ({recogidas.length})</h2>
          
          {cargando && !modoFormulario && <p className="loading">Cargando recogidas...</p>}
          
          {!cargando && recogidas.length === 0 && (
            <p className="empty-state">No hay recogidas para mostrar</p>
          )}

          {recogidas.length > 0 && (
            <div className="table-responsive">
              <table className="recogidas-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Ficha</th>
                    <th>VIN</th>
                    <th>Empresa</th>
                    <th>Vehículo</th>
                    <th>Matrícula</th>
                    <th>Estado</th>
                    <th>Fecha</th>
                    <th>Acciones</th>
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
                      <td className="fecha">
                        {recogida.fechaHora 
                          ? new Date(recogida.fechaHora).toLocaleDateString('es-ES')
                          : 'N/A'
                        }
                      </td>
                      <td className="acciones">
                        <button
                          className="btn btn-sm btn-info"
                          onClick={() => setRecogidaSeleccionada(recogida)}
                          title="Ver detalles"
                        >
                          👁️
                        </button>
                        {recogida.estado === 'ENTREGADO' && (
                          <button
                            className="btn btn-sm btn-warning"
                            onClick={() => handleActualizarEstado(recogida.id, 'DEVUELTO')}
                            title="Marcar como devuelto"
                          >
                            ↩️
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>

        {/* Modal de detalles */}
        {recogidaSeleccionada && (
          <div className="modal">
            <div className="modal-content">
              <span className="close" onClick={() => setRecogidaSeleccionada(null)}>&times;</span>
              <h2>Detalles de Recogida</h2>
              <div className="detail-grid">
                <div className="detail-item">
                  <strong>ID:</strong> {recogidaSeleccionada.id}
                </div>
                <div className="detail-item">
                  <strong>VIN:</strong> {recogidaSeleccionada.vin}
                </div>
                <div className="detail-item">
                  <strong>Empresa:</strong> {recogidaSeleccionada.empresa}
                </div>
                <div className="detail-item">
                  <strong>Vehículo:</strong> {recogidaSeleccionada.marca} {recogidaSeleccionada.modelo}
                </div>
                <div className="detail-item">
                  <strong>Matrícula:</strong> {recogidaSeleccionada.matricula}
                </div>
                <div className="detail-item">
                  <strong>Buque:</strong> {recogidaSeleccionada.buque}
                </div>
                <div className="detail-item">
                  <strong>Estado:</strong> <span className={`badge badge-${recogidaSeleccionada.estado?.toLowerCase()}`}>{recogidaSeleccionada.estado}</span>
                </div>
                <div className="detail-item">
                  <strong>Fecha/Hora:</strong> {new Date(recogidaSeleccionada.fechaHora).toLocaleString('es-ES')}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
