package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Doutorando;

import qs.pesquisaalfa.repository.DoutorandoRepository;
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
 * REST controller for managing Doutorando.
 */
@RestController
@RequestMapping("/api")
public class DoutorandoResource {

    private final Logger log = LoggerFactory.getLogger(DoutorandoResource.class);
        
    @Inject
    private DoutorandoRepository doutorandoRepository;

    /**
     * POST  /doutorandos : Create a new doutorando.
     *
     * @param doutorando the doutorando to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doutorando, or with status 400 (Bad Request) if the doutorando has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doutorandos")
    @Timed
    public ResponseEntity<Doutorando> createDoutorando(@Valid @RequestBody Doutorando doutorando) throws URISyntaxException {
        log.debug("REST request to save Doutorando : {}", doutorando);
        if (doutorando.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("doutorando", "idexists", "A new doutorando cannot already have an ID")).body(null);
        }
        Doutorando result = doutorandoRepository.save(doutorando);
        return ResponseEntity.created(new URI("/api/doutorandos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("doutorando", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doutorandos : Updates an existing doutorando.
     *
     * @param doutorando the doutorando to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doutorando,
     * or with status 400 (Bad Request) if the doutorando is not valid,
     * or with status 500 (Internal Server Error) if the doutorando couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doutorandos")
    @Timed
    public ResponseEntity<Doutorando> updateDoutorando(@Valid @RequestBody Doutorando doutorando) throws URISyntaxException {
        log.debug("REST request to update Doutorando : {}", doutorando);
        if (doutorando.getId() == null) {
            return createDoutorando(doutorando);
        }
        Doutorando result = doutorandoRepository.save(doutorando);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("doutorando", doutorando.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doutorandos : get all the doutorandos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of doutorandos in body
     */
    @GetMapping("/doutorandos")
    @Timed
    public List<Doutorando> getAllDoutorandos() {
        log.debug("REST request to get all Doutorandos");
        List<Doutorando> doutorandos = doutorandoRepository.findAll();
        return doutorandos;
    }

    /**
     * GET  /doutorandos/:id : get the "id" doutorando.
     *
     * @param id the id of the doutorando to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doutorando, or with status 404 (Not Found)
     */
    @GetMapping("/doutorandos/{id}")
    @Timed
    public ResponseEntity<Doutorando> getDoutorando(@PathVariable Long id) {
        log.debug("REST request to get Doutorando : {}", id);
        Doutorando doutorando = doutorandoRepository.findOne(id);
        return Optional.ofNullable(doutorando)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /doutorandos/:id : delete the "id" doutorando.
     *
     * @param id the id of the doutorando to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doutorandos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoutorando(@PathVariable Long id) {
        log.debug("REST request to delete Doutorando : {}", id);
        doutorandoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("doutorando", id.toString())).build();
    }

}
