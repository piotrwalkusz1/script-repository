package com.piotrwalkusz.scriptrepository.repository;

import com.piotrwalkusz.scriptrepository.domain.Script;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Script entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    @Query("select distinct script from Script script left join fetch script.tags")
    List<Script> findAllWithEagerRelationships();

    @Query("select script from Script script left join fetch script.tags where script.id =:id")
    Script findOneWithEagerRelationships(@Param("id") Long id);

    boolean existsByCollectionIdAndName(Long collectionId, String name);

    @Query("select script from Collection collection join collection.scripts script where collection.owner.login = :ownerLogin")
    List<Script> findAllByOwnerLogin(@Param("ownerLogin") String login);

    @Query("select script from Collection collection join collection.scripts script where collection.owner.login = :ownerLogin and collection.privacy = com.piotrwalkusz.scriptrepository.domain.enumeration.Privacy.PUBLIC")
    List<Script> findAllPublicByOwnerLogin(@Param("ownerLogin") String login);
}
