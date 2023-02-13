package com.Egg.news.controladores;

import com.Egg.news.entidades.Noticia;
import com.Egg.news.entidades.Periodista;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.servicios.NoticiaServicio;
import com.Egg.news.servicios.PeriodistaServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private PeriodistaServicio periodistaServicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {

        List<Periodista> periodistas = periodistaServicio.ListarPeriodistas();

        modelo.addAttribute("periodistas", periodistas);

        return "noticia_form.html";
    }

    //A traves de un método Post va a viajar la información la cual va a tener un "Action" en 
    //en una url específica, /autor/registro
    @PostMapping("/registro")
    //Para indicar a nuestro controlador que titulo y cuerpo son parámetros que van a viajar
    //en nuestra url lo vamos a marcar como un RequestParam
    public String registro(MultipartFile archivo,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String cuerpo,
            @RequestParam(required = false) Integer idPeriodista, ModelMap modelo) {
        try {

            noticiaServicio.crearNoticia(archivo, titulo, cuerpo, idPeriodista);
            modelo.put("exito", "La noticia fue creada correctamente!!");

            return "/inicio";

        } catch (MiException ex) {

            List<Periodista> periodistas = periodistaServicio.ListarPeriodistas();

            modelo.addAttribute("periodistas", periodistas);

            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @GetMapping("lista")
    public String listar(ModelMap modelo) {

        List<Noticia> noticias = noticiaServicio.ListarNoticias();

        modelo.addAttribute("noticias", noticias);

        return "noticia_lista";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @GetMapping("/mostrar/{id}")
    public String mostrar(@PathVariable Long id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getone(id));

        return "noticia_vista.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {

        modelo.put("noticia", noticiaServicio.getone(id));

        List<Periodista> periodistas = periodistaServicio.ListarPeriodistas();

        modelo.addAttribute("periodistas", periodistas);

        return "noticia_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, String titulo, String cuerpo, Integer idPeriodista, ModelMap modelo, MultipartFile archivo) {
        try {
            noticiaServicio.modificarNoticia(archivo, id, titulo, cuerpo, idPeriodista);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_modificar.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, ModelMap modelo) {
        try {
            noticiaServicio.eliminar(id);
            //modelo.put("exito", "La noticia fué eliminada correctamente");
            return "redirect:../lista";
        } catch (MiException ex) {
            //modelo.put("error", ex.getMessage());

            return "redirect:../lista";
        }
    }

}
