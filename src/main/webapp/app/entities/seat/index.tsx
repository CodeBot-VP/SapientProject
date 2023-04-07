import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Seat from './seat';
import SeatDetail from './seat-detail';
import SeatUpdate from './seat-update';
import SeatDeleteDialog from './seat-delete-dialog';

const SeatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Seat />} />
    <Route path="new" element={<SeatUpdate />} />
    <Route path=":id">
      <Route index element={<SeatDetail />} />
      <Route path="edit" element={<SeatUpdate />} />
      <Route path="delete" element={<SeatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeatRoutes;
