package com.academia.academiaerp.controller;

import com.academia.academiaerp.dto.AuthResponseDTO;
import com.academia.academiaerp.dto.LoginRequestDTO;
import com.academia.academiaerp.dto.RegistroRequestDTO;
import com.academia.academiaerp.dto.UsuarioResponseDTO;
import com.academia.academiaerp.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponseDTO> registrar(@RequestBody RegistroRequestDTO dto) {
        return new ResponseEntity<>(authService.registrar(dto), HttpStatus.CREATED);
    }
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> listarUsuarios() {
        return authService.listarUsuarios();
    }

    @PutMapping("/usuarios/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam boolean activo) {
        authService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }
}