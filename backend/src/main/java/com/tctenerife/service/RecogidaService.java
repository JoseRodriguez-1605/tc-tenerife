package com.tctenerife.service;
 
import com.tctenerife.dto.*;
import com.tctenerife.entity.ControlRecogida;
import com.tctenerife.entity.Usuario;
import com.tctenerife.repository.ControlRecogidaRepository;
import com.tctenerife.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
 
/**
 * Servicio completo para gestionar recogidas de vehículos
 */
@Service
@Slf4j
@Transactional
public class RecogidaService {
    
    @Autowired
    private ControlRecogidaRepository recogidaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Registrar nueva recogida de vehículo
     */
    public RecogidaDTO registrar(RecogidaRequest request, Long usuarioId) {
        log.info("Registrando recogida para VIN: {} por usuario: {}", request.getVin(), usuarioId);
        
        // Validar que el VIN no exista
        if (recogidaRepository.findByVin(request.getVin().toUpperCase()).isPresent()) {
            throw new RuntimeException("El VIN ya existe en el sistema");
        }
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        ControlRecogida recogida = new ControlRecogida();
        recogida.setFicha(request.getFicha());
        recogida.setEmpresa(request.getEmpresa());
        recogida.setVin(request.getVin().toUpperCase());
        recogida.setMarca(request.getMarca());
        recogida.setModelo(request.getModelo());
        recogida.setBuque(request.getBuque());
        recogida.setMatricula(request.getMatricula());
        recogida.setIdBarco(request.getIdBarco());
        recogida.setEstado(ControlRecogida.Estado.ENTREGADO);
        recogida.setUsuario(usuario);
        recogida.setFechaHora(LocalDateTime.now());
        
        ControlRecogida savedRecogida = recogidaRepository.save(recogida);
        log.info("Recogida creada: ID {}", savedRecogida.getId());
        
        return convertToDTO(savedRecogida);
    }
    
    /**
     * Obtener recogida por ID
     */
    public RecogidaDTO obtenerPorId(Integer id) {
        ControlRecogida recogida = recogidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recogida no encontrada"));
        return convertToDTO(recogida);
    }
    
    /**
     * Obtener recogida por VIN
     */
    public RecogidaDTO obtenerPorVin(String vin) {
        ControlRecogida recogida = recogidaRepository.findByVin(vin.toUpperCase())
                .orElseThrow(() -> new RuntimeException("VIN no encontrado"));
        return convertToDTO(recogida);
    }
    
