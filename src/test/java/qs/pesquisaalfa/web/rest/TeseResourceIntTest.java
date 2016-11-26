package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Tese;
import qs.pesquisaalfa.domain.Proposta;
import qs.pesquisaalfa.repository.TeseRepository;

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

import qs.pesquisaalfa.domain.enumeration.ConceitoPesquisa;
/**
 * Test class for the TeseResource REST controller.
 *
 * @see TeseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class TeseResourceIntTest {

    private static final ConceitoPesquisa DEFAULT_CONCEITO_MEDIO_PESQUISA = ConceitoPesquisa.A;
    private static final ConceitoPesquisa UPDATED_CONCEITO_MEDIO_PESQUISA = ConceitoPesquisa.B;

    @Inject
    private TeseRepository teseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTeseMockMvc;

    private Tese tese;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeseResource teseResource = new TeseResource();
        ReflectionTestUtils.setField(teseResource, "teseRepository", teseRepository);
        this.restTeseMockMvc = MockMvcBuilders.standaloneSetup(teseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tese createEntity(EntityManager em) {
        Tese tese = new Tese()
                .conceitoMedioPesquisa(DEFAULT_CONCEITO_MEDIO_PESQUISA);
        // Add required entity
        Proposta proposta = PropostaResourceIntTest.createEntity(em);
        em.persist(proposta);
        em.flush();
        tese.setProposta(proposta);
        return tese;
    }

    @Before
    public void initTest() {
        tese = createEntity(em);
    }

    @Test
    @Transactional
    public void createTese() throws Exception {
        int databaseSizeBeforeCreate = teseRepository.findAll().size();

        // Create the Tese

        restTeseMockMvc.perform(post("/api/tese")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tese)))
                .andExpect(status().isCreated());

        // Validate the Tese in the database
        List<Tese> tese = teseRepository.findAll();
        assertThat(tese).hasSize(databaseSizeBeforeCreate + 1);
        Tese testTese = tese.get(tese.size() - 1);
        assertThat(testTese.getConceitoMedioPesquisa()).isEqualTo(DEFAULT_CONCEITO_MEDIO_PESQUISA);
    }

    @Test
    @Transactional
    public void checkConceitoMedioPesquisaIsRequired() throws Exception {
        int databaseSizeBeforeTest = teseRepository.findAll().size();
        // set the field null
        tese.setConceitoMedioPesquisa(null);

        // Create the Tese, which fails.

        restTeseMockMvc.perform(post("/api/tese")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tese)))
                .andExpect(status().isBadRequest());

        List<Tese> tese = teseRepository.findAll();
        assertThat(tese).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTese() throws Exception {
        // Initialize the database
        teseRepository.saveAndFlush(tese);

        // Get all the tese
        restTeseMockMvc.perform(get("/api/tese?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tese.getId().intValue())))
                .andExpect(jsonPath("$.[*].conceitoMedioPesquisa").value(hasItem(DEFAULT_CONCEITO_MEDIO_PESQUISA.toString())));
    }

    @Test
    @Transactional
    public void getTese() throws Exception {
        // Initialize the database
        teseRepository.saveAndFlush(tese);

        // Get the tese
        restTeseMockMvc.perform(get("/api/tese/{id}", tese.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tese.getId().intValue()))
            .andExpect(jsonPath("$.conceitoMedioPesquisa").value(DEFAULT_CONCEITO_MEDIO_PESQUISA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTese() throws Exception {
        // Get the tese
        restTeseMockMvc.perform(get("/api/tese/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTese() throws Exception {
        // Initialize the database
        teseRepository.saveAndFlush(tese);
        int databaseSizeBeforeUpdate = teseRepository.findAll().size();

        // Update the tese
        Tese updatedTese = teseRepository.findOne(tese.getId());
        updatedTese
                .conceitoMedioPesquisa(UPDATED_CONCEITO_MEDIO_PESQUISA);

        restTeseMockMvc.perform(put("/api/tese")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTese)))
                .andExpect(status().isOk());

        // Validate the Tese in the database
        List<Tese> tese = teseRepository.findAll();
        assertThat(tese).hasSize(databaseSizeBeforeUpdate);
        Tese testTese = tese.get(tese.size() - 1);
        assertThat(testTese.getConceitoMedioPesquisa()).isEqualTo(UPDATED_CONCEITO_MEDIO_PESQUISA);
    }

    @Test
    @Transactional
    public void deleteTese() throws Exception {
        // Initialize the database
        teseRepository.saveAndFlush(tese);
        int databaseSizeBeforeDelete = teseRepository.findAll().size();

        // Get the tese
        restTeseMockMvc.perform(delete("/api/tese/{id}", tese.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tese> tese = teseRepository.findAll();
        assertThat(tese).hasSize(databaseSizeBeforeDelete - 1);
    }
}
