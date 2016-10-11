package rs.eestec.internshipping.service;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;
import rs.eestec.internshipping.domain.Resume;
import rs.eestec.internshipping.repository.ResumeRepository;
import rs.eestec.internshipping.repository.search.ResumeSearchRepository;
import rs.eestec.internshipping.web.rest.dto.ResumeDTO;
import rs.eestec.internshipping.web.rest.mapper.ResumeMapper;

import javax.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
        resume.setCvFileContent(parseToPlainText(resume.getCvFile()));
        resume = resumeRepository.save(resume);
        ResumeDTO result = resumeMapper.resumeToResumeDTO(resume);
        resumeSearchRepository.save(resume);
        return result;
    }

    public String parseToPlainText(byte[] source) {
        log.info("Parsing CV file to plain text");
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try (InputStream stream = new ByteArrayInputStream(source)) {
            parser.parse(stream, handler, metadata);
            log.info("Done Parsing CV file to plain text");
            return handler.toString();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } catch (SAXException e) {
            log.error(e.getMessage(),e);
        } catch (TikaException e) {
            log.error(e.getMessage(),e);
        }
        return "";
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
     *  Get resumes for job id
     *
     *  @param id the id of the job
     *  @return the Resume
     */
    @Transactional(readOnly = true)
    public Page<Resume> getResumesForJobId(Pageable pageable,Long id) {
        log.debug("Request to get Resume : {}", id);
        Page<Resume> result = resumeRepository.getResumesForJobId(pageable,id);
        return result;
    }

    /**
     *  Get all the resumes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Resume> findCurrentUserResume(Pageable pageable) {
        log.debug("Request to get all Resumes");
        Page<Resume> result = resumeRepository.findByUserIsCurrentUser(pageable);
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
