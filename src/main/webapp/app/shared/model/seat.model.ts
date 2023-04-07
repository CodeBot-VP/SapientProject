import { IMovieShow } from 'app/shared/model/movie-show.model';
import { SeatStatus } from 'app/shared/model/enumerations/seat-status.model';

export interface ISeat {
  id?: number;
  seatNumber?: string | null;
  status?: SeatStatus | null;
  movieShow?: IMovieShow | null;
}

export const defaultValue: Readonly<ISeat> = {};
