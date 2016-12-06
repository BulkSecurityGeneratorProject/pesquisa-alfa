package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.BancaAvaliacao;
import qs.pesquisaalfa.domain.Professor;
import qs.pesquisaalfa.repository.BancaAvaliacaoRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import qs.pesquisaalfa.domain.enumeration.TiposAvaliacao;
/**
 * Test class for the BancaAvaliacaoResource REST controller.
 *
 * @see BancaAvaliacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class BancaAvaliacaoResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATA_HORA_APRESENTACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATA_HORA_APRESENTACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATA_HORA_APRESENTACAO_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_DATA_HORA_APRESENTACAO);

    private static final TiposAvaliacao DEFAULT_TIPO_AVALIACAO = TiposAvaliacao.PROPOSTA;
    private static final TiposAvaliacao UPDATED_TIPO_AVALIACAO = TiposAvaliacao.TESE;

    @Inject
    private BancaAvaliacaoRepository bancaAvaliacaoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBancaAvaliacaoMockMvc;

    private BancaAvaliacao bancaAvaliacao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BancaAvaliacaoResource bancaAvaliacaoResource = new BancaAvaliacaoResource();
        ReflectionTestUtils.setField(bancaAvaliacaoResource, "bancaAvaliacaoRepository", bancaAvaliacaoRepository);
        this.restBancaAvaliacaoMockMvc = MockMvcBuilders.standaloneSetup(bancaAvaliacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BancaAvaliacao createEntity(EntityManager em) {
        BancaAvaliacao bancaAvaliacao = new BancaAvaliacao()
                .dataHoraApresentacao(DEFAULT_DATA_HORA_APRESENTACAO)
                .tipoAvaliacao(DEFAULT_TIPO_AVALIACAO);
        // Add required entity
        Professor professores = ProfessorResourceIntTest.createEntity(em);
        em.persist(professores);
        em.flush();
        bancaAvaliacao.getProfessores().add(professores);
        return bancaAvaliacao;
    }

    @Before
    public void initTest() {
        bancaAvaliacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createBancaAvaliacao() throws Exception {
        int databaseSizeBeforeCreate = bancaAvaliacaoRepository.findAll().size();

        // Create the BancaAvaliacao

        restBancaAvaliacaoMockMvc.perform(post("/api/banca-avaliacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bancaAvaliacao)))
                .andExpect(status().isCreated());

        // Validate the BancaAvaliacao in the database
        List<BancaAvaliacao> bancaAvaliacaos = bancaAvaliacaoRepository.findAll();
        assertThat(bancaAvaliacaos).hasSize(databaseSizeBeforeCreate + 1);
        BancaAvaliacao testBancaAvaliacao = bancaAvaliacaos.get(bancaAvaliacaos.size() - 1);
        assertThat(testBancaAvaliacao.getDataHoraApresentacao()).isEqualTo(DEFAULT_DATA_HORA_APRESENTACAO);
        assertThat(testBancaAvaliacao.getTipoAvaliacao()).isEqualTo(DEFAULT_TIPO_AVALIACAO);
    }

    @Test
    @Transactional
    public void checkDataHoraApresentacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancaAvaliacaoRepository.findAll().size();
        // set the field null
        bancaAvaliacao.setDataHoraApresentacao(null);

        // Create the BancaAvaliacao, which fails.

        restBancaAvaliacaoMockMvc.perform(post("/api/banca-avaliacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bancaAvaliacao)))
                .andExpect(status().isBadRequest());

        List<BancaAvaliacao> bancaAvaliacaos = bancaAvaliacaoRepository.findAll();
        assertThat(bancaAvaliacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoAvaliacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bancaAvaliacaoRepository.findAll().size();
        // set the field null
        bancaAvaliacao.setTipoAvaliacao(null);

        // Create the BancaAvaliacao, which fails.

        restBancaAvaliacaoMockMvc.perform(post("/api/banca-avaliacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bancaAvaliacao)))
                .andExpect(status().isBadRequest());

        List<BancaAvaliacao> bancaAvaliacaos = bancaAvaliacaoRepository.findAll();
        assertThat(bancaAvaliacaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBancaAvaliacaos() throws Exception {
        // Initialize the database
        bancaAvaliacaoRepository.saveAndFlush(bancaAvaliacao);

        // Get all the bancaAvaliacaos
        restBancaAvaliacaoMockMvc.perform(get("/api/banca-avaliacaos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bancaAvaliacao.getId().intValue())))
                .andExpect(jsonPath("$.[*].dataHoraApresentacao").value(hasItem(DEFAULT_DATA_HORA_APRESENTACAO_STR)))
                .andExpect(jsonPath("$.[*].tipoAvaliacao").value(hasItem(DEFAULT_TIPO_AVALIACAO.toString())));
    }

    @Test
    @Transactional
    public void getBancaAvaliacao() throws Exception {
        // Initialize the database
        bancaAvaliacaoRepository.saveAndFlush(bancaAvaliacao);

        // Get the bancaAvaliacao
        restBancaAvaliacaoMockMvc.perform(get("/api/banca-avaliacaos/{id}", bancaAvaliacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bancaAvaliacao.getId().intValue()))
            .andExpect(jsonPath("$.dataHoraApresentacao").value(DEFAULT_DATA_HORA_APRESENTACAO_STR))
            .andExpect(jsonPath("$.tipoAvaliacao").value(DEFAULT_TIPO_AVALIACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBancaAvaliacao() throws Exception {
        // Get the bancaAvaliacao
        restBancaAvaliacaoMockMvc.perform(get("/api/banca-avaliacaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBancaAvaliacao() throws Exception {
        // Initialize the database
        bancaAvaliacaoRepository.saveAndFlush(bancaAvaliacao);
        int databaseSizeBeforeUpdate = bancaAvaliacaoRepository.findAll().size();

        // Update the bancaAvaliacao
        BancaAvaliacao updatedBancaAvaliacao = bancaAvaliacaoRepository.findOne(bancaAvaliacao.getId());
        updatedBancaAvaliacao
                .dataHoraApresentacao(UPDATED_DATA_HORA_APRESENTACAO)
                .tipoAvaliacao(UPDATED_TIPO_AVALIACAO);

        restBancaAvaliacaoMockMvc.perform(put("/api/banca-avaliacaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBancaAvaliacao)))
                .andExpect(status().isOk());

        // Validate the BancaAvaliacao in the database
        List<BancaAvaliacao> bancaAvaliacaos = bancaAvaliacaoRepository.findAll();
        assertThat(bancaAvaliacaos).hasSize(databaseSizeBeforeUpdate);
        BancaAvaliacao testBancaAvaliacao = bancaAvaliacaos.get(bancaAvaliacaos.size() - 1);
        assertThat(testBancaAvaliacao.getDataHoraApresentacao()).isEqualTo(UPDATED_DATA_HORA_APRESENTACAO);
        assertThat(testBancaAvaliacao.getTipoAvaliacao()).isEqualTo(UPDATED_TIPO_AVALIACAO);
    }

    @Test
    @Transactional
    public void deleteBancaAvaliacao() throws Exception {
        // Initialize the database
        bancaAvaliacaoRepository.saveAndFlush(bancaAvaliacao);
        int databaseSizeBeforeDelete = bancaAvaliacaoRepository.findAll().size();

        // Get the bancaAvaliacao
        restBancaAvaliacaoMockMvc.perform(delete("/api/banca-avaliacaos/{id}", bancaAvaliacao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BancaAvaliacao> bancaAvaliacaos = bancaAvaliacaoRepository.findAll();
        assertThat(bancaAvaliacaos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
