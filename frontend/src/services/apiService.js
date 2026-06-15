/**
 * API Service - Todos los datos son mocks hasta que el backend esté listo
 */

const mockUsuarios = [
  {
    id: 1,
    email: 'usuario@tc.com',
    nombreCompleto: 'Juan Usuario',
    rol: 'USER',
    activo: true,
    fechaCreacion: '2026-01-15T10:00:00',
    ultimoLogin: '2026-06-15T09:30:00'
  },
  {
    id: 2,
    email: 'admin@tc.com',
    nombreCompleto: 'María Admin',
    rol: 'ADMIN',
    activo: true,
    fechaCreacion: '2026-01-01T08:00:00',
    ultimoLogin: '2026-06-15T10:00:00'
  },
  {
    id: 3,
    email: 'supervisor@tc.com',
    nombreCompleto: 'Carlos Supervisor',
    rol: 'USER',
    activo: true,
    fechaCreacion: '2026-02-10T12:00:00',
    ultimoLogin: '2026-06-14T15:45:00'
  }
];

export const login = async (email, password) => {
  await new Promise(resolve => setTimeout(resolve, 600));

  const validCredentials = {
    'usuario@tc.com': 'password123',
    'admin@tc.com': 'admin123'
  };

  if (!validCredentials[email] || validCredentials[email] !== password) {
    throw new Error('Credenciales inválidas');
  }

  const usuario = mockUsuarios.find(u => u.email === email);
  const token = 'mock_token_' + email + '_' + Date.now();

  localStorage.setItem('token', token);
  localStorage.setItem('usuario', JSON.stringify(usuario));

  return {
    token,
    usuario,
    todasLasRecogidas: []
  };
};


export const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('usuario');
};

export const getCurrentUser = () => {
  const usuario = localStorage.getItem('usuario');
  return usuario ? JSON.parse(usuario) : null;
};

const mockRecogidas = [
  {
    id: 1,
    ficha: 12345,
    empresa: 'TRANSPORTES ABC',
    vin: '1HGBH41JXMN109186',
    marca: 'Honda',
    modelo: 'Civic',
    buque: 'EMMA MAERSK',
    matricula: 'TF-1234-AA',
    fechaHora: '2026-06-15T09:00:00',
    idBarco: 101,
    estado: 'ENTREGADO',
    usuarioEmail: 'usuario@tc.com'
  },
  {
    id: 2,
    ficha: 12346,
    empresa: 'TRANSPORTES XYZ',
    vin: '1HGBH41JXMN109187',
    marca: 'Toyota',
    modelo: 'Corolla',
    buque: 'EVER GIVEN',
    matricula: 'TF-5678-BB',
    fechaHora: '2026-06-14T14:30:00',
    idBarco: 102,
    estado: 'DEVUELTO',
    usuarioEmail: 'usuario@tc.com'
  },
  {
    id: 3,
    ficha: 12347,
    empresa: 'LOGISTICA CANARIA',
    vin: '1HGBH41JXMN109188',
    marca: 'Ford',
    modelo: 'Transit',
    buque: 'MAERSK SEATRADE',
    matricula: 'TF-9999-CC',
    fechaHora: '2026-06-13T10:15:00',
    idBarco: 103,
    estado: 'ENTREGADO',
    usuarioEmail: 'admin@tc.com'
  }
];

export const listarRecogidas = async (filters = {}) => {
  await new Promise(resolve => setTimeout(resolve, 300));
  let resultado = [...mockRecogidas];

  if (filters.vin) {
    resultado = resultado.filter(r => r.vin.toUpperCase().includes(filters.vin.toUpperCase()));
  }
  if (filters.estado) {
    resultado = resultado.filter(r => r.estado === filters.estado);
  }
  if (filters.empresa) {
    resultado = resultado.filter(r => r.empresa.toUpperCase().includes(filters.empresa.toUpperCase()));
  }
  return resultado;
};

export const obtenerRecogidaPorId = async (id) => {
  await new Promise(resolve => setTimeout(resolve, 200));
  const recogida = mockRecogidas.find(r => r.id === parseInt(id));
  if (!recogida) throw new Error('No encontrado');
  return recogida;
};

export const obtenerRecogidaPorVin = async (vin) => {
  await new Promise(resolve => setTimeout(resolve, 300));
  const recogida = mockRecogidas.find(r => r.vin.toUpperCase() === vin.toUpperCase());
  if (!recogida) throw new Error('VIN no encontrado');
  return recogida;
};

export const crearRecogida = async (recogidaData) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  const usuario = getCurrentUser();
  const newRecogida = {
    id: mockRecogidas.length + 1,
    ...recogidaData,
    fechaHora: new Date().toISOString(),
    estado: 'ENTREGADO',
    usuarioEmail: usuario?.email || 'sistema@tc.com'
  };
  mockRecogidas.push(newRecogida);
  return newRecogida;
};

export const actualizarRecogida = async (id, recogidaData) => {
  await new Promise(resolve => setTimeout(resolve, 400));
  const recogida = mockRecogidas.find(r => r.id === parseInt(id));
  if (!recogida) throw new Error('No encontrado');
  Object.assign(recogida, recogidaData);
  return recogida;
};

export const cambiarEstadoRecogida = async (id, nuevoEstado) => {
  await new Promise(resolve => setTimeout(resolve, 400));
  const recogida = mockRecogidas.find(r => r.id === parseInt(id));
  if (!recogida) throw new Error('No encontrado');
  recogida.estado = nuevoEstado;
  return recogida;
};

export const crearUsuario = async (usuarioData) => {
  await new Promise(resolve => setTimeout(resolve, 500));
  const newUser = {
    id: mockUsuarios.length + 1,
    email: usuarioData.email,
    nombreCompleto: usuarioData.nombreCompleto,
    rol: usuarioData.rol || 'USER',
    activo: true,
    fechaCreacion: new Date().toISOString(),
    ultimoLogin: null
  };
  mockUsuarios.push(newUser);
  return newUser;
};

export const listarUsuarios = async () => {
  await new Promise(resolve => setTimeout(resolve, 300));
  return mockUsuarios;
};

export const obtenerEstadisticas = async () => {
  await new Promise(resolve => setTimeout(resolve, 400));
  return {
    totalRecogidas: mockRecogidas.length,
    recogidaEntregadas: mockRecogidas.filter(r => r.estado === 'ENTREGADO').length,
    recogidaDevueltas: mockRecogidas.filter(r => r.estado === 'DEVUELTO').length,
    totalUsuarios: mockUsuarios.length,
    empresas: [...new Set(mockRecogidas.map(r => r.empresa))]
  };
};

