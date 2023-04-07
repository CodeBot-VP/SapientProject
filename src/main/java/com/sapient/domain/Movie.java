package com.sapient.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "genre")
    private String genre;

    @ManyToMany
    @JoinTable(
        name = "rel_movie__theatres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "theatres_id")
    )
    @JsonIgnoreProperties(value = { "movieShows", "movies" }, allowSetters = true)
    private Set<Theatres> theatres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Movie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Movie title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return this.genre;
    }

    public Movie genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Theatres> getTheatres() {
        return this.theatres;
    }

    public void setTheatres(Set<Theatres> theatres) {
        this.theatres = theatres;
    }

    public Movie theatres(Set<Theatres> theatres) {
        this.setTheatres(theatres);
        return this;
    }

    public Movie addTheatres(Theatres theatres) {
        this.theatres.add(theatres);
        theatres.getMovies().add(this);
        return this;
    }

    public Movie removeTheatres(Theatres theatres) {
        this.theatres.remove(theatres);
        theatres.getMovies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
