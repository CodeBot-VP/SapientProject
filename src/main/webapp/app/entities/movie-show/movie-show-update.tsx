import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITheatres } from 'app/shared/model/theatres.model';
import { getEntities as getTheatres } from 'app/entities/theatres/theatres.reducer';
import { IMovieShow } from 'app/shared/model/movie-show.model';
import { getEntity, updateEntity, createEntity, reset } from './movie-show.reducer';

export const MovieShowUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const theatres = useAppSelector(state => state.theatres.entities);
  const movieShowEntity = useAppSelector(state => state.movieShow.entity);
  const loading = useAppSelector(state => state.movieShow.loading);
  const updating = useAppSelector(state => state.movieShow.updating);
  const updateSuccess = useAppSelector(state => state.movieShow.updateSuccess);

  const handleClose = () => {
    navigate('/movie-show');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTheatres({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.showTime = convertDateTimeToServer(values.showTime);

    const entity = {
      ...movieShowEntity,
      ...values,
      theatres: theatres.find(it => it.id.toString() === values.theatres.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          showTime: displayDefaultDateTime(),
        }
      : {
          ...movieShowEntity,
          showTime: convertDateTimeFromServer(movieShowEntity.showTime),
          theatres: movieShowEntity?.theatres?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookMyMovieApp.movieShow.home.createOrEditLabel" data-cy="MovieShowCreateUpdateHeading">
            Create or edit a Movie Show
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="movie-show-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Movie Name" id="movie-show-movieName" name="movieName" data-cy="movieName" type="text" />
              <ValidatedField
                label="Show Time"
                id="movie-show-showTime"
                name="showTime"
                data-cy="showTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="movie-show-theatres" name="theatres" data-cy="theatres" label="Theatres" type="select">
                <option value="" key="0" />
                {theatres
                  ? theatres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/movie-show" replace color="info">
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

export default MovieShowUpdate;
