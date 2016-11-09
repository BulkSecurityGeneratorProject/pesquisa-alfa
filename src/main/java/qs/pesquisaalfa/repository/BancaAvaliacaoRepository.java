package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.BancaAvaliacao;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BancaAvaliacao entity.
 */
@SuppressWarnings("unused")
public interface BancaAvaliacaoRepository extends JpaRepository<BancaAvaliacao,Long> {

    @Query("select distinct bancaAvaliacao from BancaAvaliacao bancaAvaliacao left join fetch bancaAvaliacao.professores")
    List<BancaAvaliacao> findAllWithEagerRelationships();

    @Query("select bancaAvaliacao from BancaAvaliacao bancaAvaliacao left join fetch bancaAvaliacao.professores where bancaAvaliacao.id =:id")
    BancaAvaliacao findOneWithEagerRelationships(@Param("id") Long id);

}
