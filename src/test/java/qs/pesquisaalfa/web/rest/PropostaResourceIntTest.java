package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Proposta;
import qs.pesquisaalfa.domain.Aluno;
import qs.pesquisaalfa.domain.Orientador;
import qs.pesquisaalfa.repository.PropostaRepository;

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
 * Test class for the PropostaResource REST controller.
 *
 * @see PropostaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class PropostaResourceIntTest {

    private static final Boolean DEFAULT_PROPOSTA_ACEITA = false;
    private static final Boolean UPDATED_PROPOSTA_ACEITA = true;

    private static final String DEFAULT_TEMA = "AAAAA";
    private static final String UPDATED_TEMA = "BBBBB";

    @Inject
    private PropostaRepository propostaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPropostaMockMvc;

    private Proposta proposta;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PropostaResource propostaResource = new PropostaResource();
        ReflectionTestUtils.setField(propostaResource, "propostaRepository", propostaRepository);
        this.restPropostaMockMvc = MockMvcBuilders.standaloneSetup(propostaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposta createEntity(EntityManager em) {
        Proposta proposta = new Proposta()
                .propostaAceita(DEFAULT_PROPOSTA_ACEITA)
                .tema(DEFAULT_TEMA);
        // Add required entity
        Aluno aluno = AlunoResourceIntTest.createEntity(em);
        em.persist(aluno);
        em.flush();
        proposta.setAluno(aluno);
        // Add required entity
        Orientador orientador = OrientadorResourceIntTest.createEntity(em);
        em.persist(orientador);
        em.flush();
        proposta.setOrientador(orientador);
        return proposta;
    }

    @Before
    public void initTest() {
        proposta = createEntity(em);
    }

    @Test
    @Transactional
    public void createProposta() throws Exception {
        int databaseSizeBeforeCreate = propostaRepository.findAll().size();

        // Create the Proposta

        restPropostaMockMvc.perform(post("/api/propostas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proposta)))
                .andExpect(status().isCreated());

        // Validate the Proposta in the database
        List<Proposta> propostas = propostaRepository.findAll();
        assertThat(propostas).hasSize(databaseSizeBeforeCreate + 1);
        Proposta testProposta = propostas.get(propostas.size() - 1);
        assertThat(testProposta.isPropostaAceita()).isEqualTo(DEFAULT_PROPOSTA_ACEITA);
        assertThat(testProposta.getTema()).isEqualTo(DEFAULT_TEMA);
    }

    @Test
    @Transactional
    public void checkPropostaAceitaIsRequired() throws Exception {
        int databaseSizeBeforeTest = propostaRepository.findAll().size();
        // set the field null
        proposta.setPropostaAceita(null);

        // Create the Proposta, which fails.

        restPropostaMockMvc.perform(post("/api/propostas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proposta)))
                .andExpect(status().isBadRequest());

        List<Proposta> propostas = propostaRepository.findAll();
        assertThat(propostas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTemaIsRequired() throws Exception {
        int databaseSizeBeforeTest = propostaRepository.findAll().size();
        // set the field null
        proposta.setTema(null);

        // Create the Proposta, which fails.

        restPropostaMockMvc.perform(post("/api/propostas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proposta)))
                .andExpect(status().isBadRequest());

        List<Proposta> propostas = propostaRepository.findAll();
        assertThat(propostas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPropostas() throws Exception {
        // Initialize the database
        propostaRepository.saveAndFlush(proposta);

        // Get all the propostas
        restPropostaMockMvc.perform(get("/api/propostas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(proposta.getId().intValue())))
                .andExpect(jsonPath("$.[*].propostaAceita").value(hasItem(DEFAULT_PROPOSTA_ACEITA.booleanValue())))
                .andExpect(jsonPath("$.[*].tema").value(hasItem(DEFAULT_TEMA.toString())));
    }

    @Test
    @Transactional
    public void getProposta() throws Exception {
        // Initialize the database
        propostaRepository.saveAndFlush(proposta);

        // Get the proposta
        restPropostaMockMvc.perform(get("/api/propostas/{id}", proposta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(proposta.getId().intValue()))
            .andExpect(jsonPath("$.propostaAceita").value(DEFAULT_PROPOSTA_ACEITA.booleanValue()))
            .andExpect(jsonPath("$.tema").value(DEFAULT_TEMA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProposta() throws Exception {
        // Get the proposta
        restPropostaMockMvc.perform(get("/api/propostas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProposta() throws Exception {
        // Initialize the database
        propostaRepository.saveAndFlush(proposta);
        int databaseSizeBeforeUpdate = propostaRepository.findAll().size();

        // Update the proposta
        Proposta updatedProposta = propostaRepository.findOne(proposta.getId());
        updatedProposta
                .propostaAceita(UPDATED_PROPOSTA_ACEITA)
                .tema(UPDATED_TEMA);

        restPropostaMockMvc.perform(put("/api/propostas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProposta)))
                .andExpect(status().isOk());

        // Validate the Proposta in the database
        List<Proposta> propostas = propostaRepository.findAll();
        assertThat(propostas).hasSize(databaseSizeBeforeUpdate);
        Proposta testProposta = propostas.get(propostas.size() - 1);
        assertThat(testProposta.isPropostaAceita()).isEqualTo(UPDATED_PROPOSTA_ACEITA);
        assertThat(testProposta.getTema()).isEqualTo(UPDATED_TEMA);
    }

    @Test
    @Transactional
    public void deleteProposta() throws Exception {
        // Initialize the database
        propostaRepository.saveAndFlush(proposta);
        int databaseSizeBeforeDelete = propostaRepository.findAll().size();

        // Get the proposta
        restPropostaMockMvc.perform(delete("/api/propostas/{id}", proposta.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Proposta> propostas = propostaRepository.findAll();
        assertThat(propostas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
