package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Proposta;

import qs.pesquisaalfa.repository.PropostaRepository;
import qs.pesquisaalfa.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Proposta.
 */
@RestController
@RequestMapping("/api")
public class PropostaResource {

    private final Logger log = LoggerFactory.getLogger(PropostaResource.class);
        
    @Inject
    private PropostaRepository propostaRepository;

    /**
     * POST  /propostas : Create a new proposta.
     *
     * @param proposta the proposta to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proposta, or with status 400 (Bad Request) if the proposta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/propostas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proposta> createProposta(@Valid @RequestBody Proposta proposta) throws URISyntaxException {
        log.debug("REST request to save Proposta : {}", proposta);
        if (proposta.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("proposta", "idexists", "A new proposta cannot already have an ID")).body(null);
        }
        Proposta result = propostaRepository.save(proposta);
        return ResponseEntity.created(new URI("/api/propostas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("proposta", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /propostas : Updates an existing proposta.
     *
     * @param proposta the proposta to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proposta,
     * or with status 400 (Bad Request) if the proposta is not valid,
     * or with status 500 (Internal Server Error) if the proposta couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/propostas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proposta> updateProposta(@Valid @RequestBody Proposta proposta) throws URISyntaxException {
        log.debug("REST request to update Proposta : {}", proposta);
        if (proposta.getId() == null) {
            return createProposta(proposta);
        }
        Proposta result = propostaRepository.save(proposta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("proposta", proposta.getId().toString()))
            .body(result);
    }

    /**
     * GET  /propostas : get all the propostas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of propostas in body
     */
    @RequestMapping(value = "/propostas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Proposta> getAllPropostas() {
        log.debug("REST request to get all Propostas");
        List<Proposta> propostas = propostaRepository.findAll();
        return propostas;
    }

    /**
     * GET  /propostas/:id : get the "id" proposta.
     *
     * @param id the id of the proposta to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proposta, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/propostas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proposta> getProposta(@PathVariable Long id) {
        log.debug("REST request to get Proposta : {}", id);
        Proposta proposta = propostaRepository.findOne(id);
        return Optional.ofNullable(proposta)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /propostas/:id : delete the "id" proposta.
     *
     * @param id the id of the proposta to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/propostas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProposta(@PathVariable Long id) {
        log.debug("REST request to delete Proposta : {}", id);
        propostaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("proposta", id.toString())).build();
    }

}
