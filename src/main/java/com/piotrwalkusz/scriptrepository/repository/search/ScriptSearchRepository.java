package com.piotrwalkusz.scriptrepository.repository.search;

import com.piotrwalkusz.scriptrepository.domain.Script;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Script entity.
 */
public interface ScriptSearchRepository extends ElasticsearchRepository<Script, Long> {
}
