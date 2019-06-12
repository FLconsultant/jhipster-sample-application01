package io.github.jhipster.application01.web.rest;

import io.github.jhipster.application01.JhipsterSampleApplication01App;
import io.github.jhipster.application01.domain.Measure;
import io.github.jhipster.application01.repository.MeasureRepository;
import io.github.jhipster.application01.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static io.github.jhipster.application01.web.rest.TestUtil.sameInstant;
import static io.github.jhipster.application01.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MeasureResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplication01App.class)
public class MeasureResourceIT {

    private static final String DEFAULT_MEASURESOURCE = "AAAAAAAAAA";
    private static final String UPDATED_MEASURESOURCE = "BBBBBBBBBB";

    private static final Float DEFAULT_MEASUREVALUE = 1F;
    private static final Float UPDATED_MEASUREVALUE = 2F;

    private static final Long DEFAULT_MEASUREVALUELONG = 1L;
    private static final Long UPDATED_MEASUREVALUELONG = 2L;

    private static final LocalDate DEFAULT_MEASUREDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEASUREDATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_MEASUREDATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MEASUREDATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMeasureMockMvc;

    private Measure measure;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeasureResource measureResource = new MeasureResource(measureRepository);
        this.restMeasureMockMvc = MockMvcBuilders.standaloneSetup(measureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measure createEntity(EntityManager em) {
        Measure measure = new Measure()
            .measuresource(DEFAULT_MEASURESOURCE)
            .measurevalue(DEFAULT_MEASUREVALUE)
            .measurevaluelong(DEFAULT_MEASUREVALUELONG)
            .measuredate(DEFAULT_MEASUREDATE)
            .measuredatetime(DEFAULT_MEASUREDATETIME);
        return measure;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Measure createUpdatedEntity(EntityManager em) {
        Measure measure = new Measure()
            .measuresource(UPDATED_MEASURESOURCE)
            .measurevalue(UPDATED_MEASUREVALUE)
            .measurevaluelong(UPDATED_MEASUREVALUELONG)
            .measuredate(UPDATED_MEASUREDATE)
            .measuredatetime(UPDATED_MEASUREDATETIME);
        return measure;
    }

    @BeforeEach
    public void initTest() {
        measure = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeasure() throws Exception {
        int databaseSizeBeforeCreate = measureRepository.findAll().size();

        // Create the Measure
        restMeasureMockMvc.perform(post("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isCreated());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeCreate + 1);
        Measure testMeasure = measureList.get(measureList.size() - 1);
        assertThat(testMeasure.getMeasuresource()).isEqualTo(DEFAULT_MEASURESOURCE);
        assertThat(testMeasure.getMeasurevalue()).isEqualTo(DEFAULT_MEASUREVALUE);
        assertThat(testMeasure.getMeasurevaluelong()).isEqualTo(DEFAULT_MEASUREVALUELONG);
        assertThat(testMeasure.getMeasuredate()).isEqualTo(DEFAULT_MEASUREDATE);
        assertThat(testMeasure.getMeasuredatetime()).isEqualTo(DEFAULT_MEASUREDATETIME);
    }

    @Test
    @Transactional
    public void createMeasureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = measureRepository.findAll().size();

        // Create the Measure with an existing ID
        measure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeasureMockMvc.perform(post("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isBadRequest());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMeasures() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get all the measureList
        restMeasureMockMvc.perform(get("/api/measures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(measure.getId().intValue())))
            .andExpect(jsonPath("$.[*].measuresource").value(hasItem(DEFAULT_MEASURESOURCE.toString())))
            .andExpect(jsonPath("$.[*].measurevalue").value(hasItem(DEFAULT_MEASUREVALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].measurevaluelong").value(hasItem(DEFAULT_MEASUREVALUELONG.intValue())))
            .andExpect(jsonPath("$.[*].measuredate").value(hasItem(DEFAULT_MEASUREDATE.toString())))
            .andExpect(jsonPath("$.[*].measuredatetime").value(hasItem(sameInstant(DEFAULT_MEASUREDATETIME))));
    }
    
    @Test
    @Transactional
    public void getMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        // Get the measure
        restMeasureMockMvc.perform(get("/api/measures/{id}", measure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(measure.getId().intValue()))
            .andExpect(jsonPath("$.measuresource").value(DEFAULT_MEASURESOURCE.toString()))
            .andExpect(jsonPath("$.measurevalue").value(DEFAULT_MEASUREVALUE.doubleValue()))
            .andExpect(jsonPath("$.measurevaluelong").value(DEFAULT_MEASUREVALUELONG.intValue()))
            .andExpect(jsonPath("$.measuredate").value(DEFAULT_MEASUREDATE.toString()))
            .andExpect(jsonPath("$.measuredatetime").value(sameInstant(DEFAULT_MEASUREDATETIME)));
    }

    @Test
    @Transactional
    public void getNonExistingMeasure() throws Exception {
        // Get the measure
        restMeasureMockMvc.perform(get("/api/measures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        int databaseSizeBeforeUpdate = measureRepository.findAll().size();

        // Update the measure
        Measure updatedMeasure = measureRepository.findById(measure.getId()).get();
        // Disconnect from session so that the updates on updatedMeasure are not directly saved in db
        em.detach(updatedMeasure);
        updatedMeasure
            .measuresource(UPDATED_MEASURESOURCE)
            .measurevalue(UPDATED_MEASUREVALUE)
            .measurevaluelong(UPDATED_MEASUREVALUELONG)
            .measuredate(UPDATED_MEASUREDATE)
            .measuredatetime(UPDATED_MEASUREDATETIME);

        restMeasureMockMvc.perform(put("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeasure)))
            .andExpect(status().isOk());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeUpdate);
        Measure testMeasure = measureList.get(measureList.size() - 1);
        assertThat(testMeasure.getMeasuresource()).isEqualTo(UPDATED_MEASURESOURCE);
        assertThat(testMeasure.getMeasurevalue()).isEqualTo(UPDATED_MEASUREVALUE);
        assertThat(testMeasure.getMeasurevaluelong()).isEqualTo(UPDATED_MEASUREVALUELONG);
        assertThat(testMeasure.getMeasuredate()).isEqualTo(UPDATED_MEASUREDATE);
        assertThat(testMeasure.getMeasuredatetime()).isEqualTo(UPDATED_MEASUREDATETIME);
    }

    @Test
    @Transactional
    public void updateNonExistingMeasure() throws Exception {
        int databaseSizeBeforeUpdate = measureRepository.findAll().size();

        // Create the Measure

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeasureMockMvc.perform(put("/api/measures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(measure)))
            .andExpect(status().isBadRequest());

        // Validate the Measure in the database
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMeasure() throws Exception {
        // Initialize the database
        measureRepository.saveAndFlush(measure);

        int databaseSizeBeforeDelete = measureRepository.findAll().size();

        // Delete the measure
        restMeasureMockMvc.perform(delete("/api/measures/{id}", measure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Measure> measureList = measureRepository.findAll();
        assertThat(measureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Measure.class);
        Measure measure1 = new Measure();
        measure1.setId(1L);
        Measure measure2 = new Measure();
        measure2.setId(measure1.getId());
        assertThat(measure1).isEqualTo(measure2);
        measure2.setId(2L);
        assertThat(measure1).isNotEqualTo(measure2);
        measure1.setId(null);
        assertThat(measure1).isNotEqualTo(measure2);
    }
}
