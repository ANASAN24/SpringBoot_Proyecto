package com.example.alumnos.controller;


// controlador web CRUD de editoriales

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.alumnos.entity.Editorial;
import com.example.alumnos.repository.EditorialRepository;
import com.example.alumnos.repository.LibroRepository;

@Controller
@RequestMapping("/editoriales")
public class EditorialWebController {

    private final EditorialRepository editorialRepository;
    private final LibroRepository libroRepository;

    public EditorialWebController(EditorialRepository editorialRepository, LibroRepository libroRepository) {
        this.editorialRepository = editorialRepository;
        this.libroRepository = libroRepository;
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
    public String eliminarEditorial(@PathVariable Long id, Model model) {

        boolean tieneLibros = !libroRepository.findByEditorialId(id).isEmpty();

        if (tieneLibros) {
            model.addAttribute("error",
                    "No se puede eliminar esta editorial porque tiene libros asociados.");
            return "error";
        }

        editorialRepository.deleteById(id);
        return "redirect:/editoriales";
    }
}
