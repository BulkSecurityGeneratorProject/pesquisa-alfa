package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Gerente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Gerente entity.
 */
@SuppressWarnings("unused")
public interface GerenteRepository extends JpaRepository<Gerente,Long> {

}
