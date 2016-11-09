package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Orientador;

import qs.pesquisaalfa.repository.OrientadorRepository;
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
 * REST controller for managing Orientador.
 */
@RestController
@RequestMapping("/api")
public class OrientadorResource {

    private final Logger log = LoggerFactory.getLogger(OrientadorResource.class);
        
    @Inject
    private OrientadorRepository orientadorRepository;

    /**
     * POST  /orientadors : Create a new orientador.
     *
     * @param orientador the orientador to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orientador, or with status 400 (Bad Request) if the orientador has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/orientadors")
    @Timed
    public ResponseEntity<Orientador> createOrientador(@Valid @RequestBody Orientador orientador) throws URISyntaxException {
        log.debug("REST request to save Orientador : {}", orientador);
        if (orientador.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orientador", "idexists", "A new orientador cannot already have an ID")).body(null);
        }
        Orientador result = orientadorRepository.save(orientador);
        return ResponseEntity.created(new URI("/api/orientadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orientador", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /orientadors : Updates an existing orientador.
     *
     * @param orientador the orientador to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orientador,
     * or with status 400 (Bad Request) if the orientador is not valid,
     * or with status 500 (Internal Server Error) if the orientador couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/orientadors")
    @Timed
    public ResponseEntity<Orientador> updateOrientador(@Valid @RequestBody Orientador orientador) throws URISyntaxException {
        log.debug("REST request to update Orientador : {}", orientador);
        if (orientador.getId() == null) {
            return createOrientador(orientador);
        }
        Orientador result = orientadorRepository.save(orientador);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orientador", orientador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /orientadors : get all the orientadors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orientadors in body
     */
    @GetMapping("/orientadors")
    @Timed
    public List<Orientador> getAllOrientadors() {
        log.debug("REST request to get all Orientadors");
        List<Orientador> orientadors = orientadorRepository.findAll();
        return orientadors;
    }

    /**
     * GET  /orientadors/:id : get the "id" orientador.
     *
     * @param id the id of the orientador to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orientador, or with status 404 (Not Found)
     */
    @GetMapping("/orientadors/{id}")
    @Timed
    public ResponseEntity<Orientador> getOrientador(@PathVariable Long id) {
        log.debug("REST request to get Orientador : {}", id);
        Orientador orientador = orientadorRepository.findOne(id);
        return Optional.ofNullable(orientador)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /orientadors/:id : delete the "id" orientador.
     *
     * @param id the id of the orientador to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orientadors/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrientador(@PathVariable Long id) {
        log.debug("REST request to delete Orientador : {}", id);
        orientadorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orientador", id.toString())).build();
    }

}
