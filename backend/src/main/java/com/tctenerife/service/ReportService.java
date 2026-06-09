package com.tctenerife.service;

import com.tctenerife.dto.*;
import com.tctenerife.entity.Car;
import com.tctenerife.entity.User;
import com.tctenerife.repository.CarRepository;
import com.tctenerife.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.*;
import java.util.stream.Collectors;
/**
 * Servicio de reportes y estadísticas
 */
@Service
@Slf4j
public class ReportService {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Obtener estadísticas del dashboard
     */
    public DashboardStatsDTO getDashboardStats() {
        Long totalCars = carRepository.count();
        Long carsToday = carRepository.countCarsToday();
        Long totalUsers = userRepository.count();
        Long activeUsers = userRepository.countActiveUsers();
        
        // Coches por empresa
        List<DashboardStatsDTO.CarsByCompanyDTO> carsByCompany = carRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Car::getCompany, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new DashboardStatsDTO.CarsByCompanyDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        
        // Coches por usuario
        List<User> users = userRepository.findAll();
        List<DashboardStatsDTO.CarsByUserDTO> carsByUser = users.stream()
            .map(u -> new DashboardStatsDTO.CarsByUserDTO(
             u.getEmail(),
            Long.valueOf(carRepository.findByCreatedByUserId(u.getId()).size())
))

                .filter(c -> c.getCount() > 0)
                .collect(Collectors.toList());
        
        return new DashboardStatsDTO(totalCars, carsToday, totalUsers, activeUsers, 
                                     carsByCompany, carsByUser);
    }
}