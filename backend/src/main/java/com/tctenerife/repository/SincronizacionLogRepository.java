package com.tctenerife.repository;
 
import com.tctenerife.entity.SincronizacionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import java.time.LocalDateTime;
import java.util.List;
 
/**
 * Repositorio para la entidad SincronizacionLog
 */
@Repository
public interface SincronizacionLogRepository extends JpaRepository<SincronizacionLog, Long> {
    
    // Buscar logs por usuario
    List<SincronizacionLog> findByUsuarioId(Long usuarioId);
    
    // Buscar logs por recogida
    List<SincronizacionLog> findByRecogidaId(Integer recogidaId);
    
    // Buscar logs sin sincronizar
    List<SincronizacionLog> findBySincronizadoFalse();
    
    // Buscar logs por rango de fechas
    @Query("SELECT sl FROM SincronizacionLog sl WHERE sl.fechaSincronizacion BETWEEN :inicio AND :fin")
    List<SincronizacionLog> findByFechaRango(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
    
    // Contar sincronizaciones exitosas
    @Query("SELECT COUNT(sl) FROM SincronizacionLog sl WHERE sl.resultado = 'OK'")
    Long countSincronizacionesExitosas();
    
    // Contar sincronizaciones con error
    @Query("SELECT COUNT(sl) FROM SincronizacionLog sl WHERE sl.resultado = 'ERROR'")
    Long countSincronizacionesError();
}