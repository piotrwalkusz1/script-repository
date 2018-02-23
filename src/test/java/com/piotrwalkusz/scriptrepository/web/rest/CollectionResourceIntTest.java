package com.piotrwalkusz.scriptrepository.web.rest;

import com.piotrwalkusz.scriptrepository.ScriptRepositoryApp;

import com.piotrwalkusz.scriptrepository.domain.Collection;
import com.piotrwalkusz.scriptrepository.domain.User;
import com.piotrwalkusz.scriptrepository.repository.CollectionRepository;
import com.piotrwalkusz.scriptrepository.repository.search.CollectionSearchRepository;
import com.piotrwalkusz.scriptrepository.service.dto.CollectionDTO;
import com.piotrwalkusz.scriptrepository.service.mapper.CollectionMapper;
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

import javax.persistence.EntityManager;
import java.util.List;

import static com.piotrwalkusz.scriptrepository.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy;
/**
 * Test class for the CollectionResource REST controller.
 *
 * @see CollectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptRepositoryApp.class)
public class CollectionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Privacy DEFAULT_PRIVACY = Privacy.PUBLIC;
    private static final Privacy UPDATED_PRIVACY = Privacy.PRIVATE;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private CollectionSearchRepository collectionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCollectionMockMvc;

    private Collection collection;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollectionResource collectionResource = new CollectionResource(collectionRepository, collectionMapper, collectionSearchRepository);
        this.restCollectionMockMvc = MockMvcBuilders.standaloneSetup(collectionResource)
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
    public static Collection createEntity(EntityManager em) {
        Collection collection = new Collection()
            .name(DEFAULT_NAME)
            .privacy(DEFAULT_PRIVACY);
        // Add required entity
        User owner = UserResourceIntTest.createEntity(em);
        em.persist(owner);
        em.flush();
        collection.setOwner(owner);
        return collection;
    }

    @Before
    public void initTest() {
        collectionSearchRepository.deleteAll();
        collection = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollection() throws Exception {
        int databaseSizeBeforeCreate = collectionRepository.findAll().size();

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        restCollectionMockMvc.perform(post("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isCreated());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeCreate + 1);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollection.getPrivacy()).isEqualTo(DEFAULT_PRIVACY);

        // Validate the Collection in Elasticsearch
        Collection collectionEs = collectionSearchRepository.findOne(testCollection.getId());
        assertThat(collectionEs).isEqualToIgnoringGivenFields(testCollection);
    }

    @Test
    @Transactional
    public void createCollectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collectionRepository.findAll().size();

        // Create the Collection with an existing ID
        collection.setId(1L);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollectionMockMvc.perform(post("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setName(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc.perform(post("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrivacyIsRequired() throws Exception {
        int databaseSizeBeforeTest = collectionRepository.findAll().size();
        // set the field null
        collection.setPrivacy(null);

        // Create the Collection, which fails.
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        restCollectionMockMvc.perform(post("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isBadRequest());

        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCollections() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get all the collectionList
        restCollectionMockMvc.perform(get("/api/collections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].privacy").value(hasItem(DEFAULT_PRIVACY.toString())));
    }

    @Test
    @Transactional
    public void getCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);

        // Get the collection
        restCollectionMockMvc.perform(get("/api/collections/{id}", collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collection.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.privacy").value(DEFAULT_PRIVACY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCollection() throws Exception {
        // Get the collection
        restCollectionMockMvc.perform(get("/api/collections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        collectionSearchRepository.save(collection);
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Update the collection
        Collection updatedCollection = collectionRepository.findOne(collection.getId());
        // Disconnect from session so that the updates on updatedCollection are not directly saved in db
        em.detach(updatedCollection);
        updatedCollection
            .name(UPDATED_NAME)
            .privacy(UPDATED_PRIVACY);
        CollectionDTO collectionDTO = collectionMapper.toDto(updatedCollection);

        restCollectionMockMvc.perform(put("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isOk());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate);
        Collection testCollection = collectionList.get(collectionList.size() - 1);
        assertThat(testCollection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollection.getPrivacy()).isEqualTo(UPDATED_PRIVACY);

        // Validate the Collection in Elasticsearch
        Collection collectionEs = collectionSearchRepository.findOne(testCollection.getId());
        assertThat(collectionEs).isEqualToIgnoringGivenFields(testCollection);
    }

    @Test
    @Transactional
    public void updateNonExistingCollection() throws Exception {
        int databaseSizeBeforeUpdate = collectionRepository.findAll().size();

        // Create the Collection
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCollectionMockMvc.perform(put("/api/collections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collectionDTO)))
            .andExpect(status().isCreated());

        // Validate the Collection in the database
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        collectionSearchRepository.save(collection);
        int databaseSizeBeforeDelete = collectionRepository.findAll().size();

        // Get the collection
        restCollectionMockMvc.perform(delete("/api/collections/{id}", collection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean collectionExistsInEs = collectionSearchRepository.exists(collection.getId());
        assertThat(collectionExistsInEs).isFalse();

        // Validate the database is empty
        List<Collection> collectionList = collectionRepository.findAll();
        assertThat(collectionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCollection() throws Exception {
        // Initialize the database
        collectionRepository.saveAndFlush(collection);
        collectionSearchRepository.save(collection);

        // Search the collection
        restCollectionMockMvc.perform(get("/api/_search/collections?query=id:" + collection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collection.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].privacy").value(hasItem(DEFAULT_PRIVACY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collection.class);
        Collection collection1 = new Collection();
        collection1.setId(1L);
        Collection collection2 = new Collection();
        collection2.setId(collection1.getId());
        assertThat(collection1).isEqualTo(collection2);
        collection2.setId(2L);
        assertThat(collection1).isNotEqualTo(collection2);
        collection1.setId(null);
        assertThat(collection1).isNotEqualTo(collection2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollectionDTO.class);
        CollectionDTO collectionDTO1 = new CollectionDTO();
        collectionDTO1.setId(1L);
        CollectionDTO collectionDTO2 = new CollectionDTO();
        assertThat(collectionDTO1).isNotEqualTo(collectionDTO2);
        collectionDTO2.setId(collectionDTO1.getId());
        assertThat(collectionDTO1).isEqualTo(collectionDTO2);
        collectionDTO2.setId(2L);
        assertThat(collectionDTO1).isNotEqualTo(collectionDTO2);
        collectionDTO1.setId(null);
        assertThat(collectionDTO1).isNotEqualTo(collectionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(collectionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(collectionMapper.fromId(null)).isNull();
    }
}
