package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Gerente;
import qs.pesquisaalfa.domain.Usuario;
import qs.pesquisaalfa.repository.GerenteRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GerenteResource REST controller.
 *
 * @see GerenteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class GerenteResourceIntTest {

    @Inject
    private GerenteRepository gerenteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGerenteMockMvc;

    private Gerente gerente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GerenteResource gerenteResource = new GerenteResource();
        ReflectionTestUtils.setField(gerenteResource, "gerenteRepository", gerenteRepository);
        this.restGerenteMockMvc = MockMvcBuilders.standaloneSetup(gerenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gerente createEntity(EntityManager em) {
        Gerente gerente = new Gerente();
        // Add required entity
        Usuario usuario = UsuarioResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        gerente.setUsuario(usuario);
        return gerente;
    }

    @Before
    public void initTest() {
        gerente = createEntity(em);
    }

    @Test
    @Transactional
    public void createGerente() throws Exception {
        int databaseSizeBeforeCreate = gerenteRepository.findAll().size();

        // Create the Gerente

        restGerenteMockMvc.perform(post("/api/gerentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gerente)))
                .andExpect(status().isCreated());

        // Validate the Gerente in the database
        List<Gerente> gerentes = gerenteRepository.findAll();
        assertThat(gerentes).hasSize(databaseSizeBeforeCreate + 1);
        Gerente testGerente = gerentes.get(gerentes.size() - 1);
    }

    @Test
    @Transactional
    public void getAllGerentes() throws Exception {
        // Initialize the database
        gerenteRepository.saveAndFlush(gerente);

        // Get all the gerentes
        restGerenteMockMvc.perform(get("/api/gerentes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gerente.getId().intValue())));
    }

    @Test
    @Transactional
    public void getGerente() throws Exception {
        // Initialize the database
        gerenteRepository.saveAndFlush(gerente);

        // Get the gerente
        restGerenteMockMvc.perform(get("/api/gerentes/{id}", gerente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gerente.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGerente() throws Exception {
        // Get the gerente
        restGerenteMockMvc.perform(get("/api/gerentes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGerente() throws Exception {
        // Initialize the database
        gerenteRepository.saveAndFlush(gerente);
        int databaseSizeBeforeUpdate = gerenteRepository.findAll().size();

        // Update the gerente
        Gerente updatedGerente = gerenteRepository.findOne(gerente.getId());

        restGerenteMockMvc.perform(put("/api/gerentes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGerente)))
                .andExpect(status().isOk());

        // Validate the Gerente in the database
        List<Gerente> gerentes = gerenteRepository.findAll();
        assertThat(gerentes).hasSize(databaseSizeBeforeUpdate);
        Gerente testGerente = gerentes.get(gerentes.size() - 1);
    }

    @Test
    @Transactional
    public void deleteGerente() throws Exception {
        // Initialize the database
        gerenteRepository.saveAndFlush(gerente);
        int databaseSizeBeforeDelete = gerenteRepository.findAll().size();

        // Get the gerente
        restGerenteMockMvc.perform(delete("/api/gerentes/{id}", gerente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Gerente> gerentes = gerenteRepository.findAll();
        assertThat(gerentes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
