package com.academia.academiaerp.dto;

import com.academia.academiaerp.enums.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroRequestDTO {
    private String username;
    private String password;
    private Rol rol;
}