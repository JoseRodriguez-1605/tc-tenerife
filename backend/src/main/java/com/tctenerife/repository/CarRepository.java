package com.tctenerife.repository;

import com.tctenerife.entity.Car;
import com.tctenerife.entity.User; import com.tctenerife.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Repositorio para la entidad Car
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    // Buscar por VIN (número de chasis)
    Optional<Car> findByVin(String vin);
    
    // Buscar por empresa
    List<Car> findByCompany(String company);
    
    // Buscar por usuario que lo creó
    List<Car> findByCreatedByUserId(Long userId);
    
    // Contar coches registrados hoy
    @Query("SELECT COUNT(c) FROM Car c WHERE DATE(c.createdAt) = CURDATE()")
    Long countCarsToday();
    
    // Contar coches totales
    @Query("SELECT COUNT(c) FROM Car c")
    Long countTotalCars();
    
    // Coches no sincronizados
    List<Car> findByIsSyncedFalse();
    
    // Buscar por rango de fechas
    @Query("SELECT c FROM Car c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Car> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                              @Param("endDate") LocalDateTime endDate);
    
    // Buscar por múltiples criterios
    @Query("SELECT c FROM Car c WHERE " +
           "(:vin IS NULL OR c.vin LIKE %:vin%) AND " +
           "(:make IS NULL OR c.make LIKE %:make%) AND " +
           "(:model IS NULL OR c.model LIKE %:model%) AND " +
           "(:company IS NULL OR c.company LIKE %:company%)")
    List<Car> findByMultipleCriteria(
            @Param("vin") String vin,
            @Param("make") String make,
            @Param("model") String model,
            @Param("company") String company
    );
}