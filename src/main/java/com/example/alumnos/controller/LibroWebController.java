package com.example.alumnos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.alumnos.service.CloudinaryService;
import com.example.alumnos.entity.Autor;
import com.example.alumnos.entity.Editorial;
import com.example.alumnos.entity.Libro;
import com.example.alumnos.repository.AutorRepository;
import com.example.alumnos.repository.EditorialRepository;
import com.example.alumnos.repository.LibroRepository;

@Controller
@RequestMapping("/libros")
public class LibroWebController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final EditorialRepository editorialRepository;
    private final CloudinaryService cloudinaryService;

    public LibroWebController(LibroRepository libroRepository,
                              AutorRepository autorRepository,
                              EditorialRepository editorialRepository,
                              CloudinaryService cloudinaryService) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.editorialRepository = editorialRepository;
        this.cloudinaryService = cloudinaryService;
    }


    // ABRIR PÁGINA LIBROS
    @GetMapping()
    public String verLibros(Model model) {

        model.addAttribute("libros", libroRepository.findAllByOrderByTituloAsc());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());

        return "libros";
    }

    // CREAR LIBRO
    @PostMapping("/crear")
    public String crearLibro(@RequestParam String titulo,
                             @RequestParam Long autorId,
                             @RequestParam Long editorialId,
                             @RequestParam String isbn,
                             @RequestParam(required = false) Integer paginas,
                             @RequestParam("imagen") MultipartFile imagen,
                             RedirectAttributes redirectAttributes){

        Autor autor = autorRepository.findById(autorId).orElse(null);
        Editorial editorial = editorialRepository.findById(editorialId).orElse(null);

        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setIsbn(isbn);
        libro.setPaginas(paginas);

        if (libroRepository.existsByIsbn(isbn)) {
            redirectAttributes.addFlashAttribute("error", "Ya existe un libro con ese ISBN");
            return "redirect:/libros";
        }

        try {

            if (!imagen.isEmpty()) {

                String urlImagen = cloudinaryService.subirImagen(imagen, "libros");

                libro.setImagenUrl(urlImagen);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        libroRepository.save(libro);

        return "redirect:/libros";
    }


    // editar y eliminar libros

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
                                  @RequestParam("imagen") MultipartFile imagen,
                                  @RequestParam(required = false) String isbn,
                                  @RequestParam(required = false) Integer paginas) {

        Libro libro = libroRepository.findById(id).orElse(null);
        if (libro == null) {
            return "redirect:/libros";
        }

        Autor autor = autorRepository.findById(autorId).orElse(null);
        Editorial editorial = editorialRepository.findById(editorialId).orElse(null);

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        try {

            if (!imagen.isEmpty()) {

                String urlImagen = cloudinaryService.subirImagen(imagen, "libros");

                libro.setImagenUrl(urlImagen);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
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

    @GetMapping("/autor/{id}")
    public String librosPorAutor(@PathVariable Long id, Model model) {

        List<Libro> librosFiltrados = libroRepository.findAll()
                .stream()
                .filter(libro ->
                        libro.getAutor() != null &&
                                libro.getAutor().getId().equals(id))
                .toList();

        model.addAttribute("libros", librosFiltrados);

        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());

        return "libros";
    }

    @GetMapping("/editorial/{id}")
    public String filtrarPorEditorial(@PathVariable Long id, Model model) {

        model.addAttribute("libros",
                libroRepository.findByEditorialId(id));

        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());

        return "libros";
    }

    @GetMapping("/buscar")
    public String buscarPorTitulo(@RequestParam String titulo, Model model) {

        model.addAttribute("libros",
                libroRepository.findByTituloContainingIgnoreCase(titulo));

        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("editoriales", editorialRepository.findAll());

        return "libros";
    }
}