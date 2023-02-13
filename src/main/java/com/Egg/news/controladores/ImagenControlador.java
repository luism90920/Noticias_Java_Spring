
package com.Egg.news.controladores;

import com.Egg.news.entidades.Imagen;
import com.Egg.news.entidades.Noticia;
import com.Egg.news.repositorios.ImagenRepositorio;
import com.Egg.news.servicios.NoticiaServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.servlet.function.RequestPredicates.headers;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    public NoticiaServicio noticiaServicio;
    
    @Autowired
    public ImagenRepositorio imagenRepositorio;
    
    @GetMapping("/noticia/{id}")
    public ResponseEntity<byte[]> imagenNoticia(@PathVariable Long id){
    
        Noticia noticia = noticiaServicio.getone(id);
                
        //en formato arreglo byte
        byte[] imagen = noticia.getImagen().getContenido();
        
        //estas cabeceras lo que van a hacer es decirle al navegador que lo que estamos devolviendo es una imágen
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    
    }
    
    
    
    //el GetMapping de abajo lo arme para cargar la imágen en "noticia_modificar.html" sin necesidad de volver 
    //a cargarla, si es que no la quiero modificar.
    
    @GetMapping("/contenido/{id}")
    public Imagen imagen(@PathVariable Long id){
        
        Noticia noticia = noticiaServicio.getone(id);
        
        Long idImagen = noticia.getImagen().getId();
        
        Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
        
        Imagen imagen = respuesta.get();
        
        return imagen;
    
    }
    
    
    
    
}
