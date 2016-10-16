package rs.eestec.internshipping.web.rest;

import rs.eestec.internshipping.InternShippingApp;
import rs.eestec.internshipping.domain.Application;
import rs.eestec.internshipping.repository.ApplicationRepository;
import rs.eestec.internshipping.service.ApplicationService;
import rs.eestec.internshipping.repository.search.ApplicationSearchRepository;
import rs.eestec.internshipping.web.rest.dto.ApplicationDTO;
import rs.eestec.internshipping.web.rest.mapper.ApplicationMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ApplicationResource REST controller.
 *
 * @see ApplicationResource
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = InternShippingApp.class)
//@WebAppConfiguration
//@IntegrationTest
public class ApplicationResourceIntTest {


    private static final Boolean DEFAULT_MARKED = false;
    private static final Boolean UPDATED_MARKED = true;

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private ApplicationMapper applicationMapper;

    @Inject
    private ApplicationService applicationService;

    @Inject
    private ApplicationSearchRepository applicationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restApplicationMockMvc;

    private Application application;

    //@PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApplicationResource applicationResource = new ApplicationResource();
        ReflectionTestUtils.setField(applicationResource, "applicationService", applicationService);
        ReflectionTestUtils.setField(applicationResource, "applicationMapper", applicationMapper);
        this.restApplicationMockMvc = MockMvcBuilders.standaloneSetup(applicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

   // @Before
    public void initTest() {
        applicationSearchRepository.deleteAll();
        application = new Application();
        application.setMarked(DEFAULT_MARKED);
    }

   // @Test
    @Transactional
    public void createApplication() throws Exception {
        int databaseSizeBeforeCreate = applicationRepository.findAll().size();

        // Create the Application
        ApplicationDTO applicationDTO = applicationMapper.applicationToApplicationDTO(application);

        restApplicationMockMvc.perform(post("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicationDTO)))
                .andExpect(status().isCreated());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeCreate + 1);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.isMarked()).isEqualTo(DEFAULT_MARKED);

        // Validate the Application in ElasticSearch
        Application applicationEs = applicationSearchRepository.findOne(testApplication.getId());
        assertThat(applicationEs).isEqualToComparingFieldByField(testApplication);
    }

   // @Test
    @Transactional
    public void getAllApplications() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get all the applications
        restApplicationMockMvc.perform(get("/api/applications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
                .andExpect(jsonPath("$.[*].marked").value(hasItem(DEFAULT_MARKED.booleanValue())));
    }

  //  @Test
    @Transactional
    public void getApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);

        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(application.getId().intValue()))
            .andExpect(jsonPath("$.marked").value(DEFAULT_MARKED.booleanValue()));
    }

  //  @Test
    @Transactional
    public void getNonExistingApplication() throws Exception {
        // Get the application
        restApplicationMockMvc.perform(get("/api/applications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

  //  @Test
    @Transactional
    public void updateApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        applicationSearchRepository.save(application);
        int databaseSizeBeforeUpdate = applicationRepository.findAll().size();

        // Update the application
        Application updatedApplication = new Application();
        updatedApplication.setId(application.getId());
        updatedApplication.setMarked(UPDATED_MARKED);
        ApplicationDTO applicationDTO = applicationMapper.applicationToApplicationDTO(updatedApplication);

        restApplicationMockMvc.perform(put("/api/applications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(applicationDTO)))
                .andExpect(status().isOk());

        // Validate the Application in the database
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeUpdate);
        Application testApplication = applications.get(applications.size() - 1);
        assertThat(testApplication.isMarked()).isEqualTo(UPDATED_MARKED);

        // Validate the Application in ElasticSearch
        Application applicationEs = applicationSearchRepository.findOne(testApplication.getId());
        assertThat(applicationEs).isEqualToComparingFieldByField(testApplication);
    }

  //  @Test
    @Transactional
    public void deleteApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        applicationSearchRepository.save(application);
        int databaseSizeBeforeDelete = applicationRepository.findAll().size();

        // Get the application
        restApplicationMockMvc.perform(delete("/api/applications/{id}", application.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean applicationExistsInEs = applicationSearchRepository.exists(application.getId());
        assertThat(applicationExistsInEs).isFalse();

        // Validate the database is empty
        List<Application> applications = applicationRepository.findAll();
        assertThat(applications).hasSize(databaseSizeBeforeDelete - 1);
    }

  //  @Test
    @Transactional
    public void searchApplication() throws Exception {
        // Initialize the database
        applicationRepository.saveAndFlush(application);
        applicationSearchRepository.save(application);

        // Search the application
        restApplicationMockMvc.perform(get("/api/_search/applications?query=id:" + application.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(application.getId().intValue())))
            .andExpect(jsonPath("$.[*].marked").value(hasItem(DEFAULT_MARKED.booleanValue())));
    }
}
