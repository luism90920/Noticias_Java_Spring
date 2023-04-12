
package com.Egg.news.entidades;

import com.Egg.news.enumeraciones.Rol;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
public class Administrador extends Periodista {

    public Administrador() {
    }

    
    
    public Administrador(Integer sueldoMensual, Integer dni, String nombre, String mail, String password, Rol rol, Boolean activo, Date alta) {
        super(sueldoMensual, dni, nombre, mail, password, rol, activo, alta);
    }
    
    
}
