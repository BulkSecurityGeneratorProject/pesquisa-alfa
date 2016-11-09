package qs.pesquisaalfa.web.rest;

import qs.pesquisaalfa.PesquisaalfaApp;

import qs.pesquisaalfa.domain.Usuario;
import qs.pesquisaalfa.repository.UsuarioRepository;

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
 * Test class for the UsuarioResource REST controller.
 *
 * @see UsuarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PesquisaalfaApp.class)
public class UsuarioResourceIntTest {

    private static final String DEFAULT_NOME = "AAA";
    private static final String UPDATED_NOME = "BBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final String DEFAULT_USERNAME = "AAA";
    private static final String UPDATED_USERNAME = "BBB";

    private static final String DEFAULT_SENHA = "AAAAA";
    private static final String UPDATED_SENHA = "BBBBB";

    @Inject
    private UsuarioRepository usuarioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UsuarioResource usuarioResource = new UsuarioResource();
        ReflectionTestUtils.setField(usuarioResource, "usuarioRepository", usuarioRepository);
        this.restUsuarioMockMvc = MockMvcBuilders.standaloneSetup(usuarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
                .nome(DEFAULT_NOME)
                .email(DEFAULT_EMAIL)
                .username(DEFAULT_USERNAME)
                .senha(DEFAULT_SENHA);
        return usuario;
    }

    @Before
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // Create the Usuario

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuario)))
                .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarios.get(usuarios.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testUsuario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsuario.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testUsuario.getSenha()).isEqualTo(DEFAULT_SENHA);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setNome(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuario)))
                .andExpect(status().isBadRequest());

        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setEmail(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuario)))
                .andExpect(status().isBadRequest());

        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setUsername(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuario)))
                .andExpect(status().isBadRequest());

        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenhaIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setSenha(null);

        // Create the Usuario, which fails.

        restUsuarioMockMvc.perform(post("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(usuario)))
                .andExpect(status().isBadRequest());

        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarios
        restUsuarioMockMvc.perform(get("/api/usuarios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())));
    }

    @Test
    @Transactional
    public void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findOne(usuario.getId());
        updatedUsuario
                .nome(UPDATED_NOME)
                .email(UPDATED_EMAIL)
                .username(UPDATED_USERNAME)
                .senha(UPDATED_SENHA);

        restUsuarioMockMvc.perform(put("/api/usuarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUsuario)))
                .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarios.get(usuarios.size() - 1);
        assertThat(testUsuario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUsuario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsuario.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testUsuario.getSenha()).isEqualTo(UPDATED_SENHA);
    }

    @Test
    @Transactional
    public void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Get the usuario
        restUsuarioMockMvc.perform(delete("/api/usuarios/{id}", usuario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Usuario> usuarios = usuarioRepository.findAll();
        assertThat(usuarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
