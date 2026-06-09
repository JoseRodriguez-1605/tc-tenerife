package com.tctenerife.repository;

import com.tctenerife.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Repositorio para la entidad SyncLog
 */
@Repository
public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {
    
    // Buscar logs de sincronización por usuario
    List<SyncLog> findByUserId(Long userId);
    
    // Buscar logs de sincronización por coche
    List<SyncLog> findByCarId(Long carId);
    
    // Buscar logs no sincronizados
    List<SyncLog> findBySyncedFalse();
    
    // Buscar logs de sincronización por rango de fechas
    @Query("SELECT s FROM SyncLog s WHERE s.syncTimestamp BETWEEN :startDate AND :endDate")
    List<SyncLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    // Buscar logs con conflictos
    @Query("SELECT s FROM SyncLog s WHERE s.syncResult = 'CONFLICT'")
    List<SyncLog> findConflicts();
    
    // Contar sincronizaciones exitosas
    @Query("SELECT COUNT(s) FROM SyncLog s WHERE s.syncResult = 'OK'")
    Long countSuccessfulSyncs();
}