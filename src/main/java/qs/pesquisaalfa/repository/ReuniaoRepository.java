package qs.pesquisaalfa.repository;

import qs.pesquisaalfa.domain.Reuniao;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Reuniao entity.
 */
@SuppressWarnings("unused")
public interface ReuniaoRepository extends JpaRepository<Reuniao,Long> {

}
