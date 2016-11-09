package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Doutorando;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Doutorando entity.
 */
@SuppressWarnings("unused")
public interface DoutorandoRepository extends JpaRepository<Doutorando,Long> {

}
