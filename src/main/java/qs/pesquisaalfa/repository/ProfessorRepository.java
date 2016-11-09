package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Professor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Professor entity.
 */
@SuppressWarnings("unused")
public interface ProfessorRepository extends JpaRepository<Professor,Long> {

}
