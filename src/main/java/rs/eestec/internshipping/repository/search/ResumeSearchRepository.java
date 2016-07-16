package rs.eestec.internshipping.repository.search;

import rs.eestec.internshipping.domain.Resume;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Resume entity.
 */
public interface ResumeSearchRepository extends ElasticsearchRepository<Resume, Long> {
}
