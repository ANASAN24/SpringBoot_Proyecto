package com.example.alumnos.controller;

// ========== AI-AÑADIDO: controlador web CRUD de editoriales ==========

import com.example.alumnos.entity.Editorial;
import com.example.alumnos.repository.EditorialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/editoriales")
public class EditorialWebController {

    private final EditorialRepository editorialRepository;

    public EditorialWebController(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("editoriales", editorialRepository.findAll());
        return "editoriales";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam String nombre,
                        @RequestParam(required = false) String pais) {

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setPais(pais);
        editorialRepository.save(editorial);
        return "redirect:/editoriales";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Editorial editorial = editorialRepository.findById(id).orElse(null);
        if (editorial == null) {
            return "redirect:/editoriales";
        }
        model.addAttribute("editorial", editorial);
        return "editorial-editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam String nombre,
                             @RequestParam(required = false) String pais) {

        Editorial editorial = editorialRepository.findById(id).orElse(null);
        if (editorial == null) {
            return "redirect:/editoriales";
        }

        editorial.setNombre(nombre);
        editorial.setPais(pais);
        editorialRepository.save(editorial);
        return "redirect:/editoriales";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        editorialRepository.deleteById(id);
        return "redirect:/editoriales";
    }
}
