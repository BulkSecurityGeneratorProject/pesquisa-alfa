package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Artigo;
import qs.pesquisaalfa.domain.Aluno;
import qs.pesquisaalfa.repository.ArtigoRepository;

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
 * Test class for the ArtigoResource REST controller.
 *
 * @see ArtigoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class ArtigoResourceIntTest {

    private static final String DEFAULT_TITULO = "AAA";
    private static final String UPDATED_TITULO = "BBB";

    @Inject
    private ArtigoRepository artigoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restArtigoMockMvc;

    private Artigo artigo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ArtigoResource artigoResource = new ArtigoResource();
        ReflectionTestUtils.setField(artigoResource, "artigoRepository", artigoRepository);
        this.restArtigoMockMvc = MockMvcBuilders.standaloneSetup(artigoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artigo createEntity(EntityManager em) {
        Artigo artigo = new Artigo()
                .titulo(DEFAULT_TITULO);
        // Add required entity
        Aluno alunos = AlunoResourceIntTest.createEntity(em);
        em.persist(alunos);
        em.flush();
        artigo.getAlunos().add(alunos);
        return artigo;
    }

    @Before
    public void initTest() {
        artigo = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtigo() throws Exception {
        int databaseSizeBeforeCreate = artigoRepository.findAll().size();

        // Create the Artigo

        restArtigoMockMvc.perform(post("/api/artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artigo)))
                .andExpect(status().isCreated());

        // Validate the Artigo in the database
        List<Artigo> artigos = artigoRepository.findAll();
        assertThat(artigos).hasSize(databaseSizeBeforeCreate + 1);
        Artigo testArtigo = artigos.get(artigos.size() - 1);
        assertThat(testArtigo.getTitulo()).isEqualTo(DEFAULT_TITULO);
    }

    @Test
    @Transactional
    public void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = artigoRepository.findAll().size();
        // set the field null
        artigo.setTitulo(null);

        // Create the Artigo, which fails.

        restArtigoMockMvc.perform(post("/api/artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artigo)))
                .andExpect(status().isBadRequest());

        List<Artigo> artigos = artigoRepository.findAll();
        assertThat(artigos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtigos() throws Exception {
        // Initialize the database
        artigoRepository.saveAndFlush(artigo);

        // Get all the artigos
        restArtigoMockMvc.perform(get("/api/artigos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(artigo.getId().intValue())))
                .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO.toString())));
    }

    @Test
    @Transactional
    public void getArtigo() throws Exception {
        // Initialize the database
        artigoRepository.saveAndFlush(artigo);

        // Get the artigo
        restArtigoMockMvc.perform(get("/api/artigos/{id}", artigo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artigo.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtigo() throws Exception {
        // Get the artigo
        restArtigoMockMvc.perform(get("/api/artigos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtigo() throws Exception {
        // Initialize the database
        artigoRepository.saveAndFlush(artigo);
        int databaseSizeBeforeUpdate = artigoRepository.findAll().size();

        // Update the artigo
        Artigo updatedArtigo = artigoRepository.findOne(artigo.getId());
        updatedArtigo
                .titulo(UPDATED_TITULO);

        restArtigoMockMvc.perform(put("/api/artigos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedArtigo)))
                .andExpect(status().isOk());

        // Validate the Artigo in the database
        List<Artigo> artigos = artigoRepository.findAll();
        assertThat(artigos).hasSize(databaseSizeBeforeUpdate);
        Artigo testArtigo = artigos.get(artigos.size() - 1);
        assertThat(testArtigo.getTitulo()).isEqualTo(UPDATED_TITULO);
    }

    @Test
    @Transactional
    public void deleteArtigo() throws Exception {
        // Initialize the database
        artigoRepository.saveAndFlush(artigo);
        int databaseSizeBeforeDelete = artigoRepository.findAll().size();

        // Get the artigo
        restArtigoMockMvc.perform(delete("/api/artigos/{id}", artigo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Artigo> artigos = artigoRepository.findAll();
        assertThat(artigos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
