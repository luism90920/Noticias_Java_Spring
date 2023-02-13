
package com.Egg.news.entidades;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Imagen {
    
    
    @Id
    @GeneratedValue(generator = "uuid")
    private Long id;
    
    //ésta caracteristica del tipo "mime" es el atributo que asigna el formato del archivo de la imágen
    private String mime;
    
    private String nombre;
    
    //byte[] es un arreglo de byte que es la forma en que se va a guardar el contenido
    //@Lob le decimos con esta anotación a spring que este dato puede ser grande, muy pesado
    //@Basic definimos el tipo de carga, que va a ser peresosa, es decir que se carga solo cuando lo pidamos
    //solo cuando hacemos un "get"
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

    public Imagen() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }
    
    
    
}
