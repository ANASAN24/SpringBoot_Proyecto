package com.example.alumnos.controller;

import com.example.alumnos.repository.AutorRepository;
import com.example.alumnos.repository.LibroRepository;
import com.example.alumnos.repository.EditorialRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DashboardController {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;
    private final EditorialRepository editorialRepository;

    public DashboardController(AutorRepository autorRepository,
                               LibroRepository libroRepository,
                               EditorialRepository editorialRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
        this.editorialRepository = editorialRepository;
    }

    @GetMapping("/api/dashboard/resumen")
    public ResponseEntity<Map<String, Long>> resumen() {

        Map<String, Long> datos = new LinkedHashMap<>();

        datos.put("autores", autorRepository.count());
        datos.put("libros", libroRepository.count());
        datos.put("editoriales", editorialRepository.count());

        return ResponseEntity.ok(datos);
    }

}