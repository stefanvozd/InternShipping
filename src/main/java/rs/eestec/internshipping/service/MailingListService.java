package rs.eestec.internshipping.service;

import rs.eestec.internshipping.domain.MailingList;
import rs.eestec.internshipping.repository.MailingListRepository;
import rs.eestec.internshipping.repository.search.MailingListSearchRepository;
import rs.eestec.internshipping.web.rest.dto.MailingListDTO;
import rs.eestec.internshipping.web.rest.mapper.MailingListMapper;
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
 * Service Implementation for managing MailingList.
 */
@Service
@Transactional
public class MailingListService {

    private final Logger log = LoggerFactory.getLogger(MailingListService.class);
    
    @Inject
    private MailingListRepository mailingListRepository;
    
    @Inject
    private MailingListMapper mailingListMapper;
    
    @Inject
    private MailingListSearchRepository mailingListSearchRepository;
    
    /**
     * Save a mailingList.
     * 
     * @param mailingListDTO the entity to save
     * @return the persisted entity
     */
    public MailingListDTO save(MailingListDTO mailingListDTO) {
        log.debug("Request to save MailingList : {}", mailingListDTO);
        MailingList mailingList = mailingListMapper.mailingListDTOToMailingList(mailingListDTO);
        mailingList = mailingListRepository.save(mailingList);
        MailingListDTO result = mailingListMapper.mailingListToMailingListDTO(mailingList);
        mailingListSearchRepository.save(mailingList);
        return result;
    }

    /**
     *  Get all the mailingLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<MailingList> findAll(Pageable pageable) {
        log.debug("Request to get all MailingLists");
        Page<MailingList> result = mailingListRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one mailingList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MailingListDTO findOne(Long id) {
        log.debug("Request to get MailingList : {}", id);
        MailingList mailingList = mailingListRepository.findOne(id);
        MailingListDTO mailingListDTO = mailingListMapper.mailingListToMailingListDTO(mailingList);
        return mailingListDTO;
    }

    /**
     *  Delete the  mailingList by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MailingList : {}", id);
        mailingListRepository.delete(id);
        mailingListSearchRepository.delete(id);
    }

    /**
     * Search for the mailingList corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MailingList> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MailingLists for query {}", query);
        return mailingListSearchRepository.search(queryStringQuery(query), pageable);
    }
}
