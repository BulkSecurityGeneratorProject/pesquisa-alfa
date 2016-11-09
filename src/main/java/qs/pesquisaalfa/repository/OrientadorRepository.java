package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Orientador;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Orientador entity.
 */
@SuppressWarnings("unused")
public interface OrientadorRepository extends JpaRepository<Orientador,Long> {

}
