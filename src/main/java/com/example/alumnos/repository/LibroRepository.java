package com.example.alumnos.repository;

import com.example.alumnos.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;
import java.util.List;

@RepositoryRestResource(path = "libros", collectionResourceRel = "libros")
public interface LibroRepository extends JpaRepository<Libro, Long> {

    @RestResource(path = "por-titulo", rel = "por-titulo")
    Optional<Libro> findByTitulo(@Param("titulo") String titulo);

    @RestResource(path = "por-isbn", rel = "por-isbn")
    Optional<Libro> findByIsbn(@Param("isbn") String isbn);

    @RestResource(path = "por-autor", rel = "por-autor")
    List<Libro> findByAutorId(@Param("id") Long id);

    @RestResource(path = "por-editorial", rel = "por-editorial")
    List<Libro> findByEditorialId(@Param("id") Long id);

}