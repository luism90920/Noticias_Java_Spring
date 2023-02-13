
package com.Egg.news.repositorios;

import com.Egg.news.entidades.Noticia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, Long>{
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public Noticia buscarPorTitulo(@Param("titulo")String titulo);
    
    @Query("SELECT n FROM Noticia n WHERE n.periodista.nombre = :nombre")
    public List<Noticia> buscarPorPeriodista(@Param ("nombre") String nombre);
}
