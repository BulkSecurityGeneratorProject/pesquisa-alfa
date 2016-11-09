package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Artigo;

import qs.pesquisaalfa.repository.ArtigoRepository;
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
 * REST controller for managing Artigo.
 */
@RestController
@RequestMapping("/api")
public class ArtigoResource {

    private final Logger log = LoggerFactory.getLogger(ArtigoResource.class);
        
    @Inject
    private ArtigoRepository artigoRepository;

    /**
     * POST  /artigos : Create a new artigo.
     *
     * @param artigo the artigo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new artigo, or with status 400 (Bad Request) if the artigo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/artigos")
    @Timed
    public ResponseEntity<Artigo> createArtigo(@Valid @RequestBody Artigo artigo) throws URISyntaxException {
        log.debug("REST request to save Artigo : {}", artigo);
        if (artigo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("artigo", "idexists", "A new artigo cannot already have an ID")).body(null);
        }
        Artigo result = artigoRepository.save(artigo);
        return ResponseEntity.created(new URI("/api/artigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("artigo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /artigos : Updates an existing artigo.
     *
     * @param artigo the artigo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated artigo,
     * or with status 400 (Bad Request) if the artigo is not valid,
     * or with status 500 (Internal Server Error) if the artigo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/artigos")
    @Timed
    public ResponseEntity<Artigo> updateArtigo(@Valid @RequestBody Artigo artigo) throws URISyntaxException {
        log.debug("REST request to update Artigo : {}", artigo);
        if (artigo.getId() == null) {
            return createArtigo(artigo);
        }
        Artigo result = artigoRepository.save(artigo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("artigo", artigo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /artigos : get all the artigos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of artigos in body
     */
    @GetMapping("/artigos")
    @Timed
    public List<Artigo> getAllArtigos() {
        log.debug("REST request to get all Artigos");
        List<Artigo> artigos = artigoRepository.findAllWithEagerRelationships();
        return artigos;
    }

    /**
     * GET  /artigos/:id : get the "id" artigo.
     *
     * @param id the id of the artigo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the artigo, or with status 404 (Not Found)
     */
    @GetMapping("/artigos/{id}")
    @Timed
    public ResponseEntity<Artigo> getArtigo(@PathVariable Long id) {
        log.debug("REST request to get Artigo : {}", id);
        Artigo artigo = artigoRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(artigo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /artigos/:id : delete the "id" artigo.
     *
     * @param id the id of the artigo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/artigos/{id}")
    @Timed
    public ResponseEntity<Void> deleteArtigo(@PathVariable Long id) {
        log.debug("REST request to delete Artigo : {}", id);
        artigoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("artigo", id.toString())).build();
    }

}
