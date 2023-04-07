import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMovieShow } from 'app/shared/model/movie-show.model';
import { getEntities as getMovieShows } from 'app/entities/movie-show/movie-show.reducer';
import { ISeat } from 'app/shared/model/seat.model';
import { SeatStatus } from 'app/shared/model/enumerations/seat-status.model';
import { getEntity, updateEntity, createEntity, reset } from './seat.reducer';

export const SeatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const movieShows = useAppSelector(state => state.movieShow.entities);
  const seatEntity = useAppSelector(state => state.seat.entity);
  const loading = useAppSelector(state => state.seat.loading);
  const updating = useAppSelector(state => state.seat.updating);
  const updateSuccess = useAppSelector(state => state.seat.updateSuccess);
  const seatStatusValues = Object.keys(SeatStatus);

  const handleClose = () => {
    navigate('/seat');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMovieShows({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...seatEntity,
      ...values,
      movieShow: movieShows.find(it => it.id.toString() === values.movieShow.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'AVAILABLE',
          ...seatEntity,
          movieShow: seatEntity?.movieShow?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookMyMovieApp.seat.home.createOrEditLabel" data-cy="SeatCreateUpdateHeading">
            Create or edit a Seat
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="seat-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField label="Seat Number" id="seat-seatNumber" name="seatNumber" data-cy="seatNumber" type="text" />
              <ValidatedField label="Status" id="seat-status" name="status" data-cy="status" type="select">
                {seatStatusValues.map(seatStatus => (
                  <option value={seatStatus} key={seatStatus}>
                    {seatStatus}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="seat-movieShow" name="movieShow" data-cy="movieShow" label="Movie Show" type="select">
                <option value="" key="0" />
                {movieShows
                  ? movieShows.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/seat" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SeatUpdate;
