package rs.eestec.internshipping.service;

import org.springframework.security.core.context.SecurityContextHolder;
import rs.eestec.internshipping.domain.Job;
import rs.eestec.internshipping.repository.JobRepository;
import rs.eestec.internshipping.repository.search.JobSearchRepository;
import rs.eestec.internshipping.web.rest.dto.JobDTO;
import rs.eestec.internshipping.web.rest.mapper.JobMapper;
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
 * Service Implementation for managing Job.
 */
@Service
@Transactional
public class JobService {

    private final Logger log = LoggerFactory.getLogger(JobService.class);

    @Inject
    private JobRepository jobRepository;

    @Inject
    private JobMapper jobMapper;

    @Inject
    private JobSearchRepository jobSearchRepository;

    /**
     * Save a job.
     *
     * @param jobDTO the entity to save
     * @return the persisted entity
     */
    public JobDTO save(JobDTO jobDTO) {
        log.debug("Request to save Job : {}", jobDTO);
        Job job = jobMapper.jobDTOToJob(jobDTO);
        job = jobRepository.save(job);
        JobDTO result = jobMapper.jobToJobDTO(job);
        jobSearchRepository.save(job);
        return result;
    }

    /**
     *  Get all the jobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> findAll(Pageable pageable) {
        log.debug("Request to get all Jobs");
        Page<Job> result = jobRepository.findAll(pageable);

        return result;
    }


    /**
     *  Get all the jobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> findOurAll(Pageable pageable) {
        log.debug("Request to get all Our Jobs");
        Page<Job> result = jobRepository.findAllOurJobs(pageable);
        return result;
    }


    /**
     *  Get all applied  jobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> allJobsApplyedTo(Pageable pageable) {
        log.debug("Request to get all applied Jobs");
        Page<Job> result = jobRepository.allJobsApplyedTo(pageable);
        return result;
    }

    /**
     *  Get one job by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public JobDTO findOne(Long id) {
        log.debug("Request to get Job : {}", id);
        Job job = jobRepository.findOne(id);
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);
        return jobDTO;
    }

    /**
     *  Delete the  job by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Job : {}", id);
        jobRepository.delete(id);
        jobSearchRepository.delete(id);
    }

    /**
     * Search for the job corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Job> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Jobs for query {}", query);
        return jobSearchRepository.search(queryStringQuery(query), pageable);
    }
}
