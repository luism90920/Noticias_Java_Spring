package com.Egg.news.servicios;

import com.Egg.news.entidades.Periodista;
import com.Egg.news.enumeraciones.Rol;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.repositorios.PeriodistaRepositorio;
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

@Service
public class PeriodistaServicio implements UserDetailsService {

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;

    @Transactional
    public void crearPeriodista(Integer dni, String nombre, String password, String password2) throws MiException {

        validar(dni, nombre, password, password2);

        Periodista periodista = new Periodista();

        periodista.setDni(dni);
        periodista.setNombre(nombre);
        periodista.setPassword(new BCryptPasswordEncoder().encode(password));
        periodista.setRol(Rol.ADMIN);
        periodista.setAlta(new Date());
        periodista.setActivo(Boolean.TRUE);

        periodistaRepositorio.save(periodista);
    }

    public List<Periodista> ListarPeriodistas() {

        List<Periodista> periodistas = new ArrayList();

        periodistas = periodistaRepositorio.findAll();

        return periodistas;
    }

    @Transactional
    public void modificarPeriodista(Integer dni, String nombre, String password, String password2) throws MiException {

        validar(dni, nombre, password, password2);

        Optional<Periodista> respuesta = periodistaRepositorio.findById(dni);

        if (respuesta.isPresent()) {

            Periodista periodista = respuesta.get();

            periodista.setDni(dni);
            periodista.setNombre(nombre);

            periodistaRepositorio.save(periodista);
        }
    }

    public Periodista getone(Integer dni) {
        return periodistaRepositorio.getOne(dni);
    }

    public void validar(Integer dni, String nombre, String password, String password2) throws MiException {
        if (dni == null) {
            throw new MiException("El dni no puede ser nulo");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo ni estar vacio");
        }

        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiException("La contraseña no estar vacía y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("La contraseñas no coinciden, deben se iguales");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {

        Periodista periodista = periodistaRepositorio.buscarPorNombre(nombre);

        if(periodista != null){
             
            List<GrantedAuthority> permisos = new ArrayList();
            
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+periodista.getRol());
            
            permisos.add(p);
        
            //Lo transformamos es un usario del dominio Spring
            //El constructor de user nos pide un username, una contraseña y una lista de permisos
            return new User(periodista.getNombre(), periodista.getPassword(), permisos);
        }else{
            
            return null;            
        }
        
        
    }
}
