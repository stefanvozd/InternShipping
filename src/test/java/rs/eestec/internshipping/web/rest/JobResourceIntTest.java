package rs.eestec.internshipping.web.rest;

import rs.eestec.internshipping.InternShippingApp;
import rs.eestec.internshipping.domain.Job;
import rs.eestec.internshipping.repository.JobRepository;
import rs.eestec.internshipping.service.JobService;
import rs.eestec.internshipping.repository.search.JobSearchRepository;
import rs.eestec.internshipping.web.rest.dto.JobDTO;
import rs.eestec.internshipping.web.rest.mapper.JobMapper;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import rs.eestec.internshipping.domain.enumeration.JobType;
import rs.eestec.internshipping.domain.enumeration.JobLevel;
import rs.eestec.internshipping.domain.enumeration.Education;

/**
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InternShippingApp.class)
@WebAppConfiguration
@IntegrationTest
public class JobResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    private static final JobType DEFAULT_JOB_TYPE = JobType.INTERNSHIP;
    private static final JobType UPDATED_JOB_TYPE = JobType.PART_TIME;

    private static final JobLevel DEFAULT_JOB_LEVEL = JobLevel.ANY;
    private static final JobLevel UPDATED_JOB_LEVEL = JobLevel.NO_EXPERIENCE;

    private static final Education DEFAULT_EDUCATION = Education.ASSOCIATE_STUDENT;
    private static final Education UPDATED_EDUCATION = Education.BACHELOR_STUDENT;
    private static final String DEFAULT_LONG_DESCRIPTION = "AAAAA";
    private static final String UPDATED_LONG_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_SKILLS = "AAAAA";
    private static final String UPDATED_SKILLS = "BBBBB";
    private static final String DEFAULT_SOCIAL_LINKEDIN = "AAAAA";
    private static final String UPDATED_SOCIAL_LINKEDIN = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final LocalDate DEFAULT_ACTIVE_UNTIL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTIVE_UNTIL = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Inject
    private JobRepository jobRepository;

    @Inject
    private JobMapper jobMapper;

    @Inject
    private JobService jobService;

    @Inject
    private JobSearchRepository jobSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restJobMockMvc;

    private Job job;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobResource jobResource = new JobResource();
        ReflectionTestUtils.setField(jobResource, "jobService", jobService);
        ReflectionTestUtils.setField(jobResource, "jobMapper", jobMapper);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        jobSearchRepository.deleteAll();
        job = new Job();
        job.setTitle(DEFAULT_TITLE);
        job.setShortDescription(DEFAULT_SHORT_DESCRIPTION);
        job.setLocation(DEFAULT_LOCATION);
        job.setJobType(DEFAULT_JOB_TYPE);
        job.setJobLevel(DEFAULT_JOB_LEVEL);
        job.setEducation(DEFAULT_EDUCATION);
        job.setLongDescription(DEFAULT_LONG_DESCRIPTION);
        job.setSkills(DEFAULT_SKILLS);
        job.setSocialLinkedin(DEFAULT_SOCIAL_LINKEDIN);
        job.setCreationDate(DEFAULT_CREATION_DATE);
        job.setActiveUntil(DEFAULT_ACTIVE_UNTIL);
        job.setActive(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobs.get(jobs.size() - 1);
        assertThat(testJob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJob.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testJob.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJob.getJobType()).isEqualTo(DEFAULT_JOB_TYPE);
        assertThat(testJob.getJobLevel()).isEqualTo(DEFAULT_JOB_LEVEL);
        assertThat(testJob.getEducation()).isEqualTo(DEFAULT_EDUCATION);
        assertThat(testJob.getLongDescription()).isEqualTo(DEFAULT_LONG_DESCRIPTION);
        assertThat(testJob.getSkills()).isEqualTo(DEFAULT_SKILLS);
        assertThat(testJob.getSocialLinkedin()).isEqualTo(DEFAULT_SOCIAL_LINKEDIN);
        assertThat(testJob.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testJob.getActiveUntil()).isEqualTo(DEFAULT_ACTIVE_UNTIL);
        assertThat(testJob.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Job in ElasticSearch
        Job jobEs = jobSearchRepository.findOne(testJob.getId());
        assertThat(jobEs).isEqualToComparingFieldByField(testJob);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setTitle(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkShortDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setShortDescription(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setLocation(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setJobType(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setJobLevel(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEducationIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setEducation(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setLongDescription(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveUntilIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobRepository.findAll().size();
        // set the field null
        job.setActiveUntil(null);

        // Create the Job, which fails.
        JobDTO jobDTO = jobMapper.jobToJobDTO(job);

        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isBadRequest());

        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobs
        restJobMockMvc.perform(get("/api/jobs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
                .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE.toString())))
                .andExpect(jsonPath("$.[*].jobLevel").value(hasItem(DEFAULT_JOB_LEVEL.toString())))
                .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION.toString())))
                .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS.toString())))
                .andExpect(jsonPath("$.[*].socialLinkedin").value(hasItem(DEFAULT_SOCIAL_LINKEDIN.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].activeUntil").value(hasItem(DEFAULT_ACTIVE_UNTIL.toString())))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.jobType").value(DEFAULT_JOB_TYPE.toString()))
            .andExpect(jsonPath("$.jobLevel").value(DEFAULT_JOB_LEVEL.toString()))
            .andExpect(jsonPath("$.education").value(DEFAULT_EDUCATION.toString()))
            .andExpect(jsonPath("$.longDescription").value(DEFAULT_LONG_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.skills").value(DEFAULT_SKILLS.toString()))
            .andExpect(jsonPath("$.socialLinkedin").value(DEFAULT_SOCIAL_LINKEDIN.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.activeUntil").value(DEFAULT_ACTIVE_UNTIL.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);
        int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        Job updatedJob = new Job();
        updatedJob.setId(job.getId());
        updatedJob.setTitle(UPDATED_TITLE);
        updatedJob.setShortDescription(UPDATED_SHORT_DESCRIPTION);
        updatedJob.setLocation(UPDATED_LOCATION);
        updatedJob.setJobType(UPDATED_JOB_TYPE);
        updatedJob.setJobLevel(UPDATED_JOB_LEVEL);
        updatedJob.setEducation(UPDATED_EDUCATION);
        updatedJob.setLongDescription(UPDATED_LONG_DESCRIPTION);
        updatedJob.setSkills(UPDATED_SKILLS);
        updatedJob.setSocialLinkedin(UPDATED_SOCIAL_LINKEDIN);
        updatedJob.setCreationDate(UPDATED_CREATION_DATE);
        updatedJob.setActiveUntil(UPDATED_ACTIVE_UNTIL);
        updatedJob.setActive(UPDATED_ACTIVE);
        JobDTO jobDTO = jobMapper.jobToJobDTO(updatedJob);

        restJobMockMvc.perform(put("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobDTO)))
                .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobs.get(jobs.size() - 1);
        assertThat(testJob.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJob.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testJob.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testJob.getJobType()).isEqualTo(UPDATED_JOB_TYPE);
        assertThat(testJob.getJobLevel()).isEqualTo(UPDATED_JOB_LEVEL);
        assertThat(testJob.getEducation()).isEqualTo(UPDATED_EDUCATION);
        assertThat(testJob.getLongDescription()).isEqualTo(UPDATED_LONG_DESCRIPTION);
        assertThat(testJob.getSkills()).isEqualTo(UPDATED_SKILLS);
        assertThat(testJob.getSocialLinkedin()).isEqualTo(UPDATED_SOCIAL_LINKEDIN);
        assertThat(testJob.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testJob.getActiveUntil()).isEqualTo(UPDATED_ACTIVE_UNTIL);
        assertThat(testJob.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Job in ElasticSearch
        Job jobEs = jobSearchRepository.findOne(testJob.getId());
        assertThat(jobEs).isEqualToComparingFieldByField(testJob);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);
        int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean jobExistsInEs = jobSearchRepository.exists(job.getId());
        assertThat(jobExistsInEs).isFalse();

        // Validate the database is empty
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);
        jobSearchRepository.save(job);

        // Search the job
        restJobMockMvc.perform(get("/api/_search/jobs?query=id:" + job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].jobLevel").value(hasItem(DEFAULT_JOB_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION.toString())))
            .andExpect(jsonPath("$.[*].longDescription").value(hasItem(DEFAULT_LONG_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS.toString())))
            .andExpect(jsonPath("$.[*].socialLinkedin").value(hasItem(DEFAULT_SOCIAL_LINKEDIN.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
            .andExpect(jsonPath("$.[*].activeUntil").value(hasItem(DEFAULT_ACTIVE_UNTIL.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
}
