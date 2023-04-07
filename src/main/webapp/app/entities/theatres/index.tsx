import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Theatres from './theatres';
import TheatresDetail from './theatres-detail';
import TheatresUpdate from './theatres-update';
import TheatresDeleteDialog from './theatres-delete-dialog';

const TheatresRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Theatres />} />
    <Route path="new" element={<TheatresUpdate />} />
    <Route path=":id">
      <Route index element={<TheatresDetail />} />
      <Route path="edit" element={<TheatresUpdate />} />
      <Route path="delete" element={<TheatresDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TheatresRoutes;
