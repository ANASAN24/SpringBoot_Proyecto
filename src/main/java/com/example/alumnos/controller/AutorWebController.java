package com.example.alumnos.controller;

// controlador web CRUD de autores

import com.example.alumnos.entity.Autor;
import com.example.alumnos.entity.Editorial;
import com.example.alumnos.repository.AutorRepository;
import com.example.alumnos.repository.EditorialRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.example.alumnos.repository.LibroRepository;

@Controller
@RequestMapping("/autores")
public class AutorWebController {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;

    public AutorWebController(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", autorRepository.findAll());
        return "autores";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam String nombre,
                        @RequestParam(required = false) String nacionalidad,
                        @RequestParam(required = false) Long editorialId) {

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setNacionalidad(nacionalidad);

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

        autorRepository.save(autor);
        return "redirect:/autores";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAutor(@PathVariable Long id, Model model) {

        boolean tieneLibros = !libroRepository.findByAutorId(id).isEmpty();

        if (tieneLibros) {
            model.addAttribute("error",
                    "No se puede eliminar este autor porque tiene libros asignados en el sistema.");
            return "error";
        }

        autorRepository.deleteById(id);
        return "redirect:/autores";
    }
}
