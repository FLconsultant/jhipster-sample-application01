package io.github.jhipster.application01.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * A Measure.
 */
@Entity
@Table(name = "measure")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Measure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "measuresource")
    private String measuresource;

    @Column(name = "measurevalue")
    private Float measurevalue;

    @Column(name = "measurevaluelong")
    private Long measurevaluelong;

    @Column(name = "measuredate")
    private LocalDate measuredate;

    @Column(name = "measuredatetime")
    private ZonedDateTime measuredatetime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasuresource() {
        return measuresource;
    }

    public Measure measuresource(String measuresource) {
        this.measuresource = measuresource;
        return this;
    }

    public void setMeasuresource(String measuresource) {
        this.measuresource = measuresource;
    }

    public Float getMeasurevalue() {
        return measurevalue;
    }

    public Measure measurevalue(Float measurevalue) {
        this.measurevalue = measurevalue;
        return this;
    }

    public void setMeasurevalue(Float measurevalue) {
        this.measurevalue = measurevalue;
    }

    public Long getMeasurevaluelong() {
        return measurevaluelong;
    }

    public Measure measurevaluelong(Long measurevaluelong) {
        this.measurevaluelong = measurevaluelong;
        return this;
    }

    public void setMeasurevaluelong(Long measurevaluelong) {
        this.measurevaluelong = measurevaluelong;
    }

    public LocalDate getMeasuredate() {
        return measuredate;
    }

    public Measure measuredate(LocalDate measuredate) {
        this.measuredate = measuredate;
        return this;
    }

    public void setMeasuredate(LocalDate measuredate) {
        this.measuredate = measuredate;
    }

    public ZonedDateTime getMeasuredatetime() {
        return measuredatetime;
    }

    public Measure measuredatetime(ZonedDateTime measuredatetime) {
        this.measuredatetime = measuredatetime;
        return this;
    }

    public void setMeasuredatetime(ZonedDateTime measuredatetime) {
        this.measuredatetime = measuredatetime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Measure)) {
            return false;
        }
        return id != null && id.equals(((Measure) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Measure{" +
            "id=" + getId() +
            ", measuresource='" + getMeasuresource() + "'" +
            ", measurevalue=" + getMeasurevalue() +
            ", measurevaluelong=" + getMeasurevaluelong() +
            ", measuredate='" + getMeasuredate() + "'" +
            ", measuredatetime='" + getMeasuredatetime() + "'" +
            "}";
    }
}
