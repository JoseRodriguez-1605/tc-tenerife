import { useState, useEffect } from 'react';
import UserPage from './pages/UserPage';
import AdminPage from './pages/AdminPage';
import { getCurrentUser, logout, login } from './services/apiService';
import './App.css';

export default function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [currentView, setCurrentView] = useState('login'); // login, user, admin
  const [loginData, setLoginData] = useState({ email: '', password: '' });
  const [loginError, setLoginError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  // Verificar si hay usuario logueado al cargar
  useEffect(() => {
    const user = getCurrentUser();
    if (user) {
      setCurrentUser(user);
      setCurrentView(user.rol === 'ADMIN' ? 'admin' : 'user');
    }
  }, []);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoginError('');
    setIsLoading(true);

    try {
      const result = await login(loginData.email, loginData.password);
      setCurrentUser(result.usuario);
      setCurrentView(result.usuario.rol === 'ADMIN' ? 'admin' : 'user');
      setLoginData({ email: '', password: '' });
    } catch (error) {
      setLoginError('Error: ' + error.message);
      console.error('Login error:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    setCurrentUser(null);
    setCurrentView('login');
    setLoginData({ email: '', password: '' });
  };

  // Vista de Login
  if (currentView === 'login') {
    return (
      <div className="login-page">
        <div className="login-container">
          <div className="login-box">
            <h1>🏭 TC Tenerife</h1>
            <h2>Sistema de Recogidas</h2>

            <form onSubmit={handleLogin} className="login-form">
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={loginData.email}
                  onChange={(e) =>
                    setLoginData({ ...loginData, email: e.target.value })
                  }
                  required
                  className="input-field"
                  placeholder="usuario@ejemplo.com"
                />
              </div>

              <div className="form-group">
                <label>Contraseña</label>
                <input
                  type="password"
                  value={loginData.password}
                  onChange={(e) =>
                    setLoginData({ ...loginData, password: e.target.value })
                  }
                  required
                  className="input-field"
                  placeholder="••••••••"
                />
              </div>

              {loginError && <div className="alert alert-error">{loginError}</div>}

              <button
                type="submit"
                className="btn btn-primary btn-block"
                disabled={isLoading}
              >
                {isLoading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
              </button>
            </form>

            <div className="login-footer">
              <p>
                <strong>Credenciales de prueba:</strong>
              </p>
              <p>Usuario: usuario@tc.com</p>
              <p>Admin: admin@tc.com</p>
            </div>
          </div>
        </div>
      </div>
    );
  }

  // Vista de Usuario o Admin
  return (
    <div className="app-layout">
      <nav className="app-navbar">
        <div className="navbar-brand">🏭 TC Tenerife</div>
        <div className="navbar-menu">
          <span className="user-badge">
            {currentUser?.nombreCompleto} ({currentUser?.rol})
          </span>
          <button onClick={handleLogout} className="btn btn-logout">
            Cerrar Sesión
          </button>
        </div>
      </nav>

      <main className="app-main">
        {currentView === 'user' && <UserPage />}
        {currentView === 'admin' && <AdminPage />}
      </main>
    </div>
  );
}