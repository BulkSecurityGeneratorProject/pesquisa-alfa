package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Secretaria;
import qs.pesquisaalfa.domain.Usuario;
import qs.pesquisaalfa.repository.SecretariaRepository;

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
 * Test class for the SecretariaResource REST controller.
 *
 * @see SecretariaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class SecretariaResourceIntTest {

    @Inject
    private SecretariaRepository secretariaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSecretariaMockMvc;

    private Secretaria secretaria;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SecretariaResource secretariaResource = new SecretariaResource();
        ReflectionTestUtils.setField(secretariaResource, "secretariaRepository", secretariaRepository);
        this.restSecretariaMockMvc = MockMvcBuilders.standaloneSetup(secretariaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Secretaria createEntity(EntityManager em) {
        Secretaria secretaria = new Secretaria();
        // Add required entity
        Usuario usuario = UsuarioResourceIntTest.createEntity(em);
        em.persist(usuario);
        em.flush();
        secretaria.setUsuario(usuario);
        return secretaria;
    }

    @Before
    public void initTest() {
        secretaria = createEntity(em);
    }

    @Test
    @Transactional
    public void createSecretaria() throws Exception {
        int databaseSizeBeforeCreate = secretariaRepository.findAll().size();

        // Create the Secretaria

        restSecretariaMockMvc.perform(post("/api/secretarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(secretaria)))
                .andExpect(status().isCreated());

        // Validate the Secretaria in the database
        List<Secretaria> secretarias = secretariaRepository.findAll();
        assertThat(secretarias).hasSize(databaseSizeBeforeCreate + 1);
        Secretaria testSecretaria = secretarias.get(secretarias.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSecretarias() throws Exception {
        // Initialize the database
        secretariaRepository.saveAndFlush(secretaria);

        // Get all the secretarias
        restSecretariaMockMvc.perform(get("/api/secretarias?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(secretaria.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSecretaria() throws Exception {
        // Initialize the database
        secretariaRepository.saveAndFlush(secretaria);

        // Get the secretaria
        restSecretariaMockMvc.perform(get("/api/secretarias/{id}", secretaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(secretaria.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSecretaria() throws Exception {
        // Get the secretaria
        restSecretariaMockMvc.perform(get("/api/secretarias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecretaria() throws Exception {
        // Initialize the database
        secretariaRepository.saveAndFlush(secretaria);
        int databaseSizeBeforeUpdate = secretariaRepository.findAll().size();

        // Update the secretaria
        Secretaria updatedSecretaria = secretariaRepository.findOne(secretaria.getId());

        restSecretariaMockMvc.perform(put("/api/secretarias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSecretaria)))
                .andExpect(status().isOk());

        // Validate the Secretaria in the database
        List<Secretaria> secretarias = secretariaRepository.findAll();
        assertThat(secretarias).hasSize(databaseSizeBeforeUpdate);
        Secretaria testSecretaria = secretarias.get(secretarias.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSecretaria() throws Exception {
        // Initialize the database
        secretariaRepository.saveAndFlush(secretaria);
        int databaseSizeBeforeDelete = secretariaRepository.findAll().size();

        // Get the secretaria
        restSecretariaMockMvc.perform(delete("/api/secretarias/{id}", secretaria.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Secretaria> secretarias = secretariaRepository.findAll();
        assertThat(secretarias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
