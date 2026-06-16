package com.tctenerife.controller;
 
import com.tctenerife.dto.*;
import com.tctenerife.service.RecogidaService;
//import com.tctenerife.config.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
 
import java.time.LocalDateTime;
import java.util.List;
 
/**
 * Controlador para endpoints de recogidas
 */
@RestController
@RequestMapping("/api/recogidas")
@Slf4j
@CrossOrigin(origins = "*")
public class RecogidaController {
    
    @Autowired
    private RecogidaService recogidaService;
    
    @Autowired
   // private JwtTokenProvider jwtTokenProvider;
    
    /**
     * POST /api/recogidas
     * Crear nueva recogida
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RecogidaDTO>> crearRecogida(
            @RequestBody RecogidaRequest request,
            Authentication authentication) {
        try {
            Long usuarioId = jwtTokenProvider.getUserIdFromToken(
                    authentication.getCredentials().toString()); 
            
            RecogidaDTO response = recogidaService.registrar(request, usuarioId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Recogida registrada exitosamente",
                    response,
                    LocalDateTime.now()
            ));
        } catch (Exception e) {
            log.error("Error creando recogida: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    "Error: " + e.getMessage(),
                    null,
                    LocalDateTime.now()
            ));
        }
    }
    
    /**
     * GET /api/recogidas/{id}
     * Obtener recogida por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecogidaDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            RecogidaDTO response = recogidaService.obtenerPorId(id);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Recogida obtenida",
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
     * GET /api/recogidas/vin/{vin}
     * Obtener recogida por VIN
     */
    @GetMapping("/vin/{vin}")
    public ResponseEntity<ApiResponse<RecogidaDTO>> obtenerPorVin(@PathVariable String vin) {
        try {
            RecogidaDTO response = recogidaService.obtenerPorVin(vin.toUpperCase());
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Recogida obtenida",
                    response,
                    LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(
                    false,
                    "VIN no encontrado",
                    null,
                    LocalDateTime.now()
            ));
        }
    }
    
    /**
     * GET /api/recogidas
     * Listar todas las recogidas con filtros opcionales
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<RecogidaDTO>>> listar(
            @RequestParam(required = false) String vin,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String buque,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String estado) {
        try {
            List<RecogidaDTO> response;
            
            if (vin != null || empresa != null || marca != null || buque != null || modelo != null || estado != null) {
                response = recogidaService.buscar(vin, empresa, marca, buque, modelo, estado);
            } else {
                response = recogidaService.listarTodas();
            }
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Recogidas obtenidas",
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
     * PUT /api/recogidas/{id}
     * Actualizar recogida
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecogidaDTO>> actualizar(
            @PathVariable Integer id,
            @RequestBody RecogidaRequest request,
            Authentication authentication) {
        try {
            //Long usuarioId = jwtTokenProvider.getUserIdFromToken(
                   // authentication.getCredentials().toString());
            
            RecogidaDTO response = recogidaService.actualizar(id, request, usuarioId);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Recogida actualizada",
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
     * PATCH /api/recogidas/{id}/estado
     * Cambiar estado de recogida
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<RecogidaDTO>> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        try {
            RecogidaDTO response = recogidaService.cambiarEstado(id, nuevoEstado);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Estado actualizado",
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
     * DELETE /api/recogidas/{id}
     * Eliminar recogida (ADMIN ONLY)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Integer id) {
        try {
            recogidaService.eliminar(id);
            
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    "Recogida eliminada",
                    null,
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