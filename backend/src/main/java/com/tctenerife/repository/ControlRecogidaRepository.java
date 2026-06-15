package com.tctenerife.repository;
 
import com.tctenerife.entity.ControlRecogida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
 
/**
 * Repositorio para la entidad ControlRecogida
 */
@Repository
public interface ControlRecogidaRepository extends JpaRepository<ControlRecogida, Integer> {
    
    // Buscar por VIN (número de chasis)
    Optional<ControlRecogida> findByVin(String vin);
    
    // Buscar por empresa
    List<ControlRecogida> findByEmpresa(String empresa);
    

    // Buscar por estado
    List<ControlRecogida> findByEstado(ControlRecogida.Estado estado);
    
    // Contar registros hoy
    @Query("SELECT COUNT(cr) FROM ControlRecogida cr WHERE DATE(cr.fechaHora) = CURDATE()")
    Long countRegistrosHoy();
    
    // Contar total
    @Query("SELECT COUNT(cr) FROM ControlRecogida cr")
    Long countTotal();
    
    // Contar por estado
    @Query("SELECT COUNT(cr) FROM ControlRecogida cr WHERE cr.estado = :estado")
    Long countByEstado(@Param("estado") ControlRecogida.Estado estado);
    
    // Buscar por rango de fechas
    @Query("SELECT cr FROM ControlRecogida cr WHERE cr.fechaHora BETWEEN :inicio AND :fin")
    List<ControlRecogida> findByFechaHoraBetween(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
    
    // Buscar por múltiples criterios
    @Query("SELECT cr FROM ControlRecogida cr WHERE " +
           "(:vin IS NULL OR cr.vin LIKE %:vin%) AND " +
           "(:empresa IS NULL OR cr.empresa LIKE %:empresa%) AND " +
           "(:marca IS NULL OR cr.marca LIKE %:marca%) AND " +
           "(:buque IS NULL OR cr.buque LIKE %:buque%) AND " +
           "(:modelo IS NULL OR cr.modelo LIKE %:modelo%) AND " +
           "(:estado IS NULL OR cr.estado = :estado)")
    List<ControlRecogida> findByMultipleCriteria(
            @Param("vin") String vin,
            @Param("empresa") String empresa,
            @Param("marca") String marca,
            @Param("buque") String buque,
            @Param("modelo") String modelo,
            @Param("estado") ControlRecogida.Estado estado
    );
    
    // Obtener estadísticas por empresa
    @Query("SELECT cr.empresa, COUNT(cr) FROM ControlRecogida cr GROUP BY cr.empresa ORDER BY COUNT(cr) DESC")
    List<Object[]> getEstadisticasPorEmpresa();
    
    // Obtener estadísticas por usuario
    @Query("SELECT cr.usuario.email, COUNT(cr) FROM ControlRecogida cr WHERE cr.usuario IS NOT NULL GROUP BY cr.usuario.id ORDER BY COUNT(cr) DESC")
    List<Object[]> getEstadisticasPorUsuario();
}