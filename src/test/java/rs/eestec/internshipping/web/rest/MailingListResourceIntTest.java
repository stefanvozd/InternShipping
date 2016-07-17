package rs.eestec.internshipping.web.rest;

import rs.eestec.internshipping.InternShippingApp;
import rs.eestec.internshipping.domain.MailingList;
import rs.eestec.internshipping.repository.MailingListRepository;
import rs.eestec.internshipping.service.MailingListService;
import rs.eestec.internshipping.repository.search.MailingListSearchRepository;
import rs.eestec.internshipping.web.rest.dto.MailingListDTO;
import rs.eestec.internshipping.web.rest.mapper.MailingListMapper;

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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MailingListResource REST controller.
 *
 * @see MailingListResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InternShippingApp.class)
@WebAppConfiguration
@IntegrationTest
public class MailingListResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MailingListRepository mailingListRepository;

    @Inject
    private MailingListMapper mailingListMapper;

    @Inject
    private MailingListService mailingListService;

    @Inject
    private MailingListSearchRepository mailingListSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMailingListMockMvc;

    private MailingList mailingList;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MailingListResource mailingListResource = new MailingListResource();
        ReflectionTestUtils.setField(mailingListResource, "mailingListService", mailingListService);
        ReflectionTestUtils.setField(mailingListResource, "mailingListMapper", mailingListMapper);
        this.restMailingListMockMvc = MockMvcBuilders.standaloneSetup(mailingListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mailingListSearchRepository.deleteAll();
        mailingList = new MailingList();
        mailingList.setEmail(DEFAULT_EMAIL);
        mailingList.setDateCreated(DEFAULT_DATE_CREATED);
    }

    @Test
    @Transactional
    public void createMailingList() throws Exception {
        int databaseSizeBeforeCreate = mailingListRepository.findAll().size();

        // Create the MailingList
        MailingListDTO mailingListDTO = mailingListMapper.mailingListToMailingListDTO(mailingList);

        restMailingListMockMvc.perform(post("/api/mailing-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailingListDTO)))
                .andExpect(status().isCreated());

        // Validate the MailingList in the database
        List<MailingList> mailingLists = mailingListRepository.findAll();
        assertThat(mailingLists).hasSize(databaseSizeBeforeCreate + 1);
        MailingList testMailingList = mailingLists.get(mailingLists.size() - 1);
        assertThat(testMailingList.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMailingList.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);

        // Validate the MailingList in ElasticSearch
        MailingList mailingListEs = mailingListSearchRepository.findOne(testMailingList.getId());
        assertThat(mailingListEs).isEqualToComparingFieldByField(testMailingList);
    }

    @Test
    @Transactional
    public void getAllMailingLists() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get all the mailingLists
        restMailingListMockMvc.perform(get("/api/mailing-lists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mailingList.getId().intValue())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }

    @Test
    @Transactional
    public void getMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);

        // Get the mailingList
        restMailingListMockMvc.perform(get("/api/mailing-lists/{id}", mailingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mailingList.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMailingList() throws Exception {
        // Get the mailingList
        restMailingListMockMvc.perform(get("/api/mailing-lists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);
        mailingListSearchRepository.save(mailingList);
        int databaseSizeBeforeUpdate = mailingListRepository.findAll().size();

        // Update the mailingList
        MailingList updatedMailingList = new MailingList();
        updatedMailingList.setId(mailingList.getId());
        updatedMailingList.setEmail(UPDATED_EMAIL);
        updatedMailingList.setDateCreated(UPDATED_DATE_CREATED);
        MailingListDTO mailingListDTO = mailingListMapper.mailingListToMailingListDTO(updatedMailingList);

        restMailingListMockMvc.perform(put("/api/mailing-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailingListDTO)))
                .andExpect(status().isOk());

        // Validate the MailingList in the database
        List<MailingList> mailingLists = mailingListRepository.findAll();
        assertThat(mailingLists).hasSize(databaseSizeBeforeUpdate);
        MailingList testMailingList = mailingLists.get(mailingLists.size() - 1);
        assertThat(testMailingList.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMailingList.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);

        // Validate the MailingList in ElasticSearch
        MailingList mailingListEs = mailingListSearchRepository.findOne(testMailingList.getId());
        assertThat(mailingListEs).isEqualToComparingFieldByField(testMailingList);
    }

    @Test
    @Transactional
    public void deleteMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);
        mailingListSearchRepository.save(mailingList);
        int databaseSizeBeforeDelete = mailingListRepository.findAll().size();

        // Get the mailingList
        restMailingListMockMvc.perform(delete("/api/mailing-lists/{id}", mailingList.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean mailingListExistsInEs = mailingListSearchRepository.exists(mailingList.getId());
        assertThat(mailingListExistsInEs).isFalse();

        // Validate the database is empty
        List<MailingList> mailingLists = mailingListRepository.findAll();
        assertThat(mailingLists).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMailingList() throws Exception {
        // Initialize the database
        mailingListRepository.saveAndFlush(mailingList);
        mailingListSearchRepository.save(mailingList);

        // Search the mailingList
        restMailingListMockMvc.perform(get("/api/_search/mailing-lists?query=id:" + mailingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mailingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
}
