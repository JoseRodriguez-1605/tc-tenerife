package com.tctenerife.service;
 
import com.tctenerife.dto.*;
import com.tctenerife.entity.Usuario;
import com.tctenerife.repository.UsuarioRepository;
import com.tctenerife.config.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
 
/**
 * Servicio para gestionar usuarios y autenticación
 */
@Service
@Slf4j
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * Login de usuario
     */
    public UsuarioDTO login(String email, String password) {
        log.info("Intento de login para: {}", email);
        
        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));
        
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            log.warn("Contraseña incorrecta para: {}", email);
            throw new RuntimeException("Credenciales inválidas");
        }
        
        // Actualizar último login
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        log.info("Login exitoso para: {}", email);
        return convertToDTO(usuario);
    }
    
    /**
     * Generar token JWT
     */
    public String generarToken(UsuarioDTO usuario) {
        return jwtTokenProvider.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol());
    }
    
    /**
     * Crear nuevo usuario (solo ADMIN)
     */
    public UsuarioDTO crearUsuario(CreateUserRequest request) {
        log.info("Creando nuevo usuario: {}", request.getEmail());
        
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setRol(Usuario.Rol.valueOf(request.getRol().toUpperCase()));
        usuario.setActivo(true);
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        log.info("Usuario creado: {} con ID: {}", request.getEmail(), savedUsuario.getId());
        
        return convertToDTO(savedUsuario);
    }
    
    /**
     * Obtener usuario por ID
     */
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(usuario);
    }
    
    /**
     * Obtener usuario por email
     */
    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToDTO(usuario);
    }
    
    /**
     * Listar todos los usuarios activos
     */
    public List<UsuarioDTO> listarActivos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar todos los usuarios
     */
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar usuarios por rol
     */
    public List<UsuarioDTO> listarPorRol(String rol) {
        Usuario.Rol rolEnum = Usuario.Rol.valueOf(rol.toUpperCase());
        return usuarioRepository.findByRol(rolEnum).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar usuario
     */
    public UsuarioDTO actualizar(Long id, UpdateUserRequest request) {
        log.info("Actualizando usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (request.getNombreCompleto() != null) {
            usuario.setNombreCompleto(request.getNombreCompleto());
        }
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getRol() != null) {
            usuario.setRol(Usuario.Rol.valueOf(request.getRol().toUpperCase()));
        }
        
        Usuario updatedUsuario = usuarioRepository.save(usuario);
        log.info("Usuario actualizado: {}", id);
        
        return convertToDTO(updatedUsuario);
    }
    
    /**
     * Cambiar contraseña
     */
    public void cambiarContrasena(Long id, String nuevaContrasena) {
        log.info("Cambiando contraseña para usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
        
        log.info("Contraseña cambiada para usuario: {}", id);
    }
    
    /**
     * Desactivar usuario
     */
    public void desactivar(Long id) {
        log.info("Desactivando usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        log.info("Usuario desactivado: {}", id);
    }
    
    /**
     * Activar usuario
     */
    public void activar(Long id) {
        log.info("Activando usuario: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        
        log.info("Usuario activado: {}", id);
    }
    
    /**
     * Eliminar usuario (soft delete)
     */
    public void eliminar(Long id) {
        desactivar(id);
    }
    
    /**
     * Contar usuarios activos
     */
    public Long contarActivos() {
        return usuarioRepository.countActivosTrue();
    }
    
    /**
     * Convertir entidad a DTO
     */
    private UsuarioDTO convertToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.getRol().name(),
                usuario.getActivo(),
                usuario.getFechaCreacion(),
                usuario.getUltimoLogin()
        );
    }

    
    
    /**
     * Listar vehículos por buque
     */
    public List<VehiculoDTO> listarPorBuque(String buque) {
        return vehiculoRepository.findByBuque(buque).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar vehículo
     */
    public VehiculoDTO actualizar(String chassis, VehiculoRequest request) {
        log.info("Actualizando vehículo: {}", chassis);
        
        Vehiculo vehiculo = vehiculoRepository.findByChassis(chassis.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        
        vehiculo.setMarca(request.getMarca());
        vehiculo.setModelo(request.getModelo());
        vehiculo.setBuque(request.getBuque());
        
        Vehiculo updatedVehiculo = vehiculoRepository.save(vehiculo);
        log.info("Vehículo actualizado: {}", chassis);
        
        return convertToDTO(updatedVehiculo);
    }
    
    /**
     * Eliminar vehículo
     */
    public void eliminar(String chassis) {
        log.info("Eliminando vehículo: {}", chassis);
        vehiculoRepository.deleteById(chassis.toUpperCase());
    }
    
    /**
     * Contar vehículos totales
     */
    public Long contarTodos() {
        return vehiculoRepository.count();
    }
    
    /**
     * Convertir entidad a DTO
     */
    private VehiculoDTO convertToDTO(Vehiculo vehiculo) {
        return new VehiculoDTO(
                vehiculo.getChassis(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getBuque()
        );
    }
}
 
/**
 * Servicio adicional para crear DTOs con conversión
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class UpdateUserRequest {
    private String nombreCompleto;
    private String password;
    private String rol;
}
 
@Data
@NoArgsConstructor
@AllArgsConstructor
class VehiculoRequest {
    private String chassis;
    private String marca;
    private String modelo;
    private String buque;
}