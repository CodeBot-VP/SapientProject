package com.sapient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sapient.domain.enumeration.SeatStatus;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Seat.
 */
@Entity
@Table(name = "seat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seats", "theatres" }, allowSetters = true)
    private MovieShow movieShow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return this.seatNumber;
    }

    public Seat seatNumber(String seatNumber) {
        this.setSeatNumber(seatNumber);
        return this;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatStatus getStatus() {
        return this.status;
    }

    public Seat status(SeatStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public MovieShow getMovieShow() {
        return this.movieShow;
    }

    public void setMovieShow(MovieShow movieShow) {
        this.movieShow = movieShow;
    }

    public Seat movieShow(MovieShow movieShow) {
        this.setMovieShow(movieShow);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        return id != null && id.equals(((Seat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seat{" +
            "id=" + getId() +
            ", seatNumber='" + getSeatNumber() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
