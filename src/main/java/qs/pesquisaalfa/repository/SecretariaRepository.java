package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Secretaria;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Secretaria entity.
 */
@SuppressWarnings("unused")
public interface SecretariaRepository extends JpaRepository<Secretaria,Long> {

}
