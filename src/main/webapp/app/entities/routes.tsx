import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Theatres from './theatres';
import MovieShow from './movie-show';
import Movie from './movie';
import Seat from './seat';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="theatres/*" element={<Theatres />} />
        <Route path="movie-show/*" element={<MovieShow />} />
        <Route path="movie/*" element={<Movie />} />
        <Route path="seat/*" element={<Seat />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
