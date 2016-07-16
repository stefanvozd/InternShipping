package rs.eestec.internshipping.web.rest;

import com.codahale.metrics.annotation.Timed;
import rs.eestec.internshipping.domain.Resume;
import rs.eestec.internshipping.repository.ResumeRepository;
import rs.eestec.internshipping.repository.search.ResumeSearchRepository;
import rs.eestec.internshipping.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Resume.
 */
@RestController
@RequestMapping("/api")
public class ResumeResource {

    private final Logger log = LoggerFactory.getLogger(ResumeResource.class);
        
    @Inject
    private ResumeRepository resumeRepository;
    
    @Inject
    private ResumeSearchRepository resumeSearchRepository;
    
    /**
     * POST  /resumes : Create a new resume.
     *
     * @param resume the resume to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resume, or with status 400 (Bad Request) if the resume has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/resumes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resume> createResume(@Valid @RequestBody Resume resume) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resume);
        if (resume.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("resume", "idexists", "A new resume cannot already have an ID")).body(null);
        }
        Resume result = resumeRepository.save(resume);
        resumeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("resume", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resumes : Updates an existing resume.
     *
     * @param resume the resume to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resume,
     * or with status 400 (Bad Request) if the resume is not valid,
     * or with status 500 (Internal Server Error) if the resume couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/resumes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resume> updateResume(@Valid @RequestBody Resume resume) throws URISyntaxException {
        log.debug("REST request to update Resume : {}", resume);
        if (resume.getId() == null) {
            return createResume(resume);
        }
        Resume result = resumeRepository.save(resume);
        resumeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("resume", resume.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resumes : get all the resumes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resumes in body
     */
    @RequestMapping(value = "/resumes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Resume> getAllResumes() {
        log.debug("REST request to get all Resumes");
        List<Resume> resumes = resumeRepository.findAll();
        return resumes;
    }

    /**
     * GET  /resumes/:id : get the "id" resume.
     *
     * @param id the id of the resume to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resume, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/resumes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        log.debug("REST request to get Resume : {}", id);
        Resume resume = resumeRepository.findOne(id);
        return Optional.ofNullable(resume)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /resumes/:id : delete the "id" resume.
     *
     * @param id the id of the resume to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/resumes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeRepository.delete(id);
        resumeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("resume", id.toString())).build();
    }

    /**
     * SEARCH  /_search/resumes?query=:query : search for the resume corresponding
     * to the query.
     *
     * @param query the query of the resume search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/resumes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Resume> searchResumes(@RequestParam String query) {
        log.debug("REST request to search Resumes for query {}", query);
        return StreamSupport
            .stream(resumeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
