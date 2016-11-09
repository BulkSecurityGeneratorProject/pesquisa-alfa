package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Tese;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tese entity.
 */
@SuppressWarnings("unused")
public interface TeseRepository extends JpaRepository<Tese,Long> {

}
