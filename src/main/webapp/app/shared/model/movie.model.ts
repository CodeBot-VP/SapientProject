import { ITheatres } from 'app/shared/model/theatres.model';

export interface IMovie {
  id?: number;
  title?: string | null;
  genre?: string | null;
  theatres?: ITheatres[] | null;
}

export const defaultValue: Readonly<IMovie> = {};
