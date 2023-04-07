import dayjs from 'dayjs';
import { ISeat } from 'app/shared/model/seat.model';
import { ITheatres } from 'app/shared/model/theatres.model';

export interface IMovieShow {
  id?: number;
  movieName?: string | null;
  showTime?: string | null;
  seats?: ISeat[] | null;
  theatres?: ITheatres | null;
}

export const defaultValue: Readonly<IMovieShow> = {};
