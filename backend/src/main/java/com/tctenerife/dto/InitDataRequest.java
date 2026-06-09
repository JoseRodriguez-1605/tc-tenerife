package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Petición para descargar todos los coches desde la última sincronización

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitDataRequest {
    private Long lastSyncTimestamp;
}
