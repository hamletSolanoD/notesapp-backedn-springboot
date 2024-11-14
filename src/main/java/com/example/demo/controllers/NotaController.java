package com.example.demo.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Nota;
import com.example.demo.models.Usuario;
import com.example.demo.repository.NotaRepository;
import com.example.demo.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para obtener las notas de un usuario
    @GetMapping("/{userId}")
    public ResponseEntity<List<Nota>> getNotasByUser(@PathVariable Long userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }        System.out.println("usuario" + usuario);

        List<Nota> notas = notaRepository.findByOwnerId(usuario.getId());
        System.out.println("Notas" + notas);
        for (Nota nota : notas) {
            System.out.println(nota.getId() + " - " + nota.getTitulo());
        }
        return ResponseEntity.ok(notas);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Nota>> searchNotas(@RequestParam Long userId, @RequestParam String keyword) {
        List<Nota> notas = notaRepository.findByOwnerIdAndTituloOrContenido(userId, keyword);
        return ResponseEntity.ok(notas);
    }
    

      @PostMapping("/create")
    public ResponseEntity<Nota> createNota(@RequestParam Long userId, @RequestBody Nota notaRequest) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }
        notaRequest.setOwner(usuario);
        notaRequest.setCreatedAt(new Date());
        notaRequest.setUpdatedAt(new Date());
        Nota nuevaNota = notaRepository.save(notaRequest);
        return ResponseEntity.ok(nuevaNota);
    }

    // Endpoint para editar una nota existente
    @PutMapping("/edit/{notaId}")
    public ResponseEntity<Nota> editNota(@PathVariable Long notaId, @RequestBody Nota notaDetails) {
        Optional<Nota> optionalNota = notaRepository.findById(notaId);
        if (!optionalNota.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Nota nota = optionalNota.get();
        if (notaDetails.getTitulo() != null) {
            nota.setTitulo(notaDetails.getTitulo());
        }
        if (notaDetails.getContenido() != null) {
            nota.setContenido(notaDetails.getContenido());
        }
        nota.setUpdatedAt(new Date());
        Nota notaActualizada = notaRepository.save(nota);
        return ResponseEntity.ok(notaActualizada);
    }

       // Endpoint para eliminar una nota
    @DeleteMapping("/delete/{notaId}")
    public ResponseEntity<Void> deleteNota(@PathVariable Long notaId) {
        Optional<Nota> optionalNota = notaRepository.findById(notaId);
        if (!optionalNota.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        notaRepository.delete(optionalNota.get());
        return ResponseEntity.noContent().build();
    }
    
}