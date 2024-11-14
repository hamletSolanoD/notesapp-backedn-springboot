// src/main/java/com/tusnotas/notesapp/controller/UsuarioController.java

package com.example.demo.controllers;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Usuario;
import com.example.demo.repository.UsuarioRepository;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

  
    // Endpoint para iniciar sesión con email y contraseña
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Correo no registrado");
        }

        Usuario usuario = usuarioOptional.get();

        if (!usuario.getContrasena().equals(loginRequest.getContrasena())) {
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }

        return ResponseEntity.ok(usuario);
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario nuevoUsuario) {
        // Verificar si el correo ya está registrado
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(nuevoUsuario.getEmail());
        if (usuarioExistente.isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        // Verificar la seguridad de la contraseña (al menos una mayúscula, una minúscula, y un número)
        String contrasena = nuevoUsuario.getContrasena();
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        if (!Pattern.matches(passwordPattern, contrasena) || contrasena.length() < 8) {
            return ResponseEntity.badRequest().body("La contraseña debe tener al menos 8 caracteres, incluir una letra mayúscula, una minúscula y un número");
        }

        // Guardar el nuevo usuario si todo está correcto
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.status(201).body(usuarioGuardado);
    }
}
