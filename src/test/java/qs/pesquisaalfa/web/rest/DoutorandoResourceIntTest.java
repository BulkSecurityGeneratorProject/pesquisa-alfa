package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Doutorando;
import qs.pesquisaalfa.domain.Aluno;
import qs.pesquisaalfa.repository.DoutorandoRepository;

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
 * Test class for the DoutorandoResource REST controller.
 *
 * @see DoutorandoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class DoutorandoResourceIntTest {

    private static final Boolean DEFAULT_TERMINOU_1_O_PERIODO = false;
    private static final Boolean UPDATED_TERMINOU_1_O_PERIODO = true;

    @Inject
    private DoutorandoRepository doutorandoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDoutorandoMockMvc;

    private Doutorando doutorando;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DoutorandoResource doutorandoResource = new DoutorandoResource();
        ReflectionTestUtils.setField(doutorandoResource, "doutorandoRepository", doutorandoRepository);
        this.restDoutorandoMockMvc = MockMvcBuilders.standaloneSetup(doutorandoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doutorando createEntity(EntityManager em) {
        Doutorando doutorando = new Doutorando()
                .terminou1OPeriodo(DEFAULT_TERMINOU_1_O_PERIODO);
        // Add required entity
        Aluno aluno = AlunoResourceIntTest.createEntity(em);
        em.persist(aluno);
        em.flush();
        doutorando.setAluno(aluno);
        return doutorando;
    }

    @Before
    public void initTest() {
        doutorando = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoutorando() throws Exception {
        int databaseSizeBeforeCreate = doutorandoRepository.findAll().size();

        // Create the Doutorando

        restDoutorandoMockMvc.perform(post("/api/doutorandos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doutorando)))
                .andExpect(status().isCreated());

        // Validate the Doutorando in the database
        List<Doutorando> doutorandos = doutorandoRepository.findAll();
        assertThat(doutorandos).hasSize(databaseSizeBeforeCreate + 1);
        Doutorando testDoutorando = doutorandos.get(doutorandos.size() - 1);
        assertThat(testDoutorando.isTerminou1OPeriodo()).isEqualTo(DEFAULT_TERMINOU_1_O_PERIODO);
    }

    @Test
    @Transactional
    public void checkTerminou1OPeriodoIsRequired() throws Exception {
        int databaseSizeBeforeTest = doutorandoRepository.findAll().size();
        // set the field null
        doutorando.setTerminou1OPeriodo(null);

        // Create the Doutorando, which fails.

        restDoutorandoMockMvc.perform(post("/api/doutorandos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(doutorando)))
                .andExpect(status().isBadRequest());

        List<Doutorando> doutorandos = doutorandoRepository.findAll();
        assertThat(doutorandos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoutorandos() throws Exception {
        // Initialize the database
        doutorandoRepository.saveAndFlush(doutorando);

        // Get all the doutorandos
        restDoutorandoMockMvc.perform(get("/api/doutorandos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(doutorando.getId().intValue())))
                .andExpect(jsonPath("$.[*].terminou1OPeriodo").value(hasItem(DEFAULT_TERMINOU_1_O_PERIODO.booleanValue())));
    }

    @Test
    @Transactional
    public void getDoutorando() throws Exception {
        // Initialize the database
        doutorandoRepository.saveAndFlush(doutorando);

        // Get the doutorando
        restDoutorandoMockMvc.perform(get("/api/doutorandos/{id}", doutorando.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doutorando.getId().intValue()))
            .andExpect(jsonPath("$.terminou1OPeriodo").value(DEFAULT_TERMINOU_1_O_PERIODO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoutorando() throws Exception {
        // Get the doutorando
        restDoutorandoMockMvc.perform(get("/api/doutorandos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoutorando() throws Exception {
        // Initialize the database
        doutorandoRepository.saveAndFlush(doutorando);
        int databaseSizeBeforeUpdate = doutorandoRepository.findAll().size();

        // Update the doutorando
        Doutorando updatedDoutorando = doutorandoRepository.findOne(doutorando.getId());
        updatedDoutorando
                .terminou1OPeriodo(UPDATED_TERMINOU_1_O_PERIODO);

        restDoutorandoMockMvc.perform(put("/api/doutorandos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDoutorando)))
                .andExpect(status().isOk());

        // Validate the Doutorando in the database
        List<Doutorando> doutorandos = doutorandoRepository.findAll();
        assertThat(doutorandos).hasSize(databaseSizeBeforeUpdate);
        Doutorando testDoutorando = doutorandos.get(doutorandos.size() - 1);
        assertThat(testDoutorando.isTerminou1OPeriodo()).isEqualTo(UPDATED_TERMINOU_1_O_PERIODO);
    }

    @Test
    @Transactional
    public void deleteDoutorando() throws Exception {
        // Initialize the database
        doutorandoRepository.saveAndFlush(doutorando);
        int databaseSizeBeforeDelete = doutorandoRepository.findAll().size();

        // Get the doutorando
        restDoutorandoMockMvc.perform(delete("/api/doutorandos/{id}", doutorando.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Doutorando> doutorandos = doutorandoRepository.findAll();
        assertThat(doutorandos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
