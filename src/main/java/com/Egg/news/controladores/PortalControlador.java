package com.Egg.news.controladores;

import com.Egg.news.entidades.Usuario;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.repositorios.UsuarioRepositorio;
import com.Egg.news.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//Configuramos la url que va a escuchar a este controlador. Va a escuchar a partir de la "/"
//Cada vez que pongamos en nuestra url "localhost:8080/" hacemos el llamado a este controlador
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    UsuarioServicio usuarioServicio;

    //creamos nuestro primer método. GetMapping mapea la url
    @GetMapping("/")
    public String index() {

        //devolvemos la vista una vez que se entre a la "/"
        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {

        return "usuario_registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Integer dni,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String password2,
            ModelMap modelo) {

        try {
            usuarioServicio.crearUsuario(dni, nombre, mail, password, password2);
            modelo.put("exito", "El usuario fue registrado correctamente");
            return "index.html";

        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "usuario_registro.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválidos!!");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_SUPERADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        //deltro del usuario de nombre "logeado" tenemos guardados todos los atributos
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");

        //validamos el rol del usuario
        if (logeado.getRol().toString().equals("SUPERADMIN")) {

            return "redirect:/admin/dashboard";
        } else {
            return "inicio.html";
        }

    }

}
