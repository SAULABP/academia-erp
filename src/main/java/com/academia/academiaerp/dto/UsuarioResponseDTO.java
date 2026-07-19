package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private Rol rol;
    private Boolean activo;

    public UsuarioResponseDTO(Long id, String username, Rol rol, Boolean activo) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.activo = activo;
    }
}