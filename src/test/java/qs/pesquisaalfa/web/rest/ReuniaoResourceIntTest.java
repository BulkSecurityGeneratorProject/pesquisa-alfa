package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Reuniao;
import qs.pesquisaalfa.domain.Aluno;
import qs.pesquisaalfa.domain.Orientador;
import qs.pesquisaalfa.repository.ReuniaoRepository;

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

/**
 * Test class for the ReuniaoResource REST controller.
 *
 * @see ReuniaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class ReuniaoResourceIntTest {

    private static final ZonedDateTime DEFAULT_HORARIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HORARIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HORARIO_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_HORARIO);

    private static final String DEFAULT_ASSUNTO = "AAAAAAAAAA";
    private static final String UPDATED_ASSUNTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REUNIAO_APROVADA = false;
    private static final Boolean UPDATED_REUNIAO_APROVADA = true;

    @Inject
    private ReuniaoRepository reuniaoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restReuniaoMockMvc;

    private Reuniao reuniao;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReuniaoResource reuniaoResource = new ReuniaoResource();
        ReflectionTestUtils.setField(reuniaoResource, "reuniaoRepository", reuniaoRepository);
        this.restReuniaoMockMvc = MockMvcBuilders.standaloneSetup(reuniaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reuniao createEntity(EntityManager em) {
        Reuniao reuniao = new Reuniao()
                .horario(DEFAULT_HORARIO)
                .assunto(DEFAULT_ASSUNTO)
                .reuniaoAprovada(DEFAULT_REUNIAO_APROVADA);
        // Add required entity
        Aluno aluno = AlunoResourceIntTest.createEntity(em);
        em.persist(aluno);
        em.flush();
        reuniao.setAluno(aluno);
        // Add required entity
        Orientador orientador = OrientadorResourceIntTest.createEntity(em);
        em.persist(orientador);
        em.flush();
        reuniao.setOrientador(orientador);
        return reuniao;
    }

    @Before
    public void initTest() {
        reuniao = createEntity(em);
    }

    @Test
    @Transactional
    public void createReuniao() throws Exception {
        int databaseSizeBeforeCreate = reuniaoRepository.findAll().size();

        // Create the Reuniao

        restReuniaoMockMvc.perform(post("/api/reuniaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reuniao)))
                .andExpect(status().isCreated());

        // Validate the Reuniao in the database
        List<Reuniao> reuniaos = reuniaoRepository.findAll();
        assertThat(reuniaos).hasSize(databaseSizeBeforeCreate + 1);
        Reuniao testReuniao = reuniaos.get(reuniaos.size() - 1);
        assertThat(testReuniao.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testReuniao.getAssunto()).isEqualTo(DEFAULT_ASSUNTO);
        assertThat(testReuniao.isReuniaoAprovada()).isEqualTo(DEFAULT_REUNIAO_APROVADA);
    }

    @Test
    @Transactional
    public void checkHorarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = reuniaoRepository.findAll().size();
        // set the field null
        reuniao.setHorario(null);

        // Create the Reuniao, which fails.

        restReuniaoMockMvc.perform(post("/api/reuniaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reuniao)))
                .andExpect(status().isBadRequest());

        List<Reuniao> reuniaos = reuniaoRepository.findAll();
        assertThat(reuniaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAssuntoIsRequired() throws Exception {
        int databaseSizeBeforeTest = reuniaoRepository.findAll().size();
        // set the field null
        reuniao.setAssunto(null);

        // Create the Reuniao, which fails.

        restReuniaoMockMvc.perform(post("/api/reuniaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reuniao)))
                .andExpect(status().isBadRequest());

        List<Reuniao> reuniaos = reuniaoRepository.findAll();
        assertThat(reuniaos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReuniaos() throws Exception {
        // Initialize the database
        reuniaoRepository.saveAndFlush(reuniao);

        // Get all the reuniaos
        restReuniaoMockMvc.perform(get("/api/reuniaos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(reuniao.getId().intValue())))
                .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO_STR)))
                .andExpect(jsonPath("$.[*].assunto").value(hasItem(DEFAULT_ASSUNTO.toString())))
                .andExpect(jsonPath("$.[*].reuniaoAprovada").value(hasItem(DEFAULT_REUNIAO_APROVADA.booleanValue())));
    }

    @Test
    @Transactional
    public void getReuniao() throws Exception {
        // Initialize the database
        reuniaoRepository.saveAndFlush(reuniao);

        // Get the reuniao
        restReuniaoMockMvc.perform(get("/api/reuniaos/{id}", reuniao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reuniao.getId().intValue()))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO_STR))
            .andExpect(jsonPath("$.assunto").value(DEFAULT_ASSUNTO.toString()))
            .andExpect(jsonPath("$.reuniaoAprovada").value(DEFAULT_REUNIAO_APROVADA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReuniao() throws Exception {
        // Get the reuniao
        restReuniaoMockMvc.perform(get("/api/reuniaos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReuniao() throws Exception {
        // Initialize the database
        reuniaoRepository.saveAndFlush(reuniao);
        int databaseSizeBeforeUpdate = reuniaoRepository.findAll().size();

        // Update the reuniao
        Reuniao updatedReuniao = reuniaoRepository.findOne(reuniao.getId());
        updatedReuniao
                .horario(UPDATED_HORARIO)
                .assunto(UPDATED_ASSUNTO)
                .reuniaoAprovada(UPDATED_REUNIAO_APROVADA);

        restReuniaoMockMvc.perform(put("/api/reuniaos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReuniao)))
                .andExpect(status().isOk());

        // Validate the Reuniao in the database
        List<Reuniao> reuniaos = reuniaoRepository.findAll();
        assertThat(reuniaos).hasSize(databaseSizeBeforeUpdate);
        Reuniao testReuniao = reuniaos.get(reuniaos.size() - 1);
        assertThat(testReuniao.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testReuniao.getAssunto()).isEqualTo(UPDATED_ASSUNTO);
        assertThat(testReuniao.isReuniaoAprovada()).isEqualTo(UPDATED_REUNIAO_APROVADA);
    }

    @Test
    @Transactional
    public void deleteReuniao() throws Exception {
        // Initialize the database
        reuniaoRepository.saveAndFlush(reuniao);
        int databaseSizeBeforeDelete = reuniaoRepository.findAll().size();

        // Get the reuniao
        restReuniaoMockMvc.perform(delete("/api/reuniaos/{id}", reuniao.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Reuniao> reuniaos = reuniaoRepository.findAll();
        assertThat(reuniaos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
