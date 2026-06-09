package com.tctenerife.controller;
 
import com.tctenerife.dto.*;
import com.tctenerife.service.UsuarioService;
import com.tctenerife.service.EstadisticasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.time.LocalDateTime;
import java.util.List;
 
/**
 * Controlador para endpoints de admin
 */
@RestController
@RequestMapping("/api/admin")
@Slf4j
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EstadisticasService estadisticasService;
    
    /**
     * POST /api/admin/usuarios
     * Crear nuevo usuario
     */
    @PostMapping("/usuarios")
    public ResponseEntity<ApiResponse<UsuarioDTO>> crearUsuario(
            @RequestBody CreateUserRequest request) {
        try {
            UsuarioDTO response = usuarioService.crearUsuario(request);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Usuario creado",
                    response,
                    LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    "Error: " + e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }
    
    /**
     * GET /api/admin/usuarios
     * Listar todos los usuarios
     */
    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> listarUsuarios() {
        try {
            List<UsuarioDTO> response = usuarioService.listarActivos();
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Usuarios obtenidos",
                    response,
                    LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    "Error: " + e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }
    
    /**
     * GET /api/admin/estadisticas
     * Obtener estadísticas del dashboard
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<EstadisticasDTO>> getEstadisticas() {
        try {
            EstadisticasDTO response = estadisticasService.getEstadisticas();
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Estadísticas obtenidas",
                    response,
                    LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    "Error: " + e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }
}
