package rs.eestec.internshipping.web.rest;

import com.codahale.metrics.annotation.Timed;
import rs.eestec.internshipping.domain.Resume;
import rs.eestec.internshipping.service.ResumeService;
import rs.eestec.internshipping.web.rest.util.HeaderUtil;
import rs.eestec.internshipping.web.rest.util.PaginationUtil;
import rs.eestec.internshipping.web.rest.dto.ResumeDTO;
import rs.eestec.internshipping.web.rest.mapper.ResumeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
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
    private ResumeService resumeService;

    @Inject
    private ResumeMapper resumeMapper;

    /**
     * POST  /resumes : Create a new resume.
     *
     * @param resumeDTO the resumeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resumeDTO, or with status 400 (Bad Request) if the resume has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/resumes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResumeDTO> createResume(@Valid @RequestBody ResumeDTO resumeDTO) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resumeDTO);
        if (resumeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("resume", "idexists", "A new resume cannot already have an ID")).body(null);
        }
        ResumeDTO result = resumeService.save(resumeDTO);
        return ResponseEntity.created(new URI("/api/resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("resume", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resumes : Updates an existing resume.
     *
     * @param resumeDTO the resumeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resumeDTO,
     * or with status 400 (Bad Request) if the resumeDTO is not valid,
     * or with status 500 (Internal Server Error) if the resumeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/resumes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResumeDTO> updateResume(@Valid @RequestBody ResumeDTO resumeDTO) throws URISyntaxException {
        log.debug("REST request to update Resume : {}", resumeDTO);
        if (resumeDTO.getId() == null) {
            return createResume(resumeDTO);
        }
        ResumeDTO result = resumeService.save(resumeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("resume", resumeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resumes : get all the resumes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resumes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/resumes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ResumeDTO>> getAllResumes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Resumes");
        Page<Resume> page = resumeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resumes");
        return new ResponseEntity<>(resumeMapper.resumesToResumeDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    /**
     * GET  /resumes : get all the resumes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resumes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/myresume",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ResumeDTO>> getCurrentUserResume(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of current user Resume");
        Page<Resume> page = resumeService.findCurrentUserResume(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/myresume");
        return new ResponseEntity<>(resumeMapper.resumesToResumeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /resumes/:id : get the "id" resume.
     *
     * @param id the id of the resumeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resumeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/resumes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ResumeDTO> getResume(@PathVariable Long id) {
        log.debug("REST request to get Resume : {}", id);
        ResumeDTO resumeDTO = resumeService.findOne(id);
        return Optional.ofNullable(resumeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /resumes/job/{id} : get all the resumes applied for job id.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of resumes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/resumes/job/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ResumeDTO>> getResumesForJobId(Pageable pageable,@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a page of Resumes");
        Page<Resume> page = resumeService.getResumesForJobId(pageable,id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resumes");
        return new ResponseEntity<>(resumeMapper.resumesToResumeDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    /**
     * DELETE  /resumes/:id : delete the "id" resume.
     *
     * @param id the id of the resumeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/resumes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeService.delete(id);
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
    public ResponseEntity<List<ResumeDTO>> searchResumes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Resumes for query {}", query);
        Page<Resume> page = resumeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/resumes");
        return new ResponseEntity<>(resumeMapper.resumesToResumeDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
