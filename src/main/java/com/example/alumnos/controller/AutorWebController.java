package com.example.alumnos.controller;

// ========== AI-AÑADIDO: controlador web CRUD de autores ==========

import com.example.alumnos.entity.Autor;
import com.example.alumnos.entity.Editorial;
import com.example.alumnos.repository.AutorRepository;
import com.example.alumnos.repository.EditorialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/autores")
public class AutorWebController {

    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;

    public AutorWebController(AutorRepository autorRepository,
                              EditorialRepository editorialRepository) {
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());
        return "autores";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam String nombre,
                        @RequestParam(required = false) String nacionalidad,
                        @RequestParam(required = false) Long editorialId) {

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setNacionalidad(nacionalidad);

        if (editorialId != null) {
            Editorial editorial = editorialRepository.findById(editorialId).orElse(null);
            autor.setEditorial(editorial);
        }

        autorRepository.save(autor);
        return "redirect:/autores";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Autor autor = autorRepository.findById(id).orElse(null);
        if (autor == null) {
            return "redirect:/autores";
        }
        model.addAttribute("autor", autor);
        model.addAttribute("editoriales", editorialRepository.findAll());
        return "autor-editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String nombre,
                             @RequestParam(required = false) String nacionalidad,
                             @RequestParam(required = false) Long editorialId) {

        Autor autor = autorRepository.findById(id).orElse(null);
        if (autor == null) {
            return "redirect:/autores";
        }

        autor.setNombre(nombre);
        autor.setNacionalidad(nacionalidad);

        if (editorialId != null) {
            autor.setEditorial(editorialRepository.findById(editorialId).orElse(null));
        } else {
            autor.setEditorial(null);
        }

        autorRepository.save(autor);
        return "redirect:/autores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        autorRepository.deleteById(id);
        return "redirect:/autores";
    }
}
