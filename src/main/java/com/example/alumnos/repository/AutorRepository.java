package com.example.alumnos.repository;

import com.example.alumnos.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "autores", collectionResourceRel = "autores")
public interface AutorRepository extends JpaRepository<Autor, Long> {

    @RestResource(path = "por-nombre", rel = "por-nombre")
    Optional<Autor> findByNombre(@Param("nombre") String nombre);
}