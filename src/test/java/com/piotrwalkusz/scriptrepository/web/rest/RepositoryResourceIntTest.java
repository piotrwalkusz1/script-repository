package com.piotrwalkusz.scriptrepository.web.rest;

import com.piotrwalkusz.scriptrepository.ScriptRepositoryApp;
import com.piotrwalkusz.scriptrepository.domain.Collection;
import com.piotrwalkusz.scriptrepository.domain.Script;
import com.piotrwalkusz.scriptrepository.domain.User;
import com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy;
import com.piotrwalkusz.scriptrepository.domain.enumeration.ScriptLanguage;
import com.piotrwalkusz.scriptrepository.repository.CollectionRepository;
import com.piotrwalkusz.scriptrepository.repository.ScriptRepository;
import com.piotrwalkusz.scriptrepository.repository.UserRepository;
import com.piotrwalkusz.scriptrepository.repository.search.CollectionSearchRepository;
import com.piotrwalkusz.scriptrepository.repository.search.ScriptSearchRepository;
import com.piotrwalkusz.scriptrepository.service.UserService;
import com.piotrwalkusz.scriptrepository.service.dto.CollectionDTO;
import com.piotrwalkusz.scriptrepository.service.dto.ScriptDTO;
import com.piotrwalkusz.scriptrepository.service.mapper.CollectionMapper;
import com.piotrwalkusz.scriptrepository.service.mapper.ScriptMapper;
import com.piotrwalkusz.scriptrepository.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptRepositoryApp.class)
@Transactional
public class RepositoryResourceIntTest {

    private final static String USERNAME = "username";

    private final static String COLLECTION_DEFAULT_NAME = "collectionName";
    private final static Privacy COLLECTION_DEFAULT_PRIVACY = Privacy.PRIVATE;

    private final static String SCRIPT_DEFAULT_NAME = "scriptName";
    private final static String SCRIPT_DEFAULT_DESCRIPTION = "script description";
    private final static String SCRIPT_DEFAULT_CODE = "def foo() {}";
    private final static ScriptLanguage SCRIPT_DEFAULT_LANGUAGE = ScriptLanguage.GROOVY;

    @Autowired
    private UserService userService;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionResource collectionResource;

    @Autowired
    private ScriptResource scriptResource;

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private ScriptMapper scriptMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    @Autowired
    private CollectionSearchRepository collectionSearchRepository;

    @Autowired
    private ScriptSearchRepository scriptSearchRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private User persistedUser;

    private Collection persistedCollection;

    private Collection collection;

