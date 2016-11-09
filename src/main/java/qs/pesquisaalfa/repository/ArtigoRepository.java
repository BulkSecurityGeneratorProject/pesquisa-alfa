package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Artigo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Artigo entity.
 */
@SuppressWarnings("unused")
public interface ArtigoRepository extends JpaRepository<Artigo,Long> {

    @Query("select distinct artigo from Artigo artigo left join fetch artigo.alunos")
    List<Artigo> findAllWithEagerRelationships();

    @Query("select artigo from Artigo artigo left join fetch artigo.alunos where artigo.id =:id")
    Artigo findOneWithEagerRelationships(@Param("id") Long id);

}
