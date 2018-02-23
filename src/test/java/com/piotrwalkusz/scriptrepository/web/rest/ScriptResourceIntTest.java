package com.piotrwalkusz.scriptrepository.web.rest;

import com.piotrwalkusz.scriptrepository.ScriptRepositoryApp;

import com.piotrwalkusz.scriptrepository.domain.Script;
import com.piotrwalkusz.scriptrepository.domain.Collection;
import com.piotrwalkusz.scriptrepository.repository.ScriptRepository;
import com.piotrwalkusz.scriptrepository.repository.search.ScriptSearchRepository;
import com.piotrwalkusz.scriptrepository.service.dto.ScriptDTO;
import com.piotrwalkusz.scriptrepository.service.mapper.ScriptMapper;
import com.piotrwalkusz.scriptrepository.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.piotrwalkusz.scriptrepository.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.piotrwalkusz.scriptrepository.domain.enumeration.ScriptLanguage;
/**
 * Test class for the ScriptResource REST controller.
 *
 * @see ScriptResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptRepositoryApp.class)
public class ScriptResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ScriptLanguage DEFAULT_SCRIPT_LANGUAGE = ScriptLanguage.BASH;
    private static final ScriptLanguage UPDATED_SCRIPT_LANGUAGE = ScriptLanguage.PYTHON_2;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOWNLOAD_COUNT = 1;
    private static final Integer UPDATED_DOWNLOAD_COUNT = 2;

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ScriptMapper scriptMapper;

    @Autowired
    private ScriptSearchRepository scriptSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScriptMockMvc;

    private Script script;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScriptResource scriptResource = new ScriptResource(scriptRepository, scriptMapper, scriptSearchRepository);
        this.restScriptMockMvc = MockMvcBuilders.standaloneSetup(scriptResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Script createEntity(EntityManager em) {
        Script script = new Script()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .scriptLanguage(DEFAULT_SCRIPT_LANGUAGE)
            .code(DEFAULT_CODE)
            .downloadCount(DEFAULT_DOWNLOAD_COUNT);
        // Add required entity
        Collection collection = CollectionResourceIntTest.createEntity(em);
        em.persist(collection);
        em.flush();
        script.setCollection(collection);
        return script;
    }

    @Before
    public void initTest() {
        scriptSearchRepository.deleteAll();
        script = createEntity(em);
    }

    @Test
    @Transactional
    public void createScript() throws Exception {
        int databaseSizeBeforeCreate = scriptRepository.findAll().size();

        // Create the Script
        ScriptDTO scriptDTO = scriptMapper.toDto(script);
        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isCreated());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeCreate + 1);
        Script testScript = scriptList.get(scriptList.size() - 1);
        assertThat(testScript.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScript.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testScript.getScriptLanguage()).isEqualTo(DEFAULT_SCRIPT_LANGUAGE);
        assertThat(testScript.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testScript.getDownloadCount()).isEqualTo(DEFAULT_DOWNLOAD_COUNT);

        // Validate the Script in Elasticsearch
        Script scriptEs = scriptSearchRepository.findOne(testScript.getId());
        assertThat(scriptEs).isEqualToIgnoringGivenFields(testScript);
    }

    @Test
    @Transactional
    public void createScriptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scriptRepository.findAll().size();

        // Create the Script with an existing ID
        script.setId(1L);
        ScriptDTO scriptDTO = scriptMapper.toDto(script);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptRepository.findAll().size();
        // set the field null
        script.setName(null);

        // Create the Script, which fails.
        ScriptDTO scriptDTO = scriptMapper.toDto(script);

        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isBadRequest());

        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkScriptLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptRepository.findAll().size();
        // set the field null
        script.setScriptLanguage(null);

        // Create the Script, which fails.
        ScriptDTO scriptDTO = scriptMapper.toDto(script);

        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isBadRequest());

        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScripts() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);

        // Get all the scriptList
        restScriptMockMvc.perform(get("/api/scripts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(script.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].scriptLanguage").value(hasItem(DEFAULT_SCRIPT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].downloadCount").value(hasItem(DEFAULT_DOWNLOAD_COUNT)));
    }

    @Test
    @Transactional
    public void getScript() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);

        // Get the script
        restScriptMockMvc.perform(get("/api/scripts/{id}", script.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(script.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.scriptLanguage").value(DEFAULT_SCRIPT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.downloadCount").value(DEFAULT_DOWNLOAD_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingScript() throws Exception {
        // Get the script
        restScriptMockMvc.perform(get("/api/scripts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScript() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);
        scriptSearchRepository.save(script);
        int databaseSizeBeforeUpdate = scriptRepository.findAll().size();

        // Update the script
        Script updatedScript = scriptRepository.findOne(script.getId());
        // Disconnect from session so that the updates on updatedScript are not directly saved in db
        em.detach(updatedScript);
        updatedScript
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .scriptLanguage(UPDATED_SCRIPT_LANGUAGE)
            .code(UPDATED_CODE)
            .downloadCount(UPDATED_DOWNLOAD_COUNT);
        ScriptDTO scriptDTO = scriptMapper.toDto(updatedScript);

        restScriptMockMvc.perform(put("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isOk());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeUpdate);
        Script testScript = scriptList.get(scriptList.size() - 1);
        assertThat(testScript.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScript.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testScript.getScriptLanguage()).isEqualTo(UPDATED_SCRIPT_LANGUAGE);
        assertThat(testScript.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testScript.getDownloadCount()).isEqualTo(UPDATED_DOWNLOAD_COUNT);

        // Validate the Script in Elasticsearch
        Script scriptEs = scriptSearchRepository.findOne(testScript.getId());
        assertThat(scriptEs).isEqualToIgnoringGivenFields(testScript);
    }

    @Test
    @Transactional
    public void updateNonExistingScript() throws Exception {
        int databaseSizeBeforeUpdate = scriptRepository.findAll().size();

        // Create the Script
        ScriptDTO scriptDTO = scriptMapper.toDto(script);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScriptMockMvc.perform(put("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isCreated());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteScript() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);
        scriptSearchRepository.save(script);
        int databaseSizeBeforeDelete = scriptRepository.findAll().size();

        // Get the script
        restScriptMockMvc.perform(delete("/api/scripts/{id}", script.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean scriptExistsInEs = scriptSearchRepository.exists(script.getId());
        assertThat(scriptExistsInEs).isFalse();

        // Validate the database is empty
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchScript() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);
        scriptSearchRepository.save(script);

        // Search the script
        restScriptMockMvc.perform(get("/api/_search/scripts?query=id:" + script.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(script.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].scriptLanguage").value(hasItem(DEFAULT_SCRIPT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].downloadCount").value(hasItem(DEFAULT_DOWNLOAD_COUNT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Script.class);
        Script script1 = new Script();
        script1.setId(1L);
        Script script2 = new Script();
        script2.setId(script1.getId());
        assertThat(script1).isEqualTo(script2);
        script2.setId(2L);
        assertThat(script1).isNotEqualTo(script2);
        script1.setId(null);
        assertThat(script1).isNotEqualTo(script2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScriptDTO.class);
        ScriptDTO scriptDTO1 = new ScriptDTO();
        scriptDTO1.setId(1L);
        ScriptDTO scriptDTO2 = new ScriptDTO();
        assertThat(scriptDTO1).isNotEqualTo(scriptDTO2);
        scriptDTO2.setId(scriptDTO1.getId());
        assertThat(scriptDTO1).isEqualTo(scriptDTO2);
        scriptDTO2.setId(2L);
        assertThat(scriptDTO1).isNotEqualTo(scriptDTO2);
        scriptDTO1.setId(null);
        assertThat(scriptDTO1).isNotEqualTo(scriptDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(scriptMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(scriptMapper.fromId(null)).isNull();
    }
}
