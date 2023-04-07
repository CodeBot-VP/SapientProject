import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MovieShow from './movie-show';
import MovieShowDetail from './movie-show-detail';
import MovieShowUpdate from './movie-show-update';
import MovieShowDeleteDialog from './movie-show-delete-dialog';

const MovieShowRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MovieShow />} />
    <Route path="new" element={<MovieShowUpdate />} />
    <Route path=":id">
      <Route index element={<MovieShowDetail />} />
      <Route path="edit" element={<MovieShowUpdate />} />
      <Route path="delete" element={<MovieShowDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MovieShowRoutes;
