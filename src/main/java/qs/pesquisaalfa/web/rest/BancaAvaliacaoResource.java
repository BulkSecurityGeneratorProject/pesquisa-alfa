package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.BancaAvaliacao;

import qs.pesquisaalfa.repository.BancaAvaliacaoRepository;
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
 * REST controller for managing BancaAvaliacao.
 */
@RestController
@RequestMapping("/api")
public class BancaAvaliacaoResource {

    private final Logger log = LoggerFactory.getLogger(BancaAvaliacaoResource.class);
        
    @Inject
    private BancaAvaliacaoRepository bancaAvaliacaoRepository;

    /**
     * POST  /banca-avaliacaos : Create a new bancaAvaliacao.
     *
     * @param bancaAvaliacao the bancaAvaliacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bancaAvaliacao, or with status 400 (Bad Request) if the bancaAvaliacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/banca-avaliacaos")
    @Timed
    public ResponseEntity<BancaAvaliacao> createBancaAvaliacao(@Valid @RequestBody BancaAvaliacao bancaAvaliacao) throws URISyntaxException {
        log.debug("REST request to save BancaAvaliacao : {}", bancaAvaliacao);
        if (bancaAvaliacao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bancaAvaliacao", "idexists", "A new bancaAvaliacao cannot already have an ID")).body(null);
        }
        BancaAvaliacao result = bancaAvaliacaoRepository.save(bancaAvaliacao);
        return ResponseEntity.created(new URI("/api/banca-avaliacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bancaAvaliacao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /banca-avaliacaos : Updates an existing bancaAvaliacao.
     *
     * @param bancaAvaliacao the bancaAvaliacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bancaAvaliacao,
     * or with status 400 (Bad Request) if the bancaAvaliacao is not valid,
     * or with status 500 (Internal Server Error) if the bancaAvaliacao couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/banca-avaliacaos")
    @Timed
    public ResponseEntity<BancaAvaliacao> updateBancaAvaliacao(@Valid @RequestBody BancaAvaliacao bancaAvaliacao) throws URISyntaxException {
        log.debug("REST request to update BancaAvaliacao : {}", bancaAvaliacao);
        if (bancaAvaliacao.getId() == null) {
            return createBancaAvaliacao(bancaAvaliacao);
        }
        BancaAvaliacao result = bancaAvaliacaoRepository.save(bancaAvaliacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bancaAvaliacao", bancaAvaliacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /banca-avaliacaos : get all the bancaAvaliacaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bancaAvaliacaos in body
     */
    @GetMapping("/banca-avaliacaos")
    @Timed
    public List<BancaAvaliacao> getAllBancaAvaliacaos() {
        log.debug("REST request to get all BancaAvaliacaos");
        List<BancaAvaliacao> bancaAvaliacaos = bancaAvaliacaoRepository.findAllWithEagerRelationships();
        return bancaAvaliacaos;
    }

    /**
     * GET  /banca-avaliacaos/:id : get the "id" bancaAvaliacao.
     *
     * @param id the id of the bancaAvaliacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bancaAvaliacao, or with status 404 (Not Found)
     */
    @GetMapping("/banca-avaliacaos/{id}")
    @Timed
    public ResponseEntity<BancaAvaliacao> getBancaAvaliacao(@PathVariable Long id) {
        log.debug("REST request to get BancaAvaliacao : {}", id);
        BancaAvaliacao bancaAvaliacao = bancaAvaliacaoRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(bancaAvaliacao)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /banca-avaliacaos/:id : delete the "id" bancaAvaliacao.
     *
     * @param id the id of the bancaAvaliacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/banca-avaliacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteBancaAvaliacao(@PathVariable Long id) {
        log.debug("REST request to delete BancaAvaliacao : {}", id);
        bancaAvaliacaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bancaAvaliacao", id.toString())).build();
    }

}
