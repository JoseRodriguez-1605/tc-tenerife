package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Datos enviados desde el dispositivo para sincronizar coches offline con el servidor

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncRequest {
    private List<CarSyncData> carsToSync;
    private Long lastSyncTimestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarSyncData {
        private Long id;
        private String vin;
        private String make;
        private String model;
        private String company;
        private String action;
        private Long deviceTimestamp;
        private Boolean isConflict;
    }
}
