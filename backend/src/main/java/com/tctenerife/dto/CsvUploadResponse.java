package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Resultado del procesamiento de un archivo CSV (filas correctas, fallidas y errores)

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvUploadResponse {
    private Integer totalRows;
    private Integer successfulRows;
    private Integer failedRows;
    private List<CsvErrorDTO> errors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CsvErrorDTO {
        private Integer rowNumber;
        private String vin;
        private String error;
    }
}
