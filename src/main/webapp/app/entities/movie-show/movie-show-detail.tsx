import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './movie-show.reducer';

export const MovieShowDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const movieShowEntity = useAppSelector(state => state.movieShow.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="movieShowDetailsHeading">Movie Show</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{movieShowEntity.id}</dd>
          <dt>
            <span id="movieName">Movie Name</span>
          </dt>
          <dd>{movieShowEntity.movieName}</dd>
          <dt>
            <span id="showTime">Show Time</span>
          </dt>
          <dd>{movieShowEntity.showTime ? <TextFormat value={movieShowEntity.showTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Theatres</dt>
          <dd>{movieShowEntity.theatres ? movieShowEntity.theatres.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/movie-show" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/movie-show/${movieShowEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MovieShowDetail;
