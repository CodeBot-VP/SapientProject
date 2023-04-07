import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './movie.reducer';

export const MovieDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const movieEntity = useAppSelector(state => state.movie.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="movieDetailsHeading">Movie</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{movieEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{movieEntity.title}</dd>
          <dt>
            <span id="genre">Genre</span>
          </dt>
          <dd>{movieEntity.genre}</dd>
          <dt>Theatres</dt>
          <dd>
            {movieEntity.theatres
              ? movieEntity.theatres.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {movieEntity.theatres && i === movieEntity.theatres.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/movie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/movie/${movieEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MovieDetail;
