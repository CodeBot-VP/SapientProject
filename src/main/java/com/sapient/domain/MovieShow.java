package com.sapient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A MovieShow.
 */
@Entity
@Table(name = "movie_show")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovieShow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "movie_name")
    private String movieName;

    @Column(name = "show_time")
    private Instant showTime;

    @OneToMany(mappedBy = "movieShow")
    @JsonIgnoreProperties(value = { "movieShow" }, allowSetters = true)
    private Set<Seat> seats = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "movieShows", "movies" }, allowSetters = true)
    private Theatres theatres;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MovieShow id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieName() {
        return this.movieName;
    }

    public MovieShow movieName(String movieName) {
        this.setMovieName(movieName);
        return this;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Instant getShowTime() {
        return this.showTime;
    }

    public MovieShow showTime(Instant showTime) {
        this.setShowTime(showTime);
        return this;
    }

    public void setShowTime(Instant showTime) {
        this.showTime = showTime;
    }

    public Set<Seat> getSeats() {
        return this.seats;
    }

    public void setSeats(Set<Seat> seats) {
        if (this.seats != null) {
            this.seats.forEach(i -> i.setMovieShow(null));
        }
        if (seats != null) {
            seats.forEach(i -> i.setMovieShow(this));
        }
        this.seats = seats;
    }

    public MovieShow seats(Set<Seat> seats) {
        this.setSeats(seats);
        return this;
    }

    public MovieShow addSeats(Seat seat) {
        this.seats.add(seat);
        seat.setMovieShow(this);
        return this;
    }

    public MovieShow removeSeats(Seat seat) {
        this.seats.remove(seat);
        seat.setMovieShow(null);
        return this;
    }

    public Theatres getTheatres() {
        return this.theatres;
    }

    public void setTheatres(Theatres theatres) {
        this.theatres = theatres;
    }

    public MovieShow theatres(Theatres theatres) {
        this.setTheatres(theatres);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovieShow)) {
            return false;
        }
        return id != null && id.equals(((MovieShow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovieShow{" +
            "id=" + getId() +
            ", movieName='" + getMovieName() + "'" +
            ", showTime='" + getShowTime() + "'" +
            "}";
    }
}
