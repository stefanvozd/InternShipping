package rs.eestec.internshipping.service;

import rs.eestec.internshipping.domain.Application;
import rs.eestec.internshipping.repository.ApplicationRepository;
import rs.eestec.internshipping.repository.ResumeRepository;
import rs.eestec.internshipping.repository.search.ApplicationSearchRepository;
import rs.eestec.internshipping.web.rest.dto.ApplicationDTO;
import rs.eestec.internshipping.web.rest.mapper.ApplicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Application.
 */
@Service
@Transactional
public class ApplicationService {

    private final Logger log = LoggerFactory.getLogger(ApplicationService.class);

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ApplicationMapper applicationMapper;

    @Inject
    private ApplicationSearchRepository applicationSearchRepository;

    @Inject
    private ResumeRepository resumeRepository;

    /**
     * Save a application.
     *
     * @param applicationDTO the entity to save
     * @return the persisted entity
     */
    public ApplicationDTO save(ApplicationDTO applicationDTO) {
        log.debug("Request to save Application : {}", applicationDTO);
        Application application = applicationMapper.applicationDTOToApplication(applicationDTO);
        application.setResume(resumeRepository.findByUserIsCurrentUser());
        application = applicationRepository.save(application);
        ApplicationDTO result = applicationMapper.applicationToApplicationDTO(application);
        applicationSearchRepository.save(application);
        return result;
    }

    /**
     *  Get all the applications.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Application> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        Page<Application> result = applicationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one application by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ApplicationDTO findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        Application application = applicationRepository.findOne(id);
        ApplicationDTO applicationDTO = applicationMapper.applicationToApplicationDTO(application);
        return applicationDTO;
    }

    /**
     *  Delete the  application by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.delete(id);
        applicationSearchRepository.delete(id);
    }

    /**
     * Search for the application corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Application> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Applications for query {}", query);
        return applicationSearchRepository.search(queryStringQuery(query), pageable);
    }
}
