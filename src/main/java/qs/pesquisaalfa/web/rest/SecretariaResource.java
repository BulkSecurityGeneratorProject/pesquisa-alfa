package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Secretaria;

import qs.pesquisaalfa.repository.SecretariaRepository;
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
 * REST controller for managing Secretaria.
 */
@RestController
@RequestMapping("/api")
public class SecretariaResource {

    private final Logger log = LoggerFactory.getLogger(SecretariaResource.class);
        
    @Inject
    private SecretariaRepository secretariaRepository;

    /**
     * POST  /secretarias : Create a new secretaria.
     *
     * @param secretaria the secretaria to create
     * @return the ResponseEntity with status 201 (Created) and with body the new secretaria, or with status 400 (Bad Request) if the secretaria has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/secretarias")
    @Timed
    public ResponseEntity<Secretaria> createSecretaria(@Valid @RequestBody Secretaria secretaria) throws URISyntaxException {
        log.debug("REST request to save Secretaria : {}", secretaria);
        if (secretaria.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("secretaria", "idexists", "A new secretaria cannot already have an ID")).body(null);
        }
        Secretaria result = secretariaRepository.save(secretaria);
        return ResponseEntity.created(new URI("/api/secretarias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("secretaria", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /secretarias : Updates an existing secretaria.
     *
     * @param secretaria the secretaria to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated secretaria,
     * or with status 400 (Bad Request) if the secretaria is not valid,
     * or with status 500 (Internal Server Error) if the secretaria couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/secretarias")
    @Timed
    public ResponseEntity<Secretaria> updateSecretaria(@Valid @RequestBody Secretaria secretaria) throws URISyntaxException {
        log.debug("REST request to update Secretaria : {}", secretaria);
        if (secretaria.getId() == null) {
            return createSecretaria(secretaria);
        }
        Secretaria result = secretariaRepository.save(secretaria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("secretaria", secretaria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /secretarias : get all the secretarias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of secretarias in body
     */
    @GetMapping("/secretarias")
    @Timed
    public List<Secretaria> getAllSecretarias() {
        log.debug("REST request to get all Secretarias");
        List<Secretaria> secretarias = secretariaRepository.findAll();
        return secretarias;
    }

    /**
     * GET  /secretarias/:id : get the "id" secretaria.
     *
     * @param id the id of the secretaria to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the secretaria, or with status 404 (Not Found)
     */
    @GetMapping("/secretarias/{id}")
    @Timed
    public ResponseEntity<Secretaria> getSecretaria(@PathVariable Long id) {
        log.debug("REST request to get Secretaria : {}", id);
        Secretaria secretaria = secretariaRepository.findOne(id);
        return Optional.ofNullable(secretaria)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /secretarias/:id : delete the "id" secretaria.
     *
     * @param id the id of the secretaria to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/secretarias/{id}")
    @Timed
    public ResponseEntity<Void> deleteSecretaria(@PathVariable Long id) {
        log.debug("REST request to delete Secretaria : {}", id);
        secretariaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("secretaria", id.toString())).build();
    }

}
