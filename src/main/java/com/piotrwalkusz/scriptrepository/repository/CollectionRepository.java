package com.piotrwalkusz.scriptrepository.repository;

import com.piotrwalkusz.scriptrepository.domain.Collection;
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
    @Query("select distinct collection from Collection collection left join fetch collection.sharedUsers")
    List<Collection> findAllWithEagerRelationships();

    @Query("select collection from Collection collection left join fetch collection.sharedUsers where collection.id =:id")
    Collection findOneWithEagerRelationships(@Param("id") Long id);

}
