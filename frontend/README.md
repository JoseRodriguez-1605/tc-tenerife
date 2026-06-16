# TC-TENERIFE - Backend (API REST)

Este es el backend de la aplicación TC-Tenerife, desarrollado en Java utilizando el ecosistema de Spring Boot. Proporciona una API REST sólida y estructurada para la gestión de usuarios, vehículos y controles de recogida de datos.

## Estructura del Proyecto

El código está organizado siguiendo una arquitectura en capas dentro del paquete principal com.tctenerife:

* controller: Capa de exposición de endpoints HTTP (REST Controllers). Contiene AdminController y RecogidaController.
* service: Capa que alberga la lógica de negocio de la aplicación (UsuarioService, RecogidaService, EstadisticasService).
* repository: Capa de acceso a datos utilizando Spring Data JPA para la comunicación con la base de datos.
* entity: Clases que representan las tablas de la base de datos (Usuario, Vehiculo, ControlRecogida, SincronizacionLog).
* dto: Objetos de Transferencia de Datos (Data Transfer Objects) para desacoplar las entidades de la API externa (UsuarioDTO, VehiculoDTO).


## Base de Datos

Me faltó la Base de datos para guardar los datos correctamente.

## Instalación y Ejecución en Local



### Pasos para arrancar
1. Configura las credenciales de tu base de datos en el archivo src/main/resources/application.properties.
2. Abre una terminal en la carpeta backend y compila el proyecto:
   ```bash
   mvn clean install



   ---

### Archivo para frontend/README.md

```markdown
# TC-TENERIFE - Frontend

Este es el frontend de la aplicación TC-Tenerife, una interfaz web desarrollada con React y compilada utilizando Vite.

## Estructura del Proyecto

El código fuente se encuentra dentro de la carpeta src y está estructurado de forma sencilla y modular:

* pages: Contiene las pantallas principales de la aplicación.
  * AdminPage.jsx: Panel de administración.
  * UserPage.jsx: Vista para los usuarios de la aplicación.
* components: Componentes visuales reutilizables compartidos entre las páginas.
* services: Módulos encargados de la comunicación con la API Backend (contiene apiService.js para las peticiones HTTP).
* styles: Archivos de estilos CSS personalizados para cada página y componente (AdminPage.css, UserPage.css, App.css).

## Tecnologías Utilizadas

* React
* Vite
* CSS3
* ESLint

## Instalación y Ejecución en Local

### Prerrequisitos
* Tener instalado Node.js (Versión LTS recomendada).

### Pasos para arrancar
1. Abre una terminal en la carpeta frontend.
2. Instala todas las dependencias necesarias:
   ```bash
   npm install