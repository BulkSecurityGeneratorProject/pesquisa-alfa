package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Mestrando;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Mestrando entity.
 */
@SuppressWarnings("unused")
public interface MestrandoRepository extends JpaRepository<Mestrando,Long> {

}
