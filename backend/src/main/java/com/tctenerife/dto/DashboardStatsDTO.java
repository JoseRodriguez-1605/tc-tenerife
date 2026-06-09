package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Estadísticas para el panel de administración (coches, usuarios, actividad)

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalCars;
    private Long carsToday;
    private Long totalUsers;
    private Long activeUsers;
    private List<CarsByCompanyDTO> carsByCompany;
    private List<CarsByUserDTO> carsByUser;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarsByCompanyDTO {
        private String company;
        private Long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarsByUserDTO {
        private String userEmail;
        private Long count;
    }
}
