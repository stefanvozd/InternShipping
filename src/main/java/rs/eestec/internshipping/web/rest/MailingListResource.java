package rs.eestec.internshipping.web.rest;

import com.codahale.metrics.annotation.Timed;
import rs.eestec.internshipping.domain.MailingList;
import rs.eestec.internshipping.service.MailingListService;
import rs.eestec.internshipping.web.rest.util.HeaderUtil;
import rs.eestec.internshipping.web.rest.util.PaginationUtil;
import rs.eestec.internshipping.web.rest.dto.MailingListDTO;
import rs.eestec.internshipping.web.rest.mapper.MailingListMapper;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MailingList.
 */
@RestController
@RequestMapping("/api")
public class MailingListResource {

    private final Logger log = LoggerFactory.getLogger(MailingListResource.class);
        
    @Inject
    private MailingListService mailingListService;
    
    @Inject
    private MailingListMapper mailingListMapper;
    
    /**
     * POST  /mailing-lists : Create a new mailingList.
     *
     * @param mailingListDTO the mailingListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mailingListDTO, or with status 400 (Bad Request) if the mailingList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mailing-lists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailingListDTO> createMailingList(@RequestBody MailingListDTO mailingListDTO) throws URISyntaxException {
        log.debug("REST request to save MailingList : {}", mailingListDTO);
        if (mailingListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mailingList", "idexists", "A new mailingList cannot already have an ID")).body(null);
        }
        MailingListDTO result = mailingListService.save(mailingListDTO);
        return ResponseEntity.created(new URI("/api/mailing-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mailingList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mailing-lists : Updates an existing mailingList.
     *
     * @param mailingListDTO the mailingListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mailingListDTO,
     * or with status 400 (Bad Request) if the mailingListDTO is not valid,
     * or with status 500 (Internal Server Error) if the mailingListDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/mailing-lists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailingListDTO> updateMailingList(@RequestBody MailingListDTO mailingListDTO) throws URISyntaxException {
        log.debug("REST request to update MailingList : {}", mailingListDTO);
        if (mailingListDTO.getId() == null) {
            return createMailingList(mailingListDTO);
        }
        MailingListDTO result = mailingListService.save(mailingListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mailingList", mailingListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mailing-lists : get all the mailingLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of mailingLists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/mailing-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MailingListDTO>> getAllMailingLists(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MailingLists");
        Page<MailingList> page = mailingListService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mailing-lists");
        return new ResponseEntity<>(mailingListMapper.mailingListsToMailingListDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /mailing-lists/:id : get the "id" mailingList.
     *
     * @param id the id of the mailingListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mailingListDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/mailing-lists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailingListDTO> getMailingList(@PathVariable Long id) {
        log.debug("REST request to get MailingList : {}", id);
        MailingListDTO mailingListDTO = mailingListService.findOne(id);
        return Optional.ofNullable(mailingListDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mailing-lists/:id : delete the "id" mailingList.
     *
     * @param id the id of the mailingListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/mailing-lists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMailingList(@PathVariable Long id) {
        log.debug("REST request to delete MailingList : {}", id);
        mailingListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mailingList", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mailing-lists?query=:query : search for the mailingList corresponding
     * to the query.
     *
     * @param query the query of the mailingList search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/mailing-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MailingListDTO>> searchMailingLists(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MailingLists for query {}", query);
        Page<MailingList> page = mailingListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/mailing-lists");
        return new ResponseEntity<>(mailingListMapper.mailingListsToMailingListDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
