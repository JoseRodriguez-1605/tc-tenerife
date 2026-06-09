package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// Respuesta del servidor después de sincronizar datos offline

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncResponse {
    private Boolean success;
    private List<CarDTO> syncedCars;
    private List<SyncConflict> conflicts;
    private LocalDateTime serverTimestamp;
    private String message;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyncConflict {
        private Long carId;
        private String vin;
        private String message;
        private CarDTO serverVersion;
    }
}
