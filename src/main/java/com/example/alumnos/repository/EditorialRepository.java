package com.example.alumnos.repository;

import com.example.alumnos.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource(path = "editoriales", collectionResourceRel = "editoriales")
public interface EditorialRepository extends JpaRepository<Editorial, Long> {

    @RestResource(path = "por-nombre", rel = "por-nombre")
    Optional<Editorial> findByNombre(@Param("nombre") String nombre);
}