package com.tctenerife.service;
import com.tctenerife.dto.*;
import com.tctenerife.entity.Car;
import com.tctenerife.entity.User;
//import com.tctenerife.entity.SyncLog;
import com.tctenerife.repository.CarRepository;
import com.tctenerife.repository.UserRepository;
import com.tctenerife.repository.SyncLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de coches
 */
@Service
@Slf4j
@Transactional
public class CarService {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SyncLogRepository syncLogRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Crear nuevo coche
     */
    public CarDTO createCar(CarRequest request, Long userId) {
        // Validar que el VIN no exista
        if (carRepository.findByVin(request.getVin()).isPresent()) {
            throw new RuntimeException("El chasis (VIN) ya existe en el sistema");
        }
        
        Car car = new Car();
        car.setVin(request.getVin());
        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setCompany(request.getCompany());
        car.setCreatedByUserId(userId);
        car.setIsSynced(true);
        car.setLastSyncAttempt(LocalDateTime.now());
        
        Car savedCar = carRepository.save(car);
        log.info("Coche creado: {} por usuario {}", request.getVin(), userId);
        
        return convertToCarDTO(savedCar);
    }
    
    /**
     * Obtener coche por ID
     */
    public CarDTO getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coche no encontrado"));
        return convertToCarDTO(car);
    }
    
    /**
     * Obtener coche por VIN
     */
    public CarDTO getCarByVin(String vin) {
        Car car = carRepository.findByVin(vin)
                .orElseThrow(() -> new RuntimeException("Coche no encontrado"));
        return convertToCarDTO(car);
    }
    
    /**
     * Listar todos los coches
     */
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Buscar coches por criterios múltiples
     */
    public List<CarDTO> searchCars(String vin, String make, String model, String company) {
        return carRepository.findByMultipleCriteria(vin, make, model, company).stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar coche
     */
    public CarDTO updateCar(Long id, CarRequest request, Long userId) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coche no encontrado"));
        
        // Si cambió el VIN, validar que el nuevo no exista
        if (!car.getVin().equals(request.getVin()) && 
            carRepository.findByVin(request.getVin()).isPresent()) {
            throw new RuntimeException("El nuevo chasis (VIN) ya existe");
        }
        
        car.setVin(request.getVin());
        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setCompany(request.getCompany());
        car.setUpdatedByUserId(userId);
        car.setIsSynced(true);
        
        Car updatedCar = carRepository.save(car);
        log.info("Coche actualizado: {}", id);
        
        return convertToCarDTO(updatedCar);
    }
    
    /**
     * Eliminar coche
     */
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coche no encontrado"));
        carRepository.delete(car);
        log.info("Coche eliminado: {}", id);
    }
    
    /**
     * Convertir Car a CarDTO
     */
    public CarDTO convertToCarDTO(Car car) {
        String createdByEmail = null;
        String updatedByEmail = null;
        
        if (car.getCreatedByUserId() != null) {
            Optional<User> createdByUser = userRepository.findById(car.getCreatedByUserId());
            createdByEmail = createdByUser.map(User::getEmail).orElse("Sistema");
        }
        
        if (car.getUpdatedByUserId() != null) {
            Optional<User> updatedByUser = userRepository.findById(car.getUpdatedByUserId());
            updatedByEmail = updatedByUser.map(User::getEmail).orElse("Sistema");
        }
        //Cambiar lo que pide el coche.
        return new CarDTO(
                car.getId(),
                car.getVin(),
                car.getMake(),
                car.getModel(),
                car.getCompany(),
                car.getCreatedAt(),
                car.getUpdatedAt(),
                createdByEmail,
                updatedByEmail,
                car.getIsSynced()
        );
    }
}
