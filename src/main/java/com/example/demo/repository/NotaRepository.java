package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Nota;
import com.example.demo.models.Usuario;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByOwnerId(Long ownerId);    
    
    @Query("SELECT n FROM Nota n WHERE n.owner.id = :userId AND (LOWER(n.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(n.contenido) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Nota> findByOwnerIdAndTituloOrContenido(Long userId, String searchTerm);
    
}