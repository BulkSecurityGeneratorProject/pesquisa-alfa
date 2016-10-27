package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Gerente;

import qs.pesquisaalfa.repository.GerenteRepository;
import qs.pesquisaalfa.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Gerente.
 */
@RestController
@RequestMapping("/api")
public class GerenteResource {

    private final Logger log = LoggerFactory.getLogger(GerenteResource.class);
        
    @Inject
    private GerenteRepository gerenteRepository;

    /**
     * POST  /gerentes : Create a new gerente.
     *
     * @param gerente the gerente to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gerente, or with status 400 (Bad Request) if the gerente has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gerentes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gerente> createGerente(@RequestBody Gerente gerente) throws URISyntaxException {
        log.debug("REST request to save Gerente : {}", gerente);
        if (gerente.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gerente", "idexists", "A new gerente cannot already have an ID")).body(null);
        }
        Gerente result = gerenteRepository.save(gerente);
        return ResponseEntity.created(new URI("/api/gerentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gerente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gerentes : Updates an existing gerente.
     *
     * @param gerente the gerente to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gerente,
     * or with status 400 (Bad Request) if the gerente is not valid,
     * or with status 500 (Internal Server Error) if the gerente couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/gerentes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gerente> updateGerente(@RequestBody Gerente gerente) throws URISyntaxException {
        log.debug("REST request to update Gerente : {}", gerente);
        if (gerente.getId() == null) {
            return createGerente(gerente);
        }
        Gerente result = gerenteRepository.save(gerente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gerente", gerente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gerentes : get all the gerentes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gerentes in body
     */
    @RequestMapping(value = "/gerentes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Gerente> getAllGerentes() {
        log.debug("REST request to get all Gerentes");
        List<Gerente> gerentes = gerenteRepository.findAll();
        return gerentes;
    }

    /**
     * GET  /gerentes/:id : get the "id" gerente.
     *
     * @param id the id of the gerente to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gerente, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/gerentes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Gerente> getGerente(@PathVariable Long id) {
        log.debug("REST request to get Gerente : {}", id);
        Gerente gerente = gerenteRepository.findOne(id);
        return Optional.ofNullable(gerente)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gerentes/:id : delete the "id" gerente.
     *
     * @param id the id of the gerente to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/gerentes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGerente(@PathVariable Long id) {
        log.debug("REST request to delete Gerente : {}", id);
        gerenteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gerente", id.toString())).build();
    }

}
