package com.piotrwalkusz.scriptrepository.web.rest;

import com.piotrwalkusz.scriptrepository.domain.*;
import com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy;
import com.piotrwalkusz.scriptrepository.repository.CollectionRepository;
import com.piotrwalkusz.scriptrepository.repository.ScriptRepository;
import com.piotrwalkusz.scriptrepository.service.UserService;
import com.piotrwalkusz.scriptrepository.service.dto.CollectionDTO;
import com.piotrwalkusz.scriptrepository.service.dto.ScriptDTO;
import com.piotrwalkusz.scriptrepository.service.mapper.CollectionMapper;
import com.piotrwalkusz.scriptrepository.service.mapper.ScriptMapper;
import com.piotrwalkusz.scriptrepository.util.ExceptionUtils;
import com.piotrwalkusz.scriptrepository.web.api.RepositoryApi;
import com.piotrwalkusz.scriptrepository.web.rest.errors.BadRequestAlertException;
import com.piotrwalkusz.scriptrepository.web.rest.errors.InternalServerErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RepositoryResource implements RepositoryApi {

    private final UserService userService;

    private final CollectionRepository collectionRepository;

    private final CollectionResource collectionResource;

    private final ScriptResource scriptResource;

    private final ScriptRepository scriptRepository;

    private final CollectionMapper collectionMapper;

    private final ScriptMapper scriptMapper;

    private final EntityManager entityManager;

    public RepositoryResource(UserService userService, CollectionRepository collectionRepository, CollectionResource collectionResource, ScriptResource scriptResource, ScriptRepository scriptRepository, CollectionMapper collectionMapper, ScriptMapper scriptMapper, EntityManager entityManager) {
        this.userService = userService;
        this.collectionRepository = collectionRepository;
        this.collectionResource = collectionResource;
        this.scriptResource = scriptResource;
        this.scriptRepository = scriptRepository;
        this.collectionMapper = collectionMapper;
        this.scriptMapper = scriptMapper;
        this.entityManager = entityManager;
    }

    @Override
    public ResponseEntity<CollectionDTO> addCollection(@Valid @RequestBody CollectionDTO collection) {
        User user = getUser();
        if (collection.getOwnerId() != null && !collection.getOwnerId().equals(user.getId())) {
            throw new BadRequestAlertException("Owner id has to be null or equal the user id", "Collection", "ownerid");
        }
        collection.setOwnerId(user.getId());
        return ExceptionUtils.wrapCheckedException(() -> collectionResource.createCollection(collection));
    }

    @Override
    public ResponseEntity<ScriptDTO> addScript(@Valid @RequestBody ScriptDTO script) {
        Collection collection = collectionRepository.findOneWithEagerRelationships(script.getCollectionId());
        if (!collection.getOwner().equals(getUser())) {
            throw new BadRequestAlertException("Cannot create a script in a collection that does not belong to the user", "Script", "collectionid");
        }
        return ExceptionUtils.wrapCheckedException(() -> scriptResource.createScript(script));
    }

    @Override
    public ResponseEntity<Void> deleteCollectionById(@PathVariable Long id) {
        Collection collection = collectionRepository.findOne(id);
        if (!collection.getOwner().equals(getUser())) {
            throw new BadRequestAlertException("Cannot delete a collection that does not belong to the user", "Collection", "ownerid");
        }
        return ExceptionUtils.wrapCheckedException(() -> collectionResource.deleteCollection(id));
    }

    @Override
    public ResponseEntity<Void> deleteScriptById(@PathVariable Long id) {
        Collection collection = collectionRepository.findOneContainingScriptId(id);
        if (!collection.getOwner().equals(getUser())) {
            throw new BadRequestAlertException("Cannot delete a script contained in a collection that does not belong to the user", "Script", "notmyscript");
        }
        return ExceptionUtils.wrapCheckedException(() -> scriptResource.deleteScript(id));
    }

    @Override
    public ResponseEntity<List<CollectionDTO>> getAllCollections(@RequestParam String user) {
        List<Collection> collections;
        User currentUser = getUser();
        if (user == null || user.equals(currentUser.getLogin())) {
            collections = collectionRepository.findByOwner(currentUser.getId());
        } else {
            collections = collectionRepository.findPublicByOwnerLogin(user);
        }
        return ResponseEntity.ok(collectionMapper.toDto(collections));
    }

    @Override
    public ResponseEntity<List<ScriptDTO>> getAllScripts(@RequestParam String user) {
        List<Script> scripts;
        User currentUser = getUser();
        if (user == null || user.equals(currentUser.getLogin())) {
            scripts = scriptRepository.findAllByOwnerLogin(currentUser.getLogin());
        } else {
            scripts = scriptRepository.findAllPublicByOwnerLogin(currentUser.getLogin());
        }
        return ResponseEntity.ok(scriptMapper.toDto(scripts));
    }

    @Override
    public ResponseEntity<CollectionDTO> getCollectionById(@PathVariable Long id) {
        Collection collection = collectionRepository.findOneWithEagerRelationships(id);
        if (collection.getPrivacy() == Privacy.PUBLIC || getUser().equals(collection.getOwner())) {
            return ResponseEntity.ok(collectionMapper.toDto(collection));
        } else {
            throw new BadRequestAlertException("A collection has to be public or you have to be the owner of the collection", "Collection", "authorize");
        }
    }

    @Override
    public ResponseEntity<ScriptDTO> getScriptById(@PathVariable Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Script> script = query.from(Script.class);
        Join<Script, Collection> collection = script.join(Script_.collection);
        Join<Collection, User> user = collection.join(Collection_.owner);
        query.multiselect(script, collection.get(Collection_.privacy), user.get(User_.id))
            .where(cb.and(
                cb.equal(script.get(Script_.id), id),
                cb.equal(script.get(Script_.collection).get(Collection_.id), collection.get(Collection_.id)),
                cb.equal(collection.get(Collection_.owner).get(User_.id), user.get(User_.id))
            ));

        try {
            Tuple result = entityManager.createQuery(query).getSingleResult();
            if (((Privacy) result.get(1)) == Privacy.PUBLIC || ((Long) result.get(2)).equals(getUser().getId())) {
                return ResponseEntity.ok(scriptMapper.toDto((Script) result.get(0)));
            } else {
                throw new BadRequestAlertException("A script has to be in public collection or you have to be the owner of the script", "Script", "authorize");
            }
        } catch (NoResultException ex) {
            throw new BadRequestAlertException("A script with this id is not found", "Script", "notexists");
        }
    }

    @Override
    public ResponseEntity<ScriptDTO> updateScriptById(@PathVariable Long id, @Valid @RequestBody ScriptDTO script) {
        User user = getUser();
        Script scriptEntity = scriptRepository.findOne(id);
        if (scriptEntity.getCollection().getOwner().equals(user)) {
            if (!scriptEntity.getCollection().getId().equals(script.getCollectionId())) {
                Collection newCollection = collectionRepository.findOne(script.getCollectionId());
                if (!newCollection.getOwner().equals(user)) {
                    throw new BadRequestAlertException("Cannot move a script to a collection not belonging to you", "Script", "noowner");
                }
            }
            return ExceptionUtils.wrapCheckedException(() -> scriptResource.updateScript(script));
        } else {
            throw new BadRequestAlertException("Cannot update a script not belonging to you", "Script", "noowner");
        }
    }

    private User getUser() {
        return userService.getUser().orElseThrow(() -> new InternalServerErrorException("User is not found in database"));
    }
}
