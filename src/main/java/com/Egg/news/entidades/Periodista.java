
package com.Egg.news.entidades;

import com.Egg.news.enumeraciones.Rol;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Periodista extends Usuario{
    
    
    private Integer sueldoMensual;

    public Periodista() {
    }

    public Periodista(Integer sueldoMensual, Integer dni, String nombre, String mail, String password, Rol rol, Boolean activo, Date alta) {
        super(dni, nombre, mail, password, rol, activo, alta);
        this.sueldoMensual = sueldoMensual;
    }

    

    public Integer getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(Integer sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }
    
    
}
