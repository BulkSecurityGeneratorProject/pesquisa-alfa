package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.ApresentacoesArtigo;
import qs.pesquisaalfa.domain.Artigo;
import qs.pesquisaalfa.repository.ApresentacoesArtigoRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ApresentacoesArtigoResource REST controller.
 *
 * @see ApresentacoesArtigoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class ApresentacoesArtigoResourceIntTest {

    private static final LocalDate DEFAULT_DATA_APRESENTACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_APRESENTACAO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LOCAL = "AAA";
    private static final String UPDATED_LOCAL = "BBB";

    @Inject
    private ApresentacoesArtigoRepository apresentacoesArtigoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restApresentacoesArtigoMockMvc;

    private ApresentacoesArtigo apresentacoesArtigo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApresentacoesArtigoResource apresentacoesArtigoResource = new ApresentacoesArtigoResource();
        ReflectionTestUtils.setField(apresentacoesArtigoResource, "apresentacoesArtigoRepository", apresentacoesArtigoRepository);
        this.restApresentacoesArtigoMockMvc = MockMvcBuilders.standaloneSetup(apresentacoesArtigoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApresentacoesArtigo createEntity(EntityManager em) {
        ApresentacoesArtigo apresentacoesArtigo = new ApresentacoesArtigo()
                .dataApresentacao(DEFAULT_DATA_APRESENTACAO)
                .local(DEFAULT_LOCAL);
        // Add required entity
        Artigo artigo = ArtigoResourceIntTest.createEntity(em);
        em.persist(artigo);
        em.flush();
        apresentacoesArtigo.setArtigo(artigo);
        return apresentacoesArtigo;
    }

    @Before
    public void initTest() {
        apresentacoesArtigo = createEntity(em);
    }

    @Test
    @Transactional
    public void createApresentacoesArtigo() throws Exception {
        int databaseSizeBeforeCreate = apresentacoesArtigoRepository.findAll().size();

        // Create the ApresentacoesArtigo

        restApresentacoesArtigoMockMvc.perform(post("/api/apresentacoes-artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apresentacoesArtigo)))
                .andExpect(status().isCreated());

        // Validate the ApresentacoesArtigo in the database
        List<ApresentacoesArtigo> apresentacoesArtigos = apresentacoesArtigoRepository.findAll();
        assertThat(apresentacoesArtigos).hasSize(databaseSizeBeforeCreate + 1);
        ApresentacoesArtigo testApresentacoesArtigo = apresentacoesArtigos.get(apresentacoesArtigos.size() - 1);
        assertThat(testApresentacoesArtigo.getDataApresentacao()).isEqualTo(DEFAULT_DATA_APRESENTACAO);
        assertThat(testApresentacoesArtigo.getLocal()).isEqualTo(DEFAULT_LOCAL);
    }

    @Test
    @Transactional
    public void checkDataApresentacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = apresentacoesArtigoRepository.findAll().size();
        // set the field null
        apresentacoesArtigo.setDataApresentacao(null);

        // Create the ApresentacoesArtigo, which fails.

        restApresentacoesArtigoMockMvc.perform(post("/api/apresentacoes-artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apresentacoesArtigo)))
                .andExpect(status().isBadRequest());

        List<ApresentacoesArtigo> apresentacoesArtigos = apresentacoesArtigoRepository.findAll();
        assertThat(apresentacoesArtigos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocalIsRequired() throws Exception {
        int databaseSizeBeforeTest = apresentacoesArtigoRepository.findAll().size();
        // set the field null
        apresentacoesArtigo.setLocal(null);

        // Create the ApresentacoesArtigo, which fails.

        restApresentacoesArtigoMockMvc.perform(post("/api/apresentacoes-artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(apresentacoesArtigo)))
                .andExpect(status().isBadRequest());

        List<ApresentacoesArtigo> apresentacoesArtigos = apresentacoesArtigoRepository.findAll();
        assertThat(apresentacoesArtigos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApresentacoesArtigos() throws Exception {
        // Initialize the database
        apresentacoesArtigoRepository.saveAndFlush(apresentacoesArtigo);

        // Get all the apresentacoesArtigos
        restApresentacoesArtigoMockMvc.perform(get("/api/apresentacoes-artigos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(apresentacoesArtigo.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataApresentacao").value(hasItem(DEFAULT_DATA_APRESENTACAO.toString())))
                .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL.toString())));
    }

    @Test
    @Transactional
    public void getApresentacoesArtigo() throws Exception {
        // Initialize the database
        apresentacoesArtigoRepository.saveAndFlush(apresentacoesArtigo);

        // Get the apresentacoesArtigo
        restApresentacoesArtigoMockMvc.perform(get("/api/apresentacoes-artigos/{id}", apresentacoesArtigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apresentacoesArtigo.getId().intValue()))
            .andExpect(jsonPath("$.dataApresentacao").value(DEFAULT_DATA_APRESENTACAO.toString()))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApresentacoesArtigo() throws Exception {
        // Get the apresentacoesArtigo
        restApresentacoesArtigoMockMvc.perform(get("/api/apresentacoes-artigos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApresentacoesArtigo() throws Exception {
        // Initialize the database
        apresentacoesArtigoRepository.saveAndFlush(apresentacoesArtigo);
        int databaseSizeBeforeUpdate = apresentacoesArtigoRepository.findAll().size();

        // Update the apresentacoesArtigo
        ApresentacoesArtigo updatedApresentacoesArtigo = apresentacoesArtigoRepository.findOne(apresentacoesArtigo.getId());
        updatedApresentacoesArtigo
                .dataApresentacao(UPDATED_DATA_APRESENTACAO)
                .local(UPDATED_LOCAL);

        restApresentacoesArtigoMockMvc.perform(put("/api/apresentacoes-artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedApresentacoesArtigo)))
                .andExpect(status().isOk());

        // Validate the ApresentacoesArtigo in the database
        List<ApresentacoesArtigo> apresentacoesArtigos = apresentacoesArtigoRepository.findAll();
        assertThat(apresentacoesArtigos).hasSize(databaseSizeBeforeUpdate);
        ApresentacoesArtigo testApresentacoesArtigo = apresentacoesArtigos.get(apresentacoesArtigos.size() - 1);
        assertThat(testApresentacoesArtigo.getDataApresentacao()).isEqualTo(UPDATED_DATA_APRESENTACAO);
        assertThat(testApresentacoesArtigo.getLocal()).isEqualTo(UPDATED_LOCAL);
    }

    @Test
    @Transactional
    public void deleteApresentacoesArtigo() throws Exception {
        // Initialize the database
        apresentacoesArtigoRepository.saveAndFlush(apresentacoesArtigo);
        int databaseSizeBeforeDelete = apresentacoesArtigoRepository.findAll().size();

        // Get the apresentacoesArtigo
        restApresentacoesArtigoMockMvc.perform(delete("/api/apresentacoes-artigos/{id}", apresentacoesArtigo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ApresentacoesArtigo> apresentacoesArtigos = apresentacoesArtigoRepository.findAll();
        assertThat(apresentacoesArtigos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
