package com.tctenerife.service;


import com.tctenerife.dto.*;
import com.tctenerife.entity.Car;
import com.tctenerife.entity.User;
import com.tctenerife.entity.SyncLog;
import com.tctenerife.repository.CarRepository;
import com.tctenerife.repository.UserRepository;
import com.tctenerife.repository.SyncLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
public class SyncService {
    
/**
 * Servicio de sincronización offline
 */
@Service

@Transactional
public class SyncService {


@Autowired
    private CarRepository carRepository;
    
    @Autowired
    private SyncLogRepository syncLogRepository;
    
    @Autowired
    private CarService carService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Procesar sincronización de datos offline
     */
    public SyncResponse syncData(SyncRequest request, Long userId) {
        log.info("Iniciando sincronización para usuario: {}", userId);
        
        List<CarDTO> syncedCars = new ArrayList<>();
        List<SyncResponse.SyncConflict> conflicts = new ArrayList<>();
        
        for (SyncRequest.CarSyncData carData : request.getCarsToSync()) {
            try {
                if ("CREATE".equals(carData.getAction())) {
                    syncCreateCar(carData, userId, syncedCars);
                } else if ("UPDATE".equals(carData.getAction())) {
                    syncUpdateCar(carData, userId, syncedCars, conflicts);
                }
            } catch (Exception e) {
                log.error("Error sincronizando coche {}: {}", carData.getVin(), e.getMessage());
                recordSyncError(carData, userId, e.getMessage());
            }
        }
        
        return new SyncResponse(
                true,
                syncedCars,
                conflicts,
                LocalDateTime.now(),
                "Sincronización completada"
        );
    }
    
    /**
     * Sincronizar creación de coche
     */
    private void syncCreateCar(SyncRequest.CarSyncData carData, Long userId, 
                              List<CarDTO> syncedCars) {
        // Verificar si ya existe
        Optional<Car> existing = carRepository.findByVin(carData.getVin());
        
        if (existing.isPresent()) {
            throw new RuntimeException("El VIN ya existe en el servidor");
        }
        
        Car car = new Car();
        car.setVin(carData.getVin());
        car.setMake(carData.getMake());
        car.setModel(carData.getModel());
        car.setCompany(carData.getCompany());
        car.setCreatedByUserId(userId);
        car.setIsSynced(true);
        car.setLastSyncAttempt(LocalDateTime.now());
        
        Car savedCar = carRepository.save(car);
        recordSync(carData, savedCar.getId(), userId, "CREATE", "OK", null);
        syncedCars.add(carService.convertToCarDTO(savedCar));
    }
    
    /**
     * Sincronizar actualización de coche
     */
    private void syncUpdateCar(SyncRequest.CarSyncData carData, Long userId, 
                              List<CarDTO> syncedCars, 
                              List<SyncResponse.SyncConflict> conflicts) {
        Optional<Car> existing = carRepository.findByVin(carData.getVin());
        
        if (existing.isEmpty()) {
            throw new RuntimeException("El VIN no existe en el servidor");
        }
        
        Car car = existing.get();
        car.setMake(carData.getMake());
        car.setModel(carData.getModel());
        car.setCompany(carData.getCompany());
        car.setUpdatedByUserId(userId);
        car.setIsSynced(true);
        
        Car updatedCar = carRepository.save(car);
        recordSync(carData, car.getId(), userId, "UPDATE", "OK", null);
        syncedCars.add(carService.convertToCarDTO(updatedCar));
    }
    
    /**
     * Obtener datos iniciales para descarga
     */
    public List<CarDTO> getInitialData(Long lastSyncTimestamp) {
        log.info("Obteniendo datos iniciales desde timestamp: {}", lastSyncTimestamp);
        return carRepository.findAll().stream()
                .map(carService::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Registrar operación de sincronización
     */
    private void recordSync(SyncRequest.CarSyncData carData, Long carId, Long userId, 
                           String action, String result, String error) {
        try {
            SyncLog log = new SyncLog();
            log.setUserId(userId);
            log.setCarId(carId);
            log.setAction(SyncLog.SyncAction.valueOf(action));
            log.setCarData(objectMapper.writeValueAsString(carData));
            log.setSynced(true);
            log.setSyncResult(result);
            log.setErrorMessage(error);
            log.setDeviceTimestamp(new Date(carData.getDeviceTimestamp()).toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            
            syncLogRepository.save(log);
        } catch (Exception e) {
            log.error("Error registrando sincronización: {}", e.getMessage());
        }
    }
    
    /**
     * Registrar error de sincronización
     */
    private void recordSyncError(SyncRequest.CarSyncData carData, Long userId, String error) {
        try {
            SyncLog log = new SyncLog();
            log.setUserId(userId);
            log.setCarId(0L); // No hay ID porque no se creó
            log.setAction(SyncLog.SyncAction.valueOf(carData.getAction()));
            log.setCarData(objectMapper.writeValueAsString(carData));
            log.setSynced(false);
            log.setSyncResult("ERROR");
            log.setErrorMessage(error);
            
            syncLogRepository.save(log);
        } catch (Exception e) {
            log.error("Error registrando error de sincronización: {}", e.getMessage());
        }
    }
}

}




    

    
    