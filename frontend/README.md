# TC Tenerife - Frontend

Sistema de gestión de recogidas portuarias para TC Tenerife. Plataforma web construida con **React + Vite** con autenticación, vistas de usuario y panel de administración.

## Características

✅ **Sistema de autenticación** - Login con email/contraseña  
✅ **Vista de usuario** - Registro y seguimiento de recogidas de vehículos  
✅ **Panel de administración** - CRUD completo de usuarios y estadísticas  
✅ **Diseño limpio y funcional** - Sin dependencias innecesarias  
✅ **Conectado a backend Java** - Integración total con API REST

## Estructura de Carpetas

```
frontend/
├── src/
│   ├── pages/
│   │   ├── UserPage.jsx        # Vista principal de usuarios
│   │   └── AdminPage.jsx       # Panel de administración
│   ├── services/
│   │   └── apiService.js       # Funciones para llamadas HTTP
│   ├── styles/
│   │   ├── UserPage.css        # Estilos de página de usuario
│   │   └── AdminPage.css       # Estilos de panel admin
│   ├── App.jsx                 # Componente principal con router
│   ├── App.css                 # Estilos globales
│   ├── main.jsx                # Punto de entrada
│   └── index.css               # Estilos base
├── .env.example                # Plantilla de configuración
├── package.json                # Dependencias
└── vite.config.js              # Configuración de Vite
```

## Instalación

### 1. Instalación de dependencias

```bash
cd frontend
npm install
```

### 2. Configurar archivo .env

Copia `.env.example` a `.env` y ajusta según sea necesario:

```bash
cp .env.example .env
```

**Contenido del .env:**
```env
VITE_API_URL=http://localhost:8080
```

### 3. Iniciar servidor de desarrollo

```bash
npm run dev
```

El frontend estará disponible en: **http://localhost:5173**

## Ejecución

### Desarrollo

```bash
npm run dev
```

### Build para producción

```bash
npm run build
```

### Preview de producción localmente

```bash
npm run preview
```

## Funcionalidades por rol

### 👤 Usuario Regular

- **Ver recogidas**: Listar todas las recogidas registradas
- **Buscar por VIN**: Buscar una recogida específica por número de VIN
- **Filtrar por estado**: Ver recogidas entregadas o devueltas
- **Registrar recogida**: Crear nueva recogida de vehículo
- **Cambiar estado**: Marcar recogida como devuelta
- **Ver detalles**: Modal con información completa de cada recogida

### 🔐 Administrador

**Dashboard:**
- Estadísticas generales (total recogidas, entregadas, devueltas, usuarios activos)
- Resumen de empresas

**Gestión de Recogidas:**
- Listar todas las recogidas
- Ver detalles de cada recogida
- Filtrar por estado, empresa, etc.

**Gestión de Usuarios:**
- Crear nuevos usuarios (regular o admin)
- Listar todos los usuarios
- Ver estado de actividad (activo/inactivo)
- Ver último login

## Endpoints del Backend Utilizados

### Autenticación
```
POST /api/auth/login - Iniciar sesión
```

### Recogidas (Públicas)
```
GET /api/recogidas - Listar todas
GET /api/recogidas?vin=...&estado=... - Con filtros
GET /api/recogidas/{id} - Obtener por ID
GET /api/recogidas/vin/{vin} - Obtener por VIN
POST /api/recogidas - Crear nueva
PUT /api/recogidas/{id} - Actualizar
PATCH /api/recogidas/{id}/estado?nuevoEstado=... - Cambiar estado
```

### Admin - Usuarios
```
POST /api/admin/usuarios - Crear usuario
GET /api/admin/usuarios - Listar usuarios
```

### Admin - Estadísticas
```
GET /api/admin/estadisticas - Obtener estadísticas
```

## Credenciales de Prueba

Usa estas credenciales para testear las diferentes funcionalidades:

**Usuario Normal:**
- Email: `usuario@tc.com`
- Contraseña: `password123`

**Administrador:**
- Email: `admin@tc.com`
- Contraseña: `admin123`

> ⚠️ **NOTA**: El endpoint `/api/auth/login` debe ser implementado en el backend si aún no existe.

## Requisitos Previos

- Node.js 16+ 
- npm o yarn
- Backend Java ejecutándose en `http://localhost:8080`

## Tecnologías Utilizadas

- **React 18** - Framework UI
- **Vite** - Build tool
- **CSS Puro** - Diseño limpio sin dependencias
- **Fetch API** - Comunicación HTTP

## Configuración CORS

El backend debe permitir CORS desde `http://localhost:5173`:

```java
@CrossOrigin(origins = "*")
// o específicamente:
@CrossOrigin(origins = "http://localhost:5173")
```

## Notas Importantes

⚠️ **Endpoint de Login faltante**: El endpoint `POST /api/auth/login` no fue encontrado en el backend. Necesita ser implementado para la autenticación completa.

⚠️ **Token JWT**: Se almacena en `localStorage`. Para mejor seguridad, considerar usar `HttpOnly` cookies en producción.

⚠️ **CORS**: Actualmente configurado con `origins = "*"`. En producción, especificar dominio exacto.

## Desarrollo Futuro

- [ ] Implementar React Router para navegación más robusta
- [ ] Agregar validaciones más complejas
- [ ] Implementar paginación en tablas
- [ ] Agregar exportación a CSV/PDF
- [ ] Implementar búsqueda avanzada
- [ ] Agregar notificaciones toast
- [ ] Modo oscuro

## Troubleshooting

**Error: CORS policy**
- Asegurar que el backend tiene CORS habilitado
- Verificar que la URL en `.env` es correcta

**Error: Cannot read property 'token'**
- El endpoint de login no devuelve un token
- Verificar respuesta del servidor

**Tabla vacía**
- Verificar que el backend está ejecutándose
- Revisar la consola del navegador para errores HTTP

## Licencia

Privado - TC Tenerife
