package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
// Respuesta del servidor al hacer login: token + usuario + coches iniciales

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDTO user;
    private List<CarDTO> allCars;
}
