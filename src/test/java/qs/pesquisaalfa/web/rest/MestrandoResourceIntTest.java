package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Mestrando;
import qs.pesquisaalfa.domain.Aluno;
import qs.pesquisaalfa.repository.MestrandoRepository;

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
 * Test class for the MestrandoResource REST controller.
 *
 * @see MestrandoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class MestrandoResourceIntTest {

    private static final Boolean DEFAULT_TERMINOU_OBRIGATORIAS = false;
    private static final Boolean UPDATED_TERMINOU_OBRIGATORIAS = true;

    @Inject
    private MestrandoRepository mestrandoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMestrandoMockMvc;

    private Mestrando mestrando;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MestrandoResource mestrandoResource = new MestrandoResource();
        ReflectionTestUtils.setField(mestrandoResource, "mestrandoRepository", mestrandoRepository);
        this.restMestrandoMockMvc = MockMvcBuilders.standaloneSetup(mestrandoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mestrando createEntity(EntityManager em) {
        Mestrando mestrando = new Mestrando()
                .terminouObrigatorias(DEFAULT_TERMINOU_OBRIGATORIAS);
        // Add required entity
        Aluno aluno = AlunoResourceIntTest.createEntity(em);
        em.persist(aluno);
        em.flush();
        mestrando.setAluno(aluno);
        return mestrando;
    }

    @Before
    public void initTest() {
        mestrando = createEntity(em);
    }

    @Test
    @Transactional
    public void createMestrando() throws Exception {
        int databaseSizeBeforeCreate = mestrandoRepository.findAll().size();

        // Create the Mestrando

        restMestrandoMockMvc.perform(post("/api/mestrandos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mestrando)))
                .andExpect(status().isCreated());

        // Validate the Mestrando in the database
        List<Mestrando> mestrandos = mestrandoRepository.findAll();
        assertThat(mestrandos).hasSize(databaseSizeBeforeCreate + 1);
        Mestrando testMestrando = mestrandos.get(mestrandos.size() - 1);
        assertThat(testMestrando.isTerminouObrigatorias()).isEqualTo(DEFAULT_TERMINOU_OBRIGATORIAS);
    }

    @Test
    @Transactional
    public void checkTerminouObrigatoriasIsRequired() throws Exception {
        int databaseSizeBeforeTest = mestrandoRepository.findAll().size();
        // set the field null
        mestrando.setTerminouObrigatorias(null);

        // Create the Mestrando, which fails.

        restMestrandoMockMvc.perform(post("/api/mestrandos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mestrando)))
                .andExpect(status().isBadRequest());

        List<Mestrando> mestrandos = mestrandoRepository.findAll();
        assertThat(mestrandos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMestrandos() throws Exception {
        // Initialize the database
        mestrandoRepository.saveAndFlush(mestrando);

        // Get all the mestrandos
        restMestrandoMockMvc.perform(get("/api/mestrandos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mestrando.getId().intValue())))
                .andExpect(jsonPath("$.[*].terminouObrigatorias").value(hasItem(DEFAULT_TERMINOU_OBRIGATORIAS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMestrando() throws Exception {
        // Initialize the database
        mestrandoRepository.saveAndFlush(mestrando);

        // Get the mestrando
        restMestrandoMockMvc.perform(get("/api/mestrandos/{id}", mestrando.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mestrando.getId().intValue()))
            .andExpect(jsonPath("$.terminouObrigatorias").value(DEFAULT_TERMINOU_OBRIGATORIAS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMestrando() throws Exception {
        // Get the mestrando
        restMestrandoMockMvc.perform(get("/api/mestrandos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMestrando() throws Exception {
        // Initialize the database
        mestrandoRepository.saveAndFlush(mestrando);
        int databaseSizeBeforeUpdate = mestrandoRepository.findAll().size();

        // Update the mestrando
        Mestrando updatedMestrando = mestrandoRepository.findOne(mestrando.getId());
        updatedMestrando
                .terminouObrigatorias(UPDATED_TERMINOU_OBRIGATORIAS);

        restMestrandoMockMvc.perform(put("/api/mestrandos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMestrando)))
                .andExpect(status().isOk());

        // Validate the Mestrando in the database
        List<Mestrando> mestrandos = mestrandoRepository.findAll();
        assertThat(mestrandos).hasSize(databaseSizeBeforeUpdate);
        Mestrando testMestrando = mestrandos.get(mestrandos.size() - 1);
        assertThat(testMestrando.isTerminouObrigatorias()).isEqualTo(UPDATED_TERMINOU_OBRIGATORIAS);
    }

    @Test
    @Transactional
    public void deleteMestrando() throws Exception {
        // Initialize the database
        mestrandoRepository.saveAndFlush(mestrando);
        int databaseSizeBeforeDelete = mestrandoRepository.findAll().size();

        // Get the mestrando
        restMestrandoMockMvc.perform(delete("/api/mestrandos/{id}", mestrando.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mestrando> mestrandos = mestrandoRepository.findAll();
        assertThat(mestrandos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
