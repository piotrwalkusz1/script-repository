package com.piotrwalkusz.scriptrepository.repository.search;

import com.piotrwalkusz.scriptrepository.domain.Collection;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Collection entity.
 */
public interface CollectionSearchRepository extends ElasticsearchRepository<Collection, Long> {
}
