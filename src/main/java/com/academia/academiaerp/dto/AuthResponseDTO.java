package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String token;
    private String username;
    private Rol rol;

    public AuthResponseDTO(String token, String username, Rol rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }
}