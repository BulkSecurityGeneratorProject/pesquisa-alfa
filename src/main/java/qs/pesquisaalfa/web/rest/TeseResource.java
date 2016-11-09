package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Tese;

import qs.pesquisaalfa.repository.TeseRepository;
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
 * REST controller for managing Tese.
 */
@RestController
@RequestMapping("/api")
public class TeseResource {

    private final Logger log = LoggerFactory.getLogger(TeseResource.class);
        
    @Inject
    private TeseRepository teseRepository;

    /**
     * POST  /tese : Create a new tese.
     *
     * @param tese the tese to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tese, or with status 400 (Bad Request) if the tese has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tese")
    @Timed
    public ResponseEntity<Tese> createTese(@Valid @RequestBody Tese tese) throws URISyntaxException {
        log.debug("REST request to save Tese : {}", tese);
        if (tese.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tese", "idexists", "A new tese cannot already have an ID")).body(null);
        }
        Tese result = teseRepository.save(tese);
        return ResponseEntity.created(new URI("/api/tese/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tese", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tese : Updates an existing tese.
     *
     * @param tese the tese to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tese,
     * or with status 400 (Bad Request) if the tese is not valid,
     * or with status 500 (Internal Server Error) if the tese couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tese")
    @Timed
    public ResponseEntity<Tese> updateTese(@Valid @RequestBody Tese tese) throws URISyntaxException {
        log.debug("REST request to update Tese : {}", tese);
        if (tese.getId() == null) {
            return createTese(tese);
        }
        Tese result = teseRepository.save(tese);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tese", tese.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tese : get all the tese.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tese in body
     */
    @GetMapping("/tese")
    @Timed
    public List<Tese> getAllTese() {
        log.debug("REST request to get all Tese");
        List<Tese> tese = teseRepository.findAll();
        return tese;
    }

    /**
     * GET  /tese/:id : get the "id" tese.
     *
     * @param id the id of the tese to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tese, or with status 404 (Not Found)
     */
    @GetMapping("/tese/{id}")
    @Timed
    public ResponseEntity<Tese> getTese(@PathVariable Long id) {
        log.debug("REST request to get Tese : {}", id);
        Tese tese = teseRepository.findOne(id);
        return Optional.ofNullable(tese)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tese/:id : delete the "id" tese.
     *
     * @param id the id of the tese to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tese/{id}")
    @Timed
    public ResponseEntity<Void> deleteTese(@PathVariable Long id) {
        log.debug("REST request to delete Tese : {}", id);
        teseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tese", id.toString())).build();
    }

}
