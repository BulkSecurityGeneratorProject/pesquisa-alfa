package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Mestrando;

import qs.pesquisaalfa.repository.MestrandoRepository;
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
 * REST controller for managing Mestrando.
 */
@RestController
@RequestMapping("/api")
public class MestrandoResource {

    private final Logger log = LoggerFactory.getLogger(MestrandoResource.class);
        
    @Inject
    private MestrandoRepository mestrandoRepository;

    /**
     * POST  /mestrandos : Create a new mestrando.
     *
     * @param mestrando the mestrando to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mestrando, or with status 400 (Bad Request) if the mestrando has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mestrandos")
    @Timed
    public ResponseEntity<Mestrando> createMestrando(@Valid @RequestBody Mestrando mestrando) throws URISyntaxException {
        log.debug("REST request to save Mestrando : {}", mestrando);
        if (mestrando.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mestrando", "idexists", "A new mestrando cannot already have an ID")).body(null);
        }
        Mestrando result = mestrandoRepository.save(mestrando);
        return ResponseEntity.created(new URI("/api/mestrandos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mestrando", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mestrandos : Updates an existing mestrando.
     *
     * @param mestrando the mestrando to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mestrando,
     * or with status 400 (Bad Request) if the mestrando is not valid,
     * or with status 500 (Internal Server Error) if the mestrando couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mestrandos")
    @Timed
    public ResponseEntity<Mestrando> updateMestrando(@Valid @RequestBody Mestrando mestrando) throws URISyntaxException {
        log.debug("REST request to update Mestrando : {}", mestrando);
        if (mestrando.getId() == null) {
            return createMestrando(mestrando);
        }
        Mestrando result = mestrandoRepository.save(mestrando);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mestrando", mestrando.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mestrandos : get all the mestrandos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mestrandos in body
     */
    @GetMapping("/mestrandos")
    @Timed
    public List<Mestrando> getAllMestrandos() {
        log.debug("REST request to get all Mestrandos");
        List<Mestrando> mestrandos = mestrandoRepository.findAll();
        return mestrandos;
    }

    /**
     * GET  /mestrandos/:id : get the "id" mestrando.
     *
     * @param id the id of the mestrando to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mestrando, or with status 404 (Not Found)
     */
    @GetMapping("/mestrandos/{id}")
    @Timed
    public ResponseEntity<Mestrando> getMestrando(@PathVariable Long id) {
        log.debug("REST request to get Mestrando : {}", id);
        Mestrando mestrando = mestrandoRepository.findOne(id);
        return Optional.ofNullable(mestrando)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mestrandos/:id : delete the "id" mestrando.
     *
     * @param id the id of the mestrando to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mestrandos/{id}")
    @Timed
    public ResponseEntity<Void> deleteMestrando(@PathVariable Long id) {
        log.debug("REST request to delete Mestrando : {}", id);
        mestrandoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mestrando", id.toString())).build();
    }

}
