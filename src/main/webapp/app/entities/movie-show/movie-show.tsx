import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMovieShow } from 'app/shared/model/movie-show.model';
import { getEntities } from './movie-show.reducer';

export const MovieShow = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const movieShowList = useAppSelector(state => state.movieShow.entities);
  const loading = useAppSelector(state => state.movieShow.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="movie-show-heading" data-cy="MovieShowHeading">
        Movie Shows
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/movie-show/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Movie Show
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {movieShowList && movieShowList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Movie Name</th>
                <th>Show Time</th>
                <th>Theatres</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {movieShowList.map((movieShow, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/movie-show/${movieShow.id}`} color="link" size="sm">
                      {movieShow.id}
                    </Button>
                  </td>
                  <td>{movieShow.movieName}</td>
                  <td>{movieShow.showTime ? <TextFormat type="date" value={movieShow.showTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{movieShow.theatres ? <Link to={`/theatres/${movieShow.theatres.id}`}>{movieShow.theatres.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/movie-show/${movieShow.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/movie-show/${movieShow.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/movie-show/${movieShow.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Movie Shows found</div>
        )}
      </div>
    </div>
  );
};

export default MovieShow;
