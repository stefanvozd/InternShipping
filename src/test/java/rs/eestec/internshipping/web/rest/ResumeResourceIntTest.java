package rs.eestec.internshipping.web.rest;

import rs.eestec.internshipping.InternShippingApp;
import rs.eestec.internshipping.domain.Resume;
import rs.eestec.internshipping.repository.ResumeRepository;
import rs.eestec.internshipping.service.ResumeService;
import rs.eestec.internshipping.repository.search.ResumeSearchRepository;
import rs.eestec.internshipping.web.rest.dto.ResumeDTO;
import rs.eestec.internshipping.web.rest.mapper.ResumeMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import rs.eestec.internshipping.domain.enumeration.Education;

/**
 * Test class for the ResumeResource REST controller.
 *
 * @see ResumeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InternShippingApp.class)
@WebAppConfiguration
@IntegrationTest
public class ResumeResourceIntTest {


    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_OVERVIEW = "AAAAA";
    private static final String UPDATED_OVERVIEW = "BBBBB";

    private static final Education DEFAULT_EDUCATION = Education.ANY;
    private static final Education UPDATED_EDUCATION = Education.ASSOCIATE_STUDENT;
    private static final String DEFAULT_FACULTY = "AAAAA";
    private static final String UPDATED_FACULTY = "BBBBB";
    private static final String DEFAULT_ENROLLMENT_YEAR = "AAAAA";
    private static final String UPDATED_ENROLLMENT_YEAR = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";
    private static final String DEFAULT_CONTACT_EMAIL = "AAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_JSON_RESUME = "AAAAA";
    private static final String UPDATED_JSON_RESUME = "BBBBB";

    private static final byte[] DEFAULT_CV_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CV_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CV_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CV_FILE_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_CV_FILE_CONTENT = "AAAAA";
    private static final String UPDATED_CV_FILE_CONTENT = "BBBBB";

    private static final Boolean DEFAULT_RECEIVE_JOB_ALERTS = false;
    private static final Boolean UPDATED_RECEIVE_JOB_ALERTS = true;
    private static final String DEFAULT_SOCIAL_LINKEDIN = "AAAAA";
    private static final String UPDATED_SOCIAL_LINKEDIN = "BBBBB";
    private static final String DEFAULT_REPRESENTATIVE_SKILLS = "AAAAA";
    private static final String UPDATED_REPRESENTATIVE_SKILLS = "BBBBB";

    @Inject
    private ResumeRepository resumeRepository;

    @Inject
    private ResumeMapper resumeMapper;

    @Inject
    private ResumeService resumeService;

    @Inject
    private ResumeSearchRepository resumeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restResumeMockMvc;

    private Resume resume;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResumeResource resumeResource = new ResumeResource();
        ReflectionTestUtils.setField(resumeResource, "resumeService", resumeService);
        ReflectionTestUtils.setField(resumeResource, "resumeMapper", resumeMapper);
        this.restResumeMockMvc = MockMvcBuilders.standaloneSetup(resumeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        resumeSearchRepository.deleteAll();
        resume = new Resume();
        resume.setImage(DEFAULT_IMAGE);
        resume.setImageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        resume.setName(DEFAULT_NAME);
        resume.setTitle(DEFAULT_TITLE);
        resume.setOverview(DEFAULT_OVERVIEW);
        resume.setEducation(DEFAULT_EDUCATION);
        resume.setFaculty(DEFAULT_FACULTY);
        resume.setEnrollmentYear(DEFAULT_ENROLLMENT_YEAR);
        resume.setLocation(DEFAULT_LOCATION);
        resume.setContactEmail(DEFAULT_CONTACT_EMAIL);
        resume.setBirthDate(DEFAULT_BIRTH_DATE);
        resume.setJsonResume(DEFAULT_JSON_RESUME);
        resume.setCvFile(DEFAULT_CV_FILE);
        resume.setCvFileContentType(DEFAULT_CV_FILE_CONTENT_TYPE);
        resume.setCvFileContent(DEFAULT_CV_FILE_CONTENT);
        resume.setReceiveJobAlerts(DEFAULT_RECEIVE_JOB_ALERTS);
        resume.setSocialLinkedin(DEFAULT_SOCIAL_LINKEDIN);
        resume.setRepresentativeSkills(DEFAULT_REPRESENTATIVE_SKILLS);
    }

    @Test
    @Transactional
    public void createResume() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // Create the Resume
        ResumeDTO resumeDTO = resumeMapper.resumeToResumeDTO(resume);

        restResumeMockMvc.perform(post("/api/resumes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
                .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(databaseSizeBeforeCreate + 1);
        Resume testResume = resumes.get(resumes.size() - 1);
        assertThat(testResume.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testResume.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testResume.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResume.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResume.getOverview()).isEqualTo(DEFAULT_OVERVIEW);
        assertThat(testResume.getEducation()).isEqualTo(DEFAULT_EDUCATION);
        assertThat(testResume.getFaculty()).isEqualTo(DEFAULT_FACULTY);
        assertThat(testResume.getEnrollmentYear()).isEqualTo(DEFAULT_ENROLLMENT_YEAR);
        assertThat(testResume.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testResume.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testResume.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testResume.getJsonResume()).isEqualTo(DEFAULT_JSON_RESUME);
        assertThat(testResume.getCvFile()).isEqualTo(DEFAULT_CV_FILE);
        assertThat(testResume.getCvFileContentType()).isEqualTo(DEFAULT_CV_FILE_CONTENT_TYPE);
        assertThat(testResume.getCvFileContent()).isEqualTo(DEFAULT_CV_FILE_CONTENT);
        assertThat(testResume.isReceiveJobAlerts()).isEqualTo(DEFAULT_RECEIVE_JOB_ALERTS);
        assertThat(testResume.getSocialLinkedin()).isEqualTo(DEFAULT_SOCIAL_LINKEDIN);
        assertThat(testResume.getRepresentativeSkills()).isEqualTo(DEFAULT_REPRESENTATIVE_SKILLS);

        // Validate the Resume in ElasticSearch
        Resume resumeEs = resumeSearchRepository.findOne(testResume.getId());
        assertThat(resumeEs).isEqualToComparingFieldByField(testResume);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resumeRepository.findAll().size();
        // set the field null
        resume.setName(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.resumeToResumeDTO(resume);

        restResumeMockMvc.perform(post("/api/resumes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
                .andExpect(status().isBadRequest());

        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacultyIsRequired() throws Exception {
        int databaseSizeBeforeTest = resumeRepository.findAll().size();
        // set the field null
        resume.setFaculty(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.resumeToResumeDTO(resume);

        restResumeMockMvc.perform(post("/api/resumes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
                .andExpect(status().isBadRequest());

        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEnrollmentYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = resumeRepository.findAll().size();
        // set the field null
        resume.setEnrollmentYear(null);

        // Create the Resume, which fails.
        ResumeDTO resumeDTO = resumeMapper.resumeToResumeDTO(resume);

        restResumeMockMvc.perform(post("/api/resumes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
                .andExpect(status().isBadRequest());

        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResumes() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumes
        restResumeMockMvc.perform(get("/api/resumes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].overview").value(hasItem(DEFAULT_OVERVIEW.toString())))
                .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION.toString())))
                .andExpect(jsonPath("$.[*].faculty").value(hasItem(DEFAULT_FACULTY.toString())))
                .andExpect(jsonPath("$.[*].enrollmentYear").value(hasItem(DEFAULT_ENROLLMENT_YEAR.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
                .andExpect(jsonPath("$.[*].jsonResume").value(hasItem(DEFAULT_JSON_RESUME.toString())))
                .andExpect(jsonPath("$.[*].cvFileContentType").value(hasItem(DEFAULT_CV_FILE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].cvFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CV_FILE))))
                .andExpect(jsonPath("$.[*].cvFileContent").value(hasItem(DEFAULT_CV_FILE_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].receiveJobAlerts").value(hasItem(DEFAULT_RECEIVE_JOB_ALERTS.booleanValue())))
                .andExpect(jsonPath("$.[*].socialLinkedin").value(hasItem(DEFAULT_SOCIAL_LINKEDIN.toString())))
                .andExpect(jsonPath("$.[*].representativeSkills").value(hasItem(DEFAULT_REPRESENTATIVE_SKILLS.toString())));
    }

    @Test
    @Transactional
    public void getResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get the resume
        restResumeMockMvc.perform(get("/api/resumes/{id}", resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(resume.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.overview").value(DEFAULT_OVERVIEW.toString()))
            .andExpect(jsonPath("$.education").value(DEFAULT_EDUCATION.toString()))
            .andExpect(jsonPath("$.faculty").value(DEFAULT_FACULTY.toString()))
            .andExpect(jsonPath("$.enrollmentYear").value(DEFAULT_ENROLLMENT_YEAR.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.jsonResume").value(DEFAULT_JSON_RESUME.toString()))
            .andExpect(jsonPath("$.cvFileContentType").value(DEFAULT_CV_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.cvFile").value(Base64Utils.encodeToString(DEFAULT_CV_FILE)))
            .andExpect(jsonPath("$.cvFileContent").value(DEFAULT_CV_FILE_CONTENT.toString()))
            .andExpect(jsonPath("$.receiveJobAlerts").value(DEFAULT_RECEIVE_JOB_ALERTS.booleanValue()))
            .andExpect(jsonPath("$.socialLinkedin").value(DEFAULT_SOCIAL_LINKEDIN.toString()))
            .andExpect(jsonPath("$.representativeSkills").value(DEFAULT_REPRESENTATIVE_SKILLS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResume() throws Exception {
        // Get the resume
        restResumeMockMvc.perform(get("/api/resumes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume
        Resume updatedResume = new Resume();
        updatedResume.setId(resume.getId());
        updatedResume.setImage(UPDATED_IMAGE);
        updatedResume.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        updatedResume.setName(UPDATED_NAME);
        updatedResume.setTitle(UPDATED_TITLE);
        updatedResume.setOverview(UPDATED_OVERVIEW);
        updatedResume.setEducation(UPDATED_EDUCATION);
        updatedResume.setFaculty(UPDATED_FACULTY);
        updatedResume.setEnrollmentYear(UPDATED_ENROLLMENT_YEAR);
        updatedResume.setLocation(UPDATED_LOCATION);
        updatedResume.setContactEmail(UPDATED_CONTACT_EMAIL);
        updatedResume.setBirthDate(UPDATED_BIRTH_DATE);
        updatedResume.setJsonResume(UPDATED_JSON_RESUME);
        updatedResume.setCvFile(UPDATED_CV_FILE);
        updatedResume.setCvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE);
        updatedResume.setCvFileContent(UPDATED_CV_FILE_CONTENT);
        updatedResume.setReceiveJobAlerts(UPDATED_RECEIVE_JOB_ALERTS);
        updatedResume.setSocialLinkedin(UPDATED_SOCIAL_LINKEDIN);
        updatedResume.setRepresentativeSkills(UPDATED_REPRESENTATIVE_SKILLS);
        ResumeDTO resumeDTO = resumeMapper.resumeToResumeDTO(updatedResume);

        restResumeMockMvc.perform(put("/api/resumes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resumeDTO)))
                .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumes.get(resumes.size() - 1);
        assertThat(testResume.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testResume.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testResume.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResume.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResume.getOverview()).isEqualTo(UPDATED_OVERVIEW);
        assertThat(testResume.getEducation()).isEqualTo(UPDATED_EDUCATION);
        assertThat(testResume.getFaculty()).isEqualTo(UPDATED_FACULTY);
        assertThat(testResume.getEnrollmentYear()).isEqualTo(UPDATED_ENROLLMENT_YEAR);
        assertThat(testResume.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testResume.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testResume.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testResume.getJsonResume()).isEqualTo(UPDATED_JSON_RESUME);
        assertThat(testResume.getCvFile()).isEqualTo(UPDATED_CV_FILE);
        assertThat(testResume.getCvFileContentType()).isEqualTo(UPDATED_CV_FILE_CONTENT_TYPE);
        assertThat(testResume.getCvFileContent()).isEqualTo(UPDATED_CV_FILE_CONTENT);
        assertThat(testResume.isReceiveJobAlerts()).isEqualTo(UPDATED_RECEIVE_JOB_ALERTS);
        assertThat(testResume.getSocialLinkedin()).isEqualTo(UPDATED_SOCIAL_LINKEDIN);
        assertThat(testResume.getRepresentativeSkills()).isEqualTo(UPDATED_REPRESENTATIVE_SKILLS);

        // Validate the Resume in ElasticSearch
        Resume resumeEs = resumeSearchRepository.findOne(testResume.getId());
        assertThat(resumeEs).isEqualToComparingFieldByField(testResume);
    }

    @Test
    @Transactional
    public void deleteResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);
        int databaseSizeBeforeDelete = resumeRepository.findAll().size();

        // Get the resume
        restResumeMockMvc.perform(delete("/api/resumes/{id}", resume.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean resumeExistsInEs = resumeSearchRepository.exists(resume.getId());
        assertThat(resumeExistsInEs).isFalse();

        // Validate the database is empty
        List<Resume> resumes = resumeRepository.findAll();
        assertThat(resumes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);

        // Search the resume
        restResumeMockMvc.perform(get("/api/_search/resumes?query=id:" + resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].overview").value(hasItem(DEFAULT_OVERVIEW.toString())))
            .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION.toString())))
            .andExpect(jsonPath("$.[*].faculty").value(hasItem(DEFAULT_FACULTY.toString())))
            .andExpect(jsonPath("$.[*].enrollmentYear").value(hasItem(DEFAULT_ENROLLMENT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].jsonResume").value(hasItem(DEFAULT_JSON_RESUME.toString())))
            .andExpect(jsonPath("$.[*].cvFileContentType").value(hasItem(DEFAULT_CV_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cvFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_CV_FILE))))
            .andExpect(jsonPath("$.[*].cvFileContent").value(hasItem(DEFAULT_CV_FILE_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].receiveJobAlerts").value(hasItem(DEFAULT_RECEIVE_JOB_ALERTS.booleanValue())))
            .andExpect(jsonPath("$.[*].socialLinkedin").value(hasItem(DEFAULT_SOCIAL_LINKEDIN.toString())))
            .andExpect(jsonPath("$.[*].representativeSkills").value(hasItem(DEFAULT_REPRESENTATIVE_SKILLS.toString())));
    }
}
