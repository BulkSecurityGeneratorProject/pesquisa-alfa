package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Aluno;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Aluno entity.
 */
@SuppressWarnings("unused")
public interface AlunoRepository extends JpaRepository<Aluno,Long> {

}
