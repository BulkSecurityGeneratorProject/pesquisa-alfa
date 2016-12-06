package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.ApresentacoesArtigo;

import qs.pesquisaalfa.repository.ApresentacoesArtigoRepository;
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
 * REST controller for managing ApresentacoesArtigo.
 */
@RestController
@RequestMapping("/api")
public class ApresentacoesArtigoResource {

    private final Logger log = LoggerFactory.getLogger(ApresentacoesArtigoResource.class);
        
    @Inject
    private ApresentacoesArtigoRepository apresentacoesArtigoRepository;

    /**
     * POST  /apresentacoes-artigos : Create a new apresentacoesArtigo.
     *
     * @param apresentacoesArtigo the apresentacoesArtigo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apresentacoesArtigo, or with status 400 (Bad Request) if the apresentacoesArtigo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apresentacoes-artigos")
    @Timed
    public ResponseEntity<ApresentacoesArtigo> createApresentacoesArtigo(@Valid @RequestBody ApresentacoesArtigo apresentacoesArtigo) throws URISyntaxException {
        log.debug("REST request to save ApresentacoesArtigo : {}", apresentacoesArtigo);
        if (apresentacoesArtigo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("apresentacoesArtigo", "idexists", "A new apresentacoesArtigo cannot already have an ID")).body(null);
        }
        ApresentacoesArtigo result = apresentacoesArtigoRepository.save(apresentacoesArtigo);
        return ResponseEntity.created(new URI("/api/apresentacoes-artigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("apresentacoesArtigo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apresentacoes-artigos : Updates an existing apresentacoesArtigo.
     *
     * @param apresentacoesArtigo the apresentacoesArtigo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apresentacoesArtigo,
     * or with status 400 (Bad Request) if the apresentacoesArtigo is not valid,
     * or with status 500 (Internal Server Error) if the apresentacoesArtigo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apresentacoes-artigos")
    @Timed
    public ResponseEntity<ApresentacoesArtigo> updateApresentacoesArtigo(@Valid @RequestBody ApresentacoesArtigo apresentacoesArtigo) throws URISyntaxException {
        log.debug("REST request to update ApresentacoesArtigo : {}", apresentacoesArtigo);
        if (apresentacoesArtigo.getId() == null) {
            return createApresentacoesArtigo(apresentacoesArtigo);
        }
        ApresentacoesArtigo result = apresentacoesArtigoRepository.save(apresentacoesArtigo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("apresentacoesArtigo", apresentacoesArtigo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apresentacoes-artigos : get all the apresentacoesArtigos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apresentacoesArtigos in body
     */
    @GetMapping("/apresentacoes-artigos")
    @Timed
    public List<ApresentacoesArtigo> getAllApresentacoesArtigos() {
        log.debug("REST request to get all ApresentacoesArtigos");
        List<ApresentacoesArtigo> apresentacoesArtigos = apresentacoesArtigoRepository.findAll();
        return apresentacoesArtigos;
    }

    /**
     * GET  /apresentacoes-artigos/:id : get the "id" apresentacoesArtigo.
     *
     * @param id the id of the apresentacoesArtigo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apresentacoesArtigo, or with status 404 (Not Found)
     */
    @GetMapping("/apresentacoes-artigos/{id}")
    @Timed
    public ResponseEntity<ApresentacoesArtigo> getApresentacoesArtigo(@PathVariable Long id) {
        log.debug("REST request to get ApresentacoesArtigo : {}", id);
        ApresentacoesArtigo apresentacoesArtigo = apresentacoesArtigoRepository.findOne(id);
        return Optional.ofNullable(apresentacoesArtigo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /apresentacoes-artigos/:id : delete the "id" apresentacoesArtigo.
     *
     * @param id the id of the apresentacoesArtigo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apresentacoes-artigos/{id}")
    @Timed
    public ResponseEntity<Void> deleteApresentacoesArtigo(@PathVariable Long id) {
        log.debug("REST request to delete ApresentacoesArtigo : {}", id);
        apresentacoesArtigoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("apresentacoesArtigo", id.toString())).build();
    }

}
