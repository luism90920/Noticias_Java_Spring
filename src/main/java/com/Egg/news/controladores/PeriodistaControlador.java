package com.Egg.news.controladores;

import com.Egg.news.entidades.Periodista;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.servicios.PeriodistaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/periodista")
public class PeriodistaControlador {

    @Autowired
    PeriodistaServicio periodistaServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/registrar")
    public String registrar() {

        return "periodista_registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Integer dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String password2,
            ModelMap modelo) {

        try {
            periodistaServicio.crearPeriodista(dni, nombre, password, password2);
            
            modelo.put("exito", "El periodista fue registrado correctamente");
            return "panel.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "periodista_registro.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Periodista> periodistas = periodistaServicio.ListarPeriodistas();

        modelo.addAttribute("periodistas", periodistas);

        return "periodista_lista.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/modificar/{dni}")
    public String modificar(@PathVariable Integer dni, ModelMap modelo) {
        modelo.addAttribute("periodista", periodistaServicio.getone(dni));
        return "periodista_modificar.html";
    }

    @PostMapping("/modificar/{dni}")
    public String modificar(@PathVariable Integer dni, 
            String nombre, 
            String password,
            String password2,
            ModelMap modelo) {

        try {
            periodistaServicio.modificarPeriodista(dni, nombre, password, password2);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "periodista_modificar.html";
        }

    }
}
