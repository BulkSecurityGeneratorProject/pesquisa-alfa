package qs.pesquisaalfa.web.rest;

import com.codahale.metrics.annotation.Timed;
import qs.pesquisaalfa.domain.Reuniao;

import qs.pesquisaalfa.repository.ReuniaoRepository;
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
 * REST controller for managing Reuniao.
 */
@RestController
@RequestMapping("/api")
public class ReuniaoResource {

    private final Logger log = LoggerFactory.getLogger(ReuniaoResource.class);
        
    @Inject
    private ReuniaoRepository reuniaoRepository;

    /**
     * POST  /reuniaos : Create a new reuniao.
     *
     * @param reuniao the reuniao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reuniao, or with status 400 (Bad Request) if the reuniao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reuniaos")
    @Timed
    public ResponseEntity<Reuniao> createReuniao(@Valid @RequestBody Reuniao reuniao) throws URISyntaxException {
        log.debug("REST request to save Reuniao : {}", reuniao);
        if (reuniao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("reuniao", "idexists", "A new reuniao cannot already have an ID")).body(null);
        }
        Reuniao result = reuniaoRepository.save(reuniao);
        return ResponseEntity.created(new URI("/api/reuniaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("reuniao", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reuniaos : Updates an existing reuniao.
     *
     * @param reuniao the reuniao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reuniao,
     * or with status 400 (Bad Request) if the reuniao is not valid,
     * or with status 500 (Internal Server Error) if the reuniao couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reuniaos")
    @Timed
    public ResponseEntity<Reuniao> updateReuniao(@Valid @RequestBody Reuniao reuniao) throws URISyntaxException {
        log.debug("REST request to update Reuniao : {}", reuniao);
        if (reuniao.getId() == null) {
            return createReuniao(reuniao);
        }
        Reuniao result = reuniaoRepository.save(reuniao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("reuniao", reuniao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reuniaos : get all the reuniaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of reuniaos in body
     */
    @GetMapping("/reuniaos")
    @Timed
    public List<Reuniao> getAllReuniaos() {
        log.debug("REST request to get all Reuniaos");
        List<Reuniao> reuniaos = reuniaoRepository.findAll();
        return reuniaos;
    }

    /**
     * GET  /reuniaos/:id : get the "id" reuniao.
     *
     * @param id the id of the reuniao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reuniao, or with status 404 (Not Found)
     */
    @GetMapping("/reuniaos/{id}")
    @Timed
    public ResponseEntity<Reuniao> getReuniao(@PathVariable Long id) {
        log.debug("REST request to get Reuniao : {}", id);
        Reuniao reuniao = reuniaoRepository.findOne(id);
        return Optional.ofNullable(reuniao)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reuniaos/:id : delete the "id" reuniao.
     *
     * @param id the id of the reuniao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reuniaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteReuniao(@PathVariable Long id) {
        log.debug("REST request to delete Reuniao : {}", id);
        reuniaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("reuniao", id.toString())).build();
    }

}
