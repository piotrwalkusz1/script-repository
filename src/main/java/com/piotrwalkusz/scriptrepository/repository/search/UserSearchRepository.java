package com.piotrwalkusz.scriptrepository.repository.search;

import com.piotrwalkusz.scriptrepository.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
