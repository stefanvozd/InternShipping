package rs.eestec.internshipping.repository.search;

import rs.eestec.internshipping.domain.MailingList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MailingList entity.
 */
public interface MailingListSearchRepository extends ElasticsearchRepository<MailingList, Long> {
}
