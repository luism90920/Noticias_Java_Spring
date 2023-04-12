
package com.Egg.news.repositorios;

import com.Egg.news.entidades.Periodista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodistaRepositorio extends JpaRepository<Periodista, Integer> {
    
    @Query("SELECT p FROM Periodista p WHERE p.mail = : mail")
    public Periodista buscarPorNombre(@Param("mail")String mail);
}
