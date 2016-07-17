package rs.eestec.internshipping.service;

import rs.eestec.internshipping.domain.Resume;
import rs.eestec.internshipping.repository.ResumeRepository;
import rs.eestec.internshipping.repository.search.ResumeSearchRepository;
import rs.eestec.internshipping.web.rest.dto.ResumeDTO;
import rs.eestec.internshipping.web.rest.mapper.ResumeMapper;
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
 * Service Implementation for managing Resume.
 */
@Service
@Transactional
public class ResumeService {

    private final Logger log = LoggerFactory.getLogger(ResumeService.class);
    
    @Inject
    private ResumeRepository resumeRepository;
    
    @Inject
    private ResumeMapper resumeMapper;
    
    @Inject
    private ResumeSearchRepository resumeSearchRepository;
    
    /**
     * Save a resume.
     * 
     * @param resumeDTO the entity to save
     * @return the persisted entity
     */
    public ResumeDTO save(ResumeDTO resumeDTO) {
        log.debug("Request to save Resume : {}", resumeDTO);
        Resume resume = resumeMapper.resumeDTOToResume(resumeDTO);
        resume = resumeRepository.save(resume);
        ResumeDTO result = resumeMapper.resumeToResumeDTO(resume);
        resumeSearchRepository.save(resume);
        return result;
    }

    /**
     *  Get all the resumes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Resume> findAll(Pageable pageable) {
        log.debug("Request to get all Resumes");
        Page<Resume> result = resumeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one resume by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ResumeDTO findOne(Long id) {
        log.debug("Request to get Resume : {}", id);
        Resume resume = resumeRepository.findOne(id);
        ResumeDTO resumeDTO = resumeMapper.resumeToResumeDTO(resume);
        return resumeDTO;
    }

    /**
     *  Delete the  resume by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Resume : {}", id);
        resumeRepository.delete(id);
        resumeSearchRepository.delete(id);
    }

    /**
     * Search for the resume corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Resume> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Resumes for query {}", query);
        return resumeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