    private Script script;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RepositoryResource repositoryResource = new RepositoryResource(userService, collectionRepository, collectionResource, scriptResource, scriptRepository, collectionMapper, scriptMapper, entityManager);
        mockMvc = MockMvcBuilders.standaloneSetup(repositoryResource)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jackson2HttpMessageConverter)
            .build();

        persistedUser = UserResourceIntTest.createEntity(entityManager);
        persistedUser.setLogin(USERNAME);
        entityManager.persist(persistedUser);
        entityManager.flush();

        persistedCollection = CollectionResourceIntTest.createEntity(entityManager);
        persistedCollection.setOwner(persistedUser);
        entityManager.persist(persistedCollection);
        entityManager.flush();

        collection = CollectionResourceIntTest.createEntity(entityManager);
        collection.setName(COLLECTION_DEFAULT_NAME);
        collection.setPrivacy(COLLECTION_DEFAULT_PRIVACY);

        script = ScriptResourceIntTest.createEntity(entityManager);
        script.setCollection(persistedCollection);
        script.setName(SCRIPT_DEFAULT_NAME);
        script.setDescription(SCRIPT_DEFAULT_DESCRIPTION);
        script.setScriptLanguage(SCRIPT_DEFAULT_LANGUAGE);
        script.setCode(SCRIPT_DEFAULT_CODE);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addCollectionWithoutOwner() throws Exception {
        int size = collectionRepository.findAll().size();

        collection.setOwner(null);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        mockMvc.perform(post("/api/repository/collections")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isCreated());

        entityManager.flush();
        entityManager.clear();

        List<Collection> collections = collectionRepository.findAll();
        assertThat(collections).hasSize(size + 1);
        Collection testCollection = collections.get(collections.size() - 1);
        assertThat(testCollection.getOwner().getLogin()).isEqualTo(USERNAME);
        assertThat(testCollection.getName()).isEqualTo(COLLECTION_DEFAULT_NAME);
        assertThat(testCollection.getPrivacy()).isEqualTo(COLLECTION_DEFAULT_PRIVACY);

        Collection collectionEs = collectionSearchRepository.findOne(testCollection.getId());
        assertThat(collectionEs).isEqualToComparingFieldByField(testCollection);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addCollectionWithRightOwner() throws Exception {
        int size = collectionRepository.findAll().size();

        collection.setOwner(userRepository.findOneByLogin(USERNAME).get());
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        mockMvc.perform(post("/api/repository/collections")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isCreated());

        entityManager.flush();
        entityManager.clear();

        List<Collection> collections = collectionRepository.findAll();
        assertThat(collections).hasSize(size + 1);
        Collection testCollection = collections.get(collections.size() - 1);
        assertThat(testCollection.getOwner().getLogin()).isEqualTo(USERNAME);
        assertThat(testCollection.getName()).isEqualTo(COLLECTION_DEFAULT_NAME);
        assertThat(testCollection.getPrivacy()).isEqualTo(COLLECTION_DEFAULT_PRIVACY);

        Collection collectionEs = collectionSearchRepository.findOne(testCollection.getId());
        assertThat(collectionEs).isEqualToComparingFieldByField(testCollection);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addCollectionWithBadOwner() throws Exception {
        int size = collectionRepository.findAll().size();

        User newUser = UserResourceIntTest.createEntity(entityManager);
        entityManager.persist(newUser);
        entityManager.flush();

        collection.setOwner(newUser);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        mockMvc.perform(post("/api/repository/collections")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Owner id has to be null or equal the user id"));

        entityManager.flush();
        entityManager.clear();

        assertThat(collectionRepository.findAll()).hasSize(size);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addCollectionWithSameName() throws Exception {
        int size = collectionRepository.findAll().size();

        collection.setOwner(persistedCollection.getOwner());
        collection.setName(persistedCollection.getName());
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        mockMvc.perform(post("/api/repository/collections")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("The user already has a collection with this name"));

        entityManager.flush();
        entityManager.clear();

        assertThat(collectionRepository.findAll()).hasSize(size);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addScript() throws Exception {
        int size  = scriptRepository.findAll().size();

        ScriptDTO scriptDTO = scriptMapper.toDto(script);
        mockMvc.perform(post("/api/repository/scripts")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isCreated());

        entityManager.flush();
        entityManager.clear();

        List<Script> scripts = scriptRepository.findAll();
        assertThat(scripts).hasSize(size + 1);
        Script testScript = scripts.get(scripts.size() - 1);
        assertThat(testScript).isEqualToIgnoringGivenFields(script, "id", "collection");
        assertThat(scriptSearchRepository.findOne(testScript.getId())).isEqualToIgnoringGivenFields(script, "id", "collection");
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addScriptWithSameName() throws Exception {
        Script persistedScript = ScriptResourceIntTest.createEntity(entityManager);
        persistedScript.setCollection(persistedCollection);
        entityManager.persist(persistedScript);
        entityManager.flush();

        int size  = scriptRepository.findAll().size();

        script.setCollection(persistedScript.getCollection());
        script.setName(persistedScript.getName());

        ScriptDTO scriptDTO = scriptMapper.toDto(script);
        mockMvc.perform(post("/api/repository/scripts")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("A script with the same name already exists in this collection"));

        entityManager.flush();
        entityManager.clear();

        List<Script> scripts = scriptRepository.findAll();
        assertThat(scripts).hasSize(size);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void addScriptToCollectionOfAnotherUser() throws Exception {
        Collection collectonOfAnotherUser = CollectionResourceIntTest.createEntity(entityManager);
        collectonOfAnotherUser.setPrivacy(Privacy.PUBLIC); // we can't add script to a collection of another user even if it's public;
        entityManager.persist(collectonOfAnotherUser);
        entityManager.flush();

        int size  = scriptRepository.findAll().size();

        script.setCollection(collectonOfAnotherUser);

        ScriptDTO scriptDTO = scriptMapper.toDto(script);
        mockMvc.perform(post("/api/repository/scripts")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scriptDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Cannot create a script in a collection that does not belong to the user"));

        entityManager.flush();
        entityManager.clear();

        List<Script> scripts = scriptRepository.findAll();
        assertThat(scripts).hasSize(size);
    }

    @Test
    @Ignore
    public void deleteCollectionById() {
    }

    @Test
    @Ignore
    public void deleteScriptById() {
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void getAllCollections() throws Exception {
        Collection collection1 = CollectionResourceIntTest.createEntity(entityManager);
        collection1.setOwner(persistedUser);
        entityManager.persist(collection1);

        Collection collection2 = CollectionResourceIntTest.createEntity(entityManager);
        collection2.setOwner(persistedUser);
        Script script = ScriptResourceIntTest.createEntity(entityManager);
        entityManager.persist(script);
        collection2.addScripts(script);
        entityManager.persist(collection2);
        script = ScriptResourceIntTest.createEntity(entityManager);
        entityManager.persist(script);
        collection2.addScripts(script);
        entityManager.persist(collection2);

        entityManager.flush();
        entityManager.clear();

        int size = (int)collectionRepository.findAll().stream().filter((c) -> c.getOwner().getId().equals(persistedUser.getId())).count();

        mockMvc.perform(get("/api/repository/collections"))
            .andExpect(jsonPath("$", hasSize(size)));
    }

    @Test
    @Ignore
    public void getAllScripts() {
    }

    @Test
    @Ignore
    public void getCollectionById() {
    }

    @Test
    @Ignore
    public void getScriptById() {
    }

    @Test
    @Ignore
    public void updateScriptById() {
    }
}
