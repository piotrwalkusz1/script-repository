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

}
