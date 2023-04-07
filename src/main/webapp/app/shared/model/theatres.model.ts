import { IMovieShow } from 'app/shared/model/movie-show.model';
import { IMovie } from 'app/shared/model/movie.model';

export interface ITheatres {
  id?: number;
  theatreName?: string | null;
  theatreCity?: string | null;
  movieShows?: IMovieShow[] | null;
  movies?: IMovie[] | null;
}

export const defaultValue: Readonly<ITheatres> = {};
