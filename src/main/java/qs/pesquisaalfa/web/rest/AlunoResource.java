package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Aluno;

import qs.pesquisaalfa.repository.AlunoRepository;
import qs.pesquisaalfa.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Aluno.
 */
@RestController
@RequestMapping("/api")
public class AlunoResource {

    private final Logger log = LoggerFactory.getLogger(AlunoResource.class);
        
    @Inject
    private AlunoRepository alunoRepository;

    /**
     * POST  /alunos : Create a new aluno.
     *
     * @param aluno the aluno to create
     * @return the ResponseEntity with status 201 (Created) and with body the new aluno, or with status 400 (Bad Request) if the aluno has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/alunos")
    @Timed
    public ResponseEntity<Aluno> createAluno(@Valid @RequestBody Aluno aluno) throws URISyntaxException {
        log.debug("REST request to save Aluno : {}", aluno);
        if (aluno.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("aluno", "idexists", "A new aluno cannot already have an ID")).body(null);
        }
        Aluno result = alunoRepository.save(aluno);
        return ResponseEntity.created(new URI("/api/alunos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("aluno", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /alunos : Updates an existing aluno.
     *
     * @param aluno the aluno to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated aluno,
     * or with status 400 (Bad Request) if the aluno is not valid,
     * or with status 500 (Internal Server Error) if the aluno couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/alunos")
    @Timed
    public ResponseEntity<Aluno> updateAluno(@Valid @RequestBody Aluno aluno) throws URISyntaxException {
        log.debug("REST request to update Aluno : {}", aluno);
        if (aluno.getId() == null) {
            return createAluno(aluno);
        }
        Aluno result = alunoRepository.save(aluno);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("aluno", aluno.getId().toString()))
            .body(result);
    }

    /**
     * GET  /alunos : get all the alunos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of alunos in body
     */
    @GetMapping("/alunos")
    @Timed
    public List<Aluno> getAllAlunos() {
        log.debug("REST request to get all Alunos");
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos;
    }

    /**
     * GET  /alunos/:id : get the "id" aluno.
     *
     * @param id the id of the aluno to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the aluno, or with status 404 (Not Found)
     */
    @GetMapping("/alunos/{id}")
    @Timed
    public ResponseEntity<Aluno> getAluno(@PathVariable Long id) {
        log.debug("REST request to get Aluno : {}", id);
        Aluno aluno = alunoRepository.findOne(id);
        return Optional.ofNullable(aluno)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /alunos/:id : delete the "id" aluno.
     *
     * @param id the id of the aluno to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alunos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        log.debug("REST request to delete Aluno : {}", id);
        alunoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("aluno", id.toString())).build();
    }

}
