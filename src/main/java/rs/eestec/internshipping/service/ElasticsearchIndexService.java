package rs.eestec.internshipping.service;

import com.codahale.metrics.annotation.Timed;
import rs.eestec.internshipping.domain.*;
import rs.eestec.internshipping.repository.*;
import rs.eestec.internshipping.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ApplicationSearchRepository applicationSearchRepository;

    @Inject
    private ArticleRepository articleRepository;

    @Inject
    private ArticleSearchRepository articleSearchRepository;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private JobRepository jobRepository;

    @Inject
    private JobSearchRepository jobSearchRepository;

    @Inject
    private MailingListRepository mailingListRepository;

    @Inject
    private MailingListSearchRepository mailingListSearchRepository;

    @Inject
    private ResumeRepository resumeRepository;

    @Inject
    private ResumeSearchRepository resumeSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Application.class, applicationRepository, applicationSearchRepository);
        reindexForClass(Article.class, articleRepository, articleSearchRepository);
        reindexForClass(Company.class, companyRepository, companySearchRepository);
        reindexForClass(Job.class, jobRepository, jobSearchRepository);
        reindexForClass(MailingList.class, mailingListRepository, mailingListSearchRepository);
        reindexForClass(Resume.class, resumeRepository, resumeSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional
    @SuppressWarnings("unchecked")
    private <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                                          ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
