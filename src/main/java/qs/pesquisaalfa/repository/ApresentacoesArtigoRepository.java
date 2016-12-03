package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.ApresentacoesArtigo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApresentacoesArtigo entity.
 */
@SuppressWarnings("unused")
public interface ApresentacoesArtigoRepository extends JpaRepository<ApresentacoesArtigo,Long> {

}
