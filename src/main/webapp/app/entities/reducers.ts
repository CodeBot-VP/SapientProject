import theatres from 'app/entities/theatres/theatres.reducer';
import movieShow from 'app/entities/movie-show/movie-show.reducer';
import movie from 'app/entities/movie/movie.reducer';
import seat from 'app/entities/seat/seat.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  theatres,
  movieShow,
  movie,
  seat,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
