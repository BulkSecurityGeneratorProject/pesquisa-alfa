package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Orientador;
import qs.pesquisaalfa.domain.Professor;
import qs.pesquisaalfa.repository.OrientadorRepository;

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
 * Test class for the OrientadorResource REST controller.
 *
 * @see OrientadorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class OrientadorResourceIntTest {

    @Inject
    private OrientadorRepository orientadorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrientadorMockMvc;

    private Orientador orientador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrientadorResource orientadorResource = new OrientadorResource();
        ReflectionTestUtils.setField(orientadorResource, "orientadorRepository", orientadorRepository);
        this.restOrientadorMockMvc = MockMvcBuilders.standaloneSetup(orientadorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orientador createEntity(EntityManager em) {
        Orientador orientador = new Orientador();
        // Add required entity
        Professor professor = ProfessorResourceIntTest.createEntity(em);
        em.persist(professor);
        em.flush();
        orientador.setProfessor(professor);
        return orientador;
    }

    @Before
    public void initTest() {
        orientador = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrientador() throws Exception {
        int databaseSizeBeforeCreate = orientadorRepository.findAll().size();

        // Create the Orientador

        restOrientadorMockMvc.perform(post("/api/orientadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orientador)))
                .andExpect(status().isCreated());

        // Validate the Orientador in the database
        List<Orientador> orientadors = orientadorRepository.findAll();
        assertThat(orientadors).hasSize(databaseSizeBeforeCreate + 1);
        Orientador testOrientador = orientadors.get(orientadors.size() - 1);
    }

    @Test
    @Transactional
    public void getAllOrientadors() throws Exception {
        // Initialize the database
        orientadorRepository.saveAndFlush(orientador);

        // Get all the orientadors
        restOrientadorMockMvc.perform(get("/api/orientadors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orientador.getId().intValue())));
    }

    @Test
    @Transactional
    public void getOrientador() throws Exception {
        // Initialize the database
        orientadorRepository.saveAndFlush(orientador);

        // Get the orientador
        restOrientadorMockMvc.perform(get("/api/orientadors/{id}", orientador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orientador.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrientador() throws Exception {
        // Get the orientador
        restOrientadorMockMvc.perform(get("/api/orientadors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrientador() throws Exception {
        // Initialize the database
        orientadorRepository.saveAndFlush(orientador);
        int databaseSizeBeforeUpdate = orientadorRepository.findAll().size();

        // Update the orientador
        Orientador updatedOrientador = orientadorRepository.findOne(orientador.getId());

        restOrientadorMockMvc.perform(put("/api/orientadors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrientador)))
                .andExpect(status().isOk());

        // Validate the Orientador in the database
        List<Orientador> orientadors = orientadorRepository.findAll();
        assertThat(orientadors).hasSize(databaseSizeBeforeUpdate);
        Orientador testOrientador = orientadors.get(orientadors.size() - 1);
    }

    @Test
    @Transactional
    public void deleteOrientador() throws Exception {
        // Initialize the database
        orientadorRepository.saveAndFlush(orientador);
        int databaseSizeBeforeDelete = orientadorRepository.findAll().size();

        // Get the orientador
        restOrientadorMockMvc.perform(delete("/api/orientadors/{id}", orientador.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Orientador> orientadors = orientadorRepository.findAll();
        assertThat(orientadors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
