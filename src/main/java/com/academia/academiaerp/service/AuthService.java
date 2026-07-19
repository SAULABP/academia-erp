package com.academia.academiaerp.service;

import com.academia.academiaerp.dto.AuthResponseDTO;
import com.academia.academiaerp.dto.LoginRequestDTO;
import com.academia.academiaerp.dto.RegistroRequestDTO;
import com.academia.academiaerp.dto.UsuarioResponseDTO;
import com.academia.academiaerp.exception.RecursoNoEncontradoException;
import com.academia.academiaerp.exception.ReglaNegocioException;
import com.academia.academiaerp.model.Usuario;
import com.academia.academiaerp.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioDetailsService usuarioDetailsService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UsuarioDetailsService usuarioDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    // ---- Registrar un nuevo usuario ----
    public AuthResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new ReglaNegocioException("El username ya está en uso: " + dto.getUsername());
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generarToken(userDetails);

        return new AuthResponseDTO(token, usuario.getUsername(), usuario.getRol());
    }

    // ---- Login ----
    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ReglaNegocioException("Usuario no encontrado"));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generarToken(userDetails);

        return new AuthResponseDTO(token, usuario.getUsername(), usuario.getRol());
    }
    // Listar todos los usuarios
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getUsername(), u.getRol(), u.getActivo()))
                .toList();
    }

    // Activar/desactivar un usuario
    public void cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }
}