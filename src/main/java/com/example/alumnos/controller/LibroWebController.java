package com.example.alumnos.controller;

import com.example.alumnos.entity.Autor;
import com.example.alumnos.entity.Editorial;
import com.example.alumnos.entity.Libro;
import com.example.alumnos.repository.AutorRepository;
import com.example.alumnos.repository.EditorialRepository;
import com.example.alumnos.repository.LibroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/libros")
public class LibroWebController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;

    public LibroWebController(LibroRepository libroRepository,
                              AutorRepository autorRepository,
                              EditorialRepository editorialRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
    }

    // 🔵 1. ABRIR PÁGINA LIBROS
    @GetMapping()
    public String verLibros(Model model) {

        model.addAttribute("libros", libroRepository.findAll());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());

        return "libros";
    }

    // 🟢 2. CREAR LIBRO
    @PostMapping("/crear")
    public String crearLibro(@RequestParam String titulo,
                             @RequestParam Long autorId,
                             @RequestParam(required = false) Long editorialId,
                             @RequestParam(required = false) String nuevaEditorial,
                             @RequestParam(required = false) String imagenUrl) {

        Autor autor = autorRepository.findById(autorId).orElse(null);

        //Editorial editorial = editorialRepository.findById(editorialId).orElse(null);

        Editorial editorial;

        // SI ESCRIBE NUEVA EDITORIAL
        if(nuevaEditorial != null && !nuevaEditorial.isBlank()) {

            editorial = new Editorial();

            editorial.setNombre(nuevaEditorial);

            editorialRepository.save(editorial);

        } else {

            // SI ELIGE UNA EXISTENTE
            editorial =
                    editorialRepository.findById(editorialId)
                            .orElse(null);
        }

        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setImagenUrl(imagenUrl);

        libroRepository.save(libro);

        return "redirect:/libros";
    }

    // ========== AI-AÑADIDO: editar y eliminar libros ==========

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {

        Libro libro = libroRepository.findById(id).orElse(null);
        if (libro == null) {
            return "redirect:/libros";
        }

        model.addAttribute("libro", libro);
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());

        return "libro-editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarLibro(@PathVariable Long id,
                                  @RequestParam String titulo,
                                  @RequestParam Long autorId,
                                  @RequestParam(required = false) Long editorialId,
                                  @RequestParam(required = false) String nuevaEditorial,
                                  @RequestParam(required = false) String imagenUrl,
                                  @RequestParam(required = false) String isbn,
                                  @RequestParam(required = false) Integer paginas) {

        Libro libro = libroRepository.findById(id).orElse(null);
        if (libro == null) {
            return "redirect:/libros";
        }

        Autor autor = autorRepository.findById(autorId).orElse(null);
        Editorial editorial;

        if (nuevaEditorial != null && !nuevaEditorial.isBlank()) {
            editorial = new Editorial();
            editorial.setNombre(nuevaEditorial);
            editorialRepository.save(editorial);
        } else if (editorialId != null) {
            editorial = editorialRepository.findById(editorialId).orElse(null);
        } else {
            editorial = null;
        }

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setImagenUrl(imagenUrl);
        libro.setIsbn(isbn);
        libro.setPaginas(paginas);

        libroRepository.save(libro);
        return "redirect:/libros";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        libroRepository.deleteById(id);
        return "redirect:/libros";
    }
}