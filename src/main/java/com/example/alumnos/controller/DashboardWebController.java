package com.example.alumnos.controller;

import com.example.alumnos.repository.AutorRepository;
import com.example.alumnos.repository.LibroRepository;
import com.example.alumnos.repository.EditorialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class DashboardWebController {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;
    private final EditorialRepository editorialRepository;

    public DashboardWebController(AutorRepository autorRepository,
                                  LibroRepository libroRepository,
                                  EditorialRepository editorialRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
        this.editorialRepository = editorialRepository;
    }

    @GetMapping()
    public String verDashboard(Model model) {

        model.addAttribute("totalAutores", autorRepository.count());
        model.addAttribute("totalLibros", libroRepository.count());
        model.addAttribute("totalEditoriales", editorialRepository.count());

        return "dashboard";
    }
}