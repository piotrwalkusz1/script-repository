package com.piotrwalkusz.scriptrepository.repository;

import com.piotrwalkusz.scriptrepository.domain.Collection;
import com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Collection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

    @Query("select collection from Collection collection where collection.owner.login = ?#{principal.username}")
    List<Collection> findByOwnerIsCurrentUser();

    @Query("select collection from Collection collection where collection.owner.id = :ownerId")
    List<Collection> findByOwner(@Param("ownerId") Long ownerId);

    @Query("select distinct collection from Collection collection left join fetch collection.scripts left join fetch collection.sharedUsers where collection.owner.id = :ownerId")
    List<Collection> findByOwnerWithEagerRelationships(@Param("ownerId") Long ownerId);

    @Query("select collection from Collection collection where collection.owner.login = :ownerLogin and collection.privacy = com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy.PUBLIC")
    List<Collection> findPublicByOwnerLogin(@Param("ownerLogin") String ownerLogin);

    @Query("select distinct collection from Collection collection left join fetch collection.scripts left join fetch collection.sharedUsers where collection.owner.login = :ownerLogin and collection.privacy = com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy.PUBLIC")
    List<Collection> findPublicByOwnerLoginWithEagerRelationships(@Param("ownerLogin") String ownerLogin);

    boolean existsByOwnerIdAndName(Long ownerId, String name);

    @Query("select distinct collection from Collection collection left join fetch collection.sharedUsers")
    List<Collection> findAllWithEagerRelationships();

    @Query("select distinct collection from Collection collection left join fetch collection.sharedUsers where collection.id =:id")
    Collection findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select collection from Collection collection where collection.id = (select script.collection.id from Script script where script.id = :id)")
    Collection findOneContainingScriptId(@Param("id") Long id);
}
