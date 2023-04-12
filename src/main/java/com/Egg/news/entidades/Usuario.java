
package com.Egg.news.entidades;

import com.Egg.news.enumeraciones.Rol;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
public class Usuario {
    
    @Id
    private Integer dni;
    
    private String nombre;
    private String mail;
    private String password;
    
    
    @Enumerated(EnumType.STRING)
    public Rol rol;
    
    private Boolean activo;
    
    @Temporal(TemporalType.DATE)
    private Date alta;

    public Usuario() {
    }
    
    public Usuario(Integer dni, String nombre, String mail, String password, Rol rol, Boolean activo, Date alta) {
        this.dni = dni;
        this.nombre = nombre;
        this.mail = mail;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
        this.alta = alta;
    }


    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

    

    
}
