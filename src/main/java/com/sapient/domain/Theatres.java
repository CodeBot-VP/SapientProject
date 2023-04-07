package com.sapient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Theatres.
 */
@Entity
@Table(name = "theatres")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Theatres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "theatre_name")
    private String theatreName;

    @Column(name = "theatre_city")
    private String theatreCity;

    @OneToMany(mappedBy = "theatres")
    @JsonIgnoreProperties(value = { "seats", "theatres" }, allowSetters = true)
    private Set<MovieShow> movieShows = new HashSet<>();

    @ManyToMany(mappedBy = "theatres")
    @JsonIgnoreProperties(value = { "theatres" }, allowSetters = true)
    private Set<Movie> movies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Theatres id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheatreName() {
        return this.theatreName;
    }

    public Theatres theatreName(String theatreName) {
        this.setTheatreName(theatreName);
        return this;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getTheatreCity() {
        return this.theatreCity;
    }

    public Theatres theatreCity(String theatreCity) {
        this.setTheatreCity(theatreCity);
        return this;
    }

    public void setTheatreCity(String theatreCity) {
        this.theatreCity = theatreCity;
    }

    public Set<MovieShow> getMovieShows() {
        return this.movieShows;
    }

    public void setMovieShows(Set<MovieShow> movieShows) {
        if (this.movieShows != null) {
            this.movieShows.forEach(i -> i.setTheatres(null));
        }
        if (movieShows != null) {
            movieShows.forEach(i -> i.setTheatres(this));
        }
        this.movieShows = movieShows;
    }

    public Theatres movieShows(Set<MovieShow> movieShows) {
        this.setMovieShows(movieShows);
        return this;
    }

    public Theatres addMovieShows(MovieShow movieShow) {
        this.movieShows.add(movieShow);
        movieShow.setTheatres(this);
        return this;
    }

    public Theatres removeMovieShows(MovieShow movieShow) {
        this.movieShows.remove(movieShow);
        movieShow.setTheatres(null);
        return this;
    }

    public Set<Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(Set<Movie> movies) {
        if (this.movies != null) {
            this.movies.forEach(i -> i.removeTheatres(this));
        }
        if (movies != null) {
            movies.forEach(i -> i.addTheatres(this));
        }
        this.movies = movies;
    }

    public Theatres movies(Set<Movie> movies) {
        this.setMovies(movies);
        return this;
    }

    public Theatres addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getTheatres().add(this);
        return this;
    }

    public Theatres removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getTheatres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theatres)) {
            return false;
        }
        return id != null && id.equals(((Theatres) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Theatres{" +
            "id=" + getId() +
            ", theatreName='" + getTheatreName() + "'" +
            ", theatreCity='" + getTheatreCity() + "'" +
            "}";
    }
}
