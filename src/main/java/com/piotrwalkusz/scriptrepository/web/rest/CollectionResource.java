package com.piotrwalkusz.scriptrepository.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.piotrwalkusz.scriptrepository.domain.Collection;
import com.piotrwalkusz.scriptrepository.repository.CollectionRepository;
import com.piotrwalkusz.scriptrepository.repository.UserRepository;
import com.piotrwalkusz.scriptrepository.repository.search.CollectionSearchRepository;
import com.piotrwalkusz.scriptrepository.security.AuthoritiesConstants;
import com.piotrwalkusz.scriptrepository.service.dto.CollectionDTO;
import com.piotrwalkusz.scriptrepository.service.mapper.CollectionMapper;
import com.piotrwalkusz.scriptrepository.web.rest.errors.BadRequestAlertException;
import com.piotrwalkusz.scriptrepository.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Collection.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CollectionResource {

    private final Logger log = LoggerFactory.getLogger(CollectionResource.class);

    private static final String ENTITY_NAME = "collection";

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    private final CollectionSearchRepository collectionSearchRepository;

    public CollectionResource(CollectionRepository collectionRepository, CollectionMapper collectionMapper, CollectionSearchRepository collectionSearchRepository) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
        this.collectionSearchRepository = collectionSearchRepository;
    }

    /**
     * POST  /collections : Create a new collection.
     *
     * @param collectionDTO the collectionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new collectionDTO, or with status 400 (Bad Request) if the collection has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/collections")
    @Timed
    public ResponseEntity<CollectionDTO> createCollection(@Valid @RequestBody CollectionDTO collectionDTO) throws URISyntaxException {
        log.debug("REST request to save Collection : {}", collectionDTO);
        if (collectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new collection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (collectionRepository.existsByOwnerIdAndName(collectionDTO.getOwnerId(), collectionDTO.getName())) {
            throw new BadRequestAlertException("The user already has a collection with this name", ENTITY_NAME, "namexists");
        }
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        CollectionDTO result = collectionMapper.toDto(collection);
        collectionSearchRepository.save(collection);
        return ResponseEntity.created(new URI("/api/collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /collections : Updates an existing collection.
     *
     * @param collectionDTO the collectionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated collectionDTO,
     * or with status 400 (Bad Request) if the collectionDTO is not valid,
     * or with status 500 (Internal Server Error) if the collectionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/collections")
    @Timed
    public ResponseEntity<CollectionDTO> updateCollection(@Valid @RequestBody CollectionDTO collectionDTO) throws URISyntaxException {
        log.debug("REST request to update Collection : {}", collectionDTO);
        if (collectionDTO.getId() == null) {
            return createCollection(collectionDTO);
        }
        if (collectionRepository.existsByOwnerIdAndNameAndIdNot(collectionDTO.getOwnerId(), collectionDTO.getName(), collectionDTO.getId())) {
            throw new BadRequestAlertException("The user has already had a collection with this name", ENTITY_NAME, "namexists");
        }
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        CollectionDTO result = collectionMapper.toDto(collection);
        collectionSearchRepository.save(collection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, collectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /collections : get all the collections.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of collections in body
     */
    @GetMapping("/collections")
    @Timed
    public List<CollectionDTO> getAllCollections() {
        log.debug("REST request to get all Collections");
        List<Collection> collections = collectionRepository.findAllWithEagerRelationships();
        return collectionMapper.toDto(collections);
    }

    /**
     * GET  /collections/:id : get the "id" collection.
     *
     * @param id the id of the collectionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the collectionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/collections/{id}")
    @Timed
    public ResponseEntity<CollectionDTO> getCollection(@PathVariable Long id) {
        log.debug("REST request to get Collection : {}", id);
        Collection collection = collectionRepository.findOneWithEagerRelationships(id);
        CollectionDTO collectionDTO = collectionMapper.toDto(collection);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(collectionDTO));
    }

    /**
     * DELETE  /collections/:id : delete the "id" collection.
     *
     * @param id the id of the collectionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/collections/{id}")
    @Timed
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        log.debug("REST request to delete Collection : {}", id);
        collectionRepository.delete(id);
        collectionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/collections?query=:query : search for the collection corresponding
     * to the query.
     *
     * @param query the query of the collection search
     * @return the result of the search
     */
    @GetMapping("/_search/collections")
    @Timed
    public List<CollectionDTO> searchCollections(@RequestParam String query) {
        log.debug("REST request to search Collections for query {}", query);
        return StreamSupport
            .stream(collectionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(collectionMapper::toDto)
            .collect(Collectors.toList());
    }
}
