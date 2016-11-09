package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Aluno;
import qs.pesquisaalfa.domain.Usuario;
import qs.pesquisaalfa.repository.AlunoRepository;

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
 * Test class for the AlunoResource REST controller.
 *
 * @see AlunoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class AlunoResourceIntTest {

    private static final Integer DEFAULT_DRE = 1;
    private static final Integer UPDATED_DRE = 2;

    private static final LocalDate DEFAULT_DATA_MATRICULA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_MATRICULA = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_EH_CANDIDATO = false;
    private static final Boolean UPDATED_EH_CANDIDATO = true;

    @Inject
    private AlunoRepository alunoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAlunoMockMvc;

    private Aluno aluno;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlunoResource alunoResource = new AlunoResource();
        ReflectionTestUtils.setField(alunoResource, "alunoRepository", alunoRepository);
        this.restAlunoMockMvc = MockMvcBuilders.standaloneSetup(alunoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aluno createEntity(EntityManager em) {
        Aluno aluno = new Aluno()
                .dre(DEFAULT_DRE)
                .dataMatricula(DEFAULT_DATA_MATRICULA)
                .ehCandidato(DEFAULT_EH_CANDIDATO);
        // Add required entity
        Usuario usuario = UsuarioResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        aluno.setUsuario(usuario);
        return aluno;
    }

    @Before
    public void initTest() {
        aluno = createEntity(em);
    }

    @Test
    @Transactional
    public void createAluno() throws Exception {
        int databaseSizeBeforeCreate = alunoRepository.findAll().size();

        // Create the Aluno

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isCreated());

        // Validate the Aluno in the database
        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeCreate + 1);
        Aluno testAluno = alunos.get(alunos.size() - 1);
        assertThat(testAluno.getDre()).isEqualTo(DEFAULT_DRE);
        assertThat(testAluno.getDataMatricula()).isEqualTo(DEFAULT_DATA_MATRICULA);
        assertThat(testAluno.isEhCandidato()).isEqualTo(DEFAULT_EH_CANDIDATO);
    }

    @Test
    @Transactional
    public void checkDataMatriculaIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunoRepository.findAll().size();
        // set the field null
        aluno.setDataMatricula(null);

        // Create the Aluno, which fails.

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isBadRequest());

        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEhCandidatoIsRequired() throws Exception {
        int databaseSizeBeforeTest = alunoRepository.findAll().size();
        // set the field null
        aluno.setEhCandidato(null);

        // Create the Aluno, which fails.

        restAlunoMockMvc.perform(post("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(aluno)))
                .andExpect(status().isBadRequest());

        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlunos() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);

        // Get all the alunos
        restAlunoMockMvc.perform(get("/api/alunos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(aluno.getId().intValue())))
                .andExpect(jsonPath("$.[*].dre").value(hasItem(DEFAULT_DRE)))
                .andExpect(jsonPath("$.[*].dataMatricula").value(hasItem(DEFAULT_DATA_MATRICULA.toString())))
                .andExpect(jsonPath("$.[*].ehCandidato").value(hasItem(DEFAULT_EH_CANDIDATO.booleanValue())));
    }

    @Test
    @Transactional
    public void getAluno() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);

        // Get the aluno
        restAlunoMockMvc.perform(get("/api/alunos/{id}", aluno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(aluno.getId().intValue()))
            .andExpect(jsonPath("$.dre").value(DEFAULT_DRE))
            .andExpect(jsonPath("$.dataMatricula").value(DEFAULT_DATA_MATRICULA.toString()))
            .andExpect(jsonPath("$.ehCandidato").value(DEFAULT_EH_CANDIDATO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAluno() throws Exception {
        // Get the aluno
        restAlunoMockMvc.perform(get("/api/alunos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAluno() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);
        int databaseSizeBeforeUpdate = alunoRepository.findAll().size();

        // Update the aluno
        Aluno updatedAluno = alunoRepository.findOne(aluno.getId());
        updatedAluno
                .dre(UPDATED_DRE)
                .dataMatricula(UPDATED_DATA_MATRICULA)
                .ehCandidato(UPDATED_EH_CANDIDATO);

        restAlunoMockMvc.perform(put("/api/alunos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAluno)))
                .andExpect(status().isOk());

        // Validate the Aluno in the database
        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeUpdate);
        Aluno testAluno = alunos.get(alunos.size() - 1);
        assertThat(testAluno.getDre()).isEqualTo(UPDATED_DRE);
        assertThat(testAluno.getDataMatricula()).isEqualTo(UPDATED_DATA_MATRICULA);
        assertThat(testAluno.isEhCandidato()).isEqualTo(UPDATED_EH_CANDIDATO);
    }

    @Test
    @Transactional
    public void deleteAluno() throws Exception {
        // Initialize the database
        alunoRepository.saveAndFlush(aluno);
        int databaseSizeBeforeDelete = alunoRepository.findAll().size();

        // Get the aluno
        restAlunoMockMvc.perform(delete("/api/alunos/{id}", aluno.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Aluno> alunos = alunoRepository.findAll();
        assertThat(alunos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
