package com.Egg.news.servicios;

import com.Egg.news.entidades.Usuario;
import com.Egg.news.enumeraciones.Rol;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    //new BCryptPasswordEncoder().encode(password)
    @Transactional
    public void crearUsuario(Integer dni, String nombre, String password, String password2) throws MiException {

        validar(dni, nombre, password, password2);

        Usuario usuario = new Usuario();

        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        usuario.setAlta(new Date());
        usuario.setActivo(Boolean.TRUE);

        usuarioRepositorio.save(usuario);
    }

    public List<Usuario> ListarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }

    @Transactional
    public void modificarUsuario(Integer dni, String nombre, String password, String password2) throws MiException {

        validar(dni, nombre, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(dni);

        if (respuesta.isPresent()) {

            Usuario usuario = new Usuario();

            usuario.setNombre(nombre);

            usuarioRepositorio.save(usuario);
        }
    }

    public Usuario getone(Integer dni) {
        return usuarioRepositorio.getOne(dni);
    }

    public void validar(Integer dni, String nombre, String password, String password2) throws MiException {
        if (dni == null) {
            throw new MiException("El dni no puede ser nulo");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni estar vacio");
        }

        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacia y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas no coinciden, deben ser iguales");
        }
    }

    //Recibe un usario para que podamos autenticar, valido a través del nombre. 
    //Se puede validar a través de un email
    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio
                .buscarPorNombre(nombre);

        

        
        if(usuario != null){
             
            List<GrantedAuthority> permisos = new ArrayList();

        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

        permisos.add(p);

        
        //Captamos la información del usuario que ya está logeado, esto nos sirve para mostrar las vistas 
        //de acuerdo al usuario logeado
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        
        session.setAttribute("usuariosession", usuario);
        
        //Lo transformamos es un usario del dominio Spring
        //El constructor de user nos pide un username, una contraseña y una lista de permisos
        return new User(usuario.getNombre(), usuario.getPassword(), permisos);
            
        }else{
            
            return null;            
        }
         
    }

}
