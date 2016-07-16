package rs.eestec.internshipping.repository.search;

import rs.eestec.internshipping.domain.Application;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Application entity.
 */
public interface ApplicationSearchRepository extends ElasticsearchRepository<Application, Long> {
}
