package io.github.jhipster.application01.web.rest;

import io.github.jhipster.application01.domain.Measure;
import io.github.jhipster.application01.repository.MeasureRepository;
import io.github.jhipster.application01.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.application01.domain.Measure}.
 */
@RestController
@RequestMapping("/api")
public class MeasureResource {

    private final Logger log = LoggerFactory.getLogger(MeasureResource.class);

    private static final String ENTITY_NAME = "measure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasureRepository measureRepository;

    public MeasureResource(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    /**
     * {@code POST  /measures} : Create a new measure.
     *
     * @param measure the measure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measure, or with status {@code 400 (Bad Request)} if the measure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measures")
    public ResponseEntity<Measure> createMeasure(@RequestBody Measure measure) throws URISyntaxException {
        log.debug("REST request to save Measure : {}", measure);
        if (measure.getId() != null) {
            throw new BadRequestAlertException("A new measure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Measure result = measureRepository.save(measure);
        return ResponseEntity.created(new URI("/api/measures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /measures} : Updates an existing measure.
     *
     * @param measure the measure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measure,
     * or with status {@code 400 (Bad Request)} if the measure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measures")
    public ResponseEntity<Measure> updateMeasure(@RequestBody Measure measure) throws URISyntaxException {
        log.debug("REST request to update Measure : {}", measure);
        if (measure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Measure result = measureRepository.save(measure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, measure.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /measures} : get all the measures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measures in body.
     */
    @GetMapping("/measures")
    public ResponseEntity<List<Measure>> getAllMeasures(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Measures");
        Page<Measure> page = measureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /measures/:id} : get the "id" measure.
     *
     * @param id the id of the measure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measures/{id}")
    public ResponseEntity<Measure> getMeasure(@PathVariable Long id) {
        log.debug("REST request to get Measure : {}", id);
        Optional<Measure> measure = measureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(measure);
    }

    /**
     * {@code DELETE  /measures/:id} : delete the "id" measure.
     *
     * @param id the id of the measure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measures/{id}")
    public ResponseEntity<Void> deleteMeasure(@PathVariable Long id) {
        log.debug("REST request to delete Measure : {}", id);
        measureRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