    /**
     * Listar todas las recogidas
     */
    public List<RecogidaDTO> listarTodas() {
        return recogidaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Listar recogidas paginadas
     */
    public Page<RecogidaDTO> listarPaginadas(Pageable pageable) {
        return recogidaRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Buscar recogidas por múltiples criterios
     */
    public List<RecogidaDTO> buscar(String vin, String empresa, String marca, String modelo, String estado) {
        ControlRecogida.Estado estadoEnum = null;
        if (estado != null && !estado.isEmpty()) {
            try {
                estadoEnum = ControlRecogida.Estado.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Estado inválido: {}", estado);
            }
        }
        
        return recogidaRepository.findByMultipleCriteria(
                vin != null ? vin.toUpperCase() : null,
                empresa,
                marca,
                modelo,
                estadoEnum
        ).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    /**
     * Lista recogidas por empresa
     */
    public List<RecogidaDTO> listarPorEmpresa(String empresa) {
        return recogidaRepository.findByEmpresa(empresa).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista recogidas por usuario
     */
    public List<RecogidaDTO> listarPorUsuario(Long usuarioId) {
        return recogidaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista recogidas por estado
     */
    public List<RecogidaDTO> listarPorEstado(String estado) {
        ControlRecogida.Estado estadoEnum = ControlRecogida.Estado.valueOf(estado.toUpperCase());
        return recogidaRepository.findByEstado(estadoEnum).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista recogidas de hoy
     */
    public List<RecogidaDTO> listarDeHoy() {
        LocalDateTime inicioHoy = LocalDateTime.of(LocalDate.now(), java.time.LocalTime.MIN);
        LocalDateTime finHoy = LocalDateTime.of(LocalDate.now(), java.time.LocalTime.MAX);
        
        return recogidaRepository.findByFechaHoraBetween(inicioHoy, finHoy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista recogidas por rango de fechas
     */
    public List<RecogidaDTO> listarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return recogidaRepository.findByFechaHoraBetween(inicio, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar recogida
     */
    public RecogidaDTO actualizar(Integer id, RecogidaRequest request, Long usuarioId) {
        log.info("Actualizando recogida: ID {}", id);
        
        ControlRecogida recogida = recogidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recogida no encontrada"));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        recogida.setFicha(request.getFicha());
        recogida.setEmpresa(request.getEmpresa());
        recogida.setMarca(request.getMarca());
        recogida.setModelo(request.getModelo());
        recogida.setBuque(request.getBuque());
        recogida.setMatricula(request.getMatricula());
        recogida.setIdBarco(request.getIdBarco());
        recogida.setUsuario(usuario);
        
        ControlRecogida updatedRecogida = recogidaRepository.save(recogida);
        log.info("Recogida actualizada: ID {}", id);
        
        return convertToDTO(updatedRecogida);
    }
    
    /**
     * Cambiar estado de recogida
     */
    public RecogidaDTO cambiarEstado(Integer id, String nuevoEstado) {
        log.info("Cambiando estado de recogida ID: {} a {}", id, nuevoEstado);
        
        ControlRecogida recogida = recogidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recogida no encontrada"));
        
        ControlRecogida.Estado estado = ControlRecogida.Estado.valueOf(nuevoEstado.toUpperCase());
        recogida.setEstado(estado);
        
        ControlRecogida updated = recogidaRepository.save(recogida);
        log.info("Estado actualizado para recogida ID {}: {}", id, estado);
        
        return convertToDTO(updated);
    }
    
    /**
     * Cambiar empresa de recogida
     */
    public RecogidaDTO cambiarEmpresa(Integer id, String nuevaEmpresa) {
        log.info("Cambiando empresa de recogida ID: {} a {}", id, nuevaEmpresa);
        
        ControlRecogida recogida = recogidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recogida no encontrada"));
        
        recogida.setEmpresa(nuevaEmpresa);
        ControlRecogida updated = recogidaRepository.save(recogida);
        
        return convertToDTO(updated);
    }
    
    /**
     * Eliminar recogida (soft delete)
     */
    public void eliminar(Integer id) {
        log.info("Eliminando recogida: ID {}", id);
        recogidaRepository.deleteById(id);
    }
    
    /**
     * Contar total de recogidas
     */
    public Long contarTodas() {
        return recogidaRepository.countTotal();
    }
    
    /**
     * Contar recogidas de hoy
     */
    public Long contarHoy() {
        return recogidaRepository.countRegistrosHoy();
    }
    
    /**
     * Contar recogidas por estado
     */
    public Long contarPorEstado(String estado) {
        ControlRecogida.Estado estadoEnum = ControlRecogida.Estado.valueOf(estado.toUpperCase());
        return recogidaRepository.countByEstado(estadoEnum);
    }
    
    /**
     * Obtener estadísticas rápidas
     */
    public RecogidaStatsDTO obtenerEstadisticas() {
        Long total = contarTodas();
        Long hoy = contarHoy();
        Long entregados = contarPorEstado("ENTREGADO");
        Long devueltos = contarPorEstado("DEVUELTO");
        
        return new RecogidaStatsDTO(total, hoy, entregados, devueltos);
    }
    
    /**
     * Convertir entidad a DTO
     */
    private RecogidaDTO convertToDTO(ControlRecogida recogida) {
        return new RecogidaDTO(
                recogida.getId(),
                recogida.getFicha(),
                recogida.getEmpresa(),
                recogida.getVin(),
                recogida.getMarca(),
                recogida.getModelo(),
                recogida.getBuque(),
                recogida.getMatricula(),
                recogida.getFechaHora(),
                recogida.getIdBarco(),
                recogida.getEstado().name(),
                recogida.getUsuario() != null ? recogida.getUsuario().getEmail() : null
        );
    }
}
 
/**
 * DTO para estadísticas rápidas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class RecogidaStatsDTO {
    private Long totalRecogidas;
    private Long recogidasHoy;
    private Long entregados;
    private Long devueltos;
}