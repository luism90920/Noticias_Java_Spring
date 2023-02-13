
package com.Egg.news.servicios;

import com.Egg.news.entidades.Imagen;
import com.Egg.news.entidades.Noticia;
import com.Egg.news.entidades.Periodista;
import com.Egg.news.excepciones.MiException;
import com.Egg.news.repositorios.NoticiaRepositorio;
import com.Egg.news.repositorios.PeriodistaRepositorio;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {
    
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    
    @Autowired 
    private ImagenServicio imagenServicio;
    
    @Transactional
    public void crearNoticia(MultipartFile archivo, String titulo, String cuerpo, Integer idPeriodista ) throws MiException{
        
        validar(titulo, cuerpo, idPeriodista);
        Periodista periodista = periodistaRepositorio.findById(idPeriodista).get();
        Noticia noticia = new Noticia();
        
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setPeriodista(periodista);
        
        noticia.setAlta(new Date());
        
        Imagen imagen = imagenServicio.guardar(archivo);
        
        noticia.setImagen(imagen);
        
        noticiaRepositorio.save(noticia);
    }
    
    public List<Noticia> ListarNoticias(){
        List<Noticia> noticias = new ArrayList();
        
        noticias = noticiaRepositorio.findAll();
        
        return noticias;
    }
    
    @Transactional
    public void modificarNoticia(MultipartFile archivo, Long id, String titulo, String cuerpo, Integer idPeriodista) throws MiException{
        
        validar(titulo, cuerpo, idPeriodista);
        
        Optional<Noticia> respuesta = noticiaRepositorio.findById(id);
        Optional<Periodista> respuestaPeriodista = periodistaRepositorio.findById(idPeriodista);
        
        Periodista periodista = new Periodista();
        
        if(respuestaPeriodista.isPresent()){
            periodista = respuestaPeriodista.get();
        }
        
        if(respuesta.isPresent()){
            
            Noticia noticia = respuesta.get();
            
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setPeriodista(periodista);
            
            Long idImagen = null;
            
            if(noticia.getImagen() != null){
                idImagen = noticia.getImagen().getId();
            }
            
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            
            noticia.setImagen(imagen);
            
            noticiaRepositorio.save(noticia);
        }
    }
    
    
    @Transactional
    public void eliminar(Long id) throws MiException{
        noticiaRepositorio.deleteById(id);
    }
    
    
    public Noticia getone(Long id){
        return noticiaRepositorio.getOne(id);
    }
    
    
    public void validar(String titulo, String cuerpo, Integer idPeriodista) throws MiException{
        if( titulo == null || titulo.isEmpty()){
            throw new MiException("El titulo no puede ser nulo ni estar vacio");
        }
        
        if(cuerpo == null || cuerpo.isEmpty()){
            throw new MiException("El cuerpos no puede ser nulo ni estar vacio");
        }
    }
}
