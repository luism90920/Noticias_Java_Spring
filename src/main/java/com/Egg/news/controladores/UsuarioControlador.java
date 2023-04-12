package com.Egg.news.controladores;

import com.Egg.news.entidades.Periodista;
import com.Egg.news.entidades.Usuario;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.servicios.PeriodistaServicio;
import com.Egg.news.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.ListarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "usuario_lista.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN')")
    @GetMapping("/modificar/{dni}")
    public String modificar(@PathVariable Integer dni, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getone(dni));
        return "usuario_modificar.html";
    }

    
    @PostMapping("/modificar/{dni}")
    public String modificar(@PathVariable Integer dni, 
            String nombre, 
            String mail,
            String password,
            String password2,
            ModelMap modelo) {

        try {
            usuarioServicio.modificarUsuario(dni, nombre, mail, password, password2);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_modificar.html";
        }

    }
}
