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
import { IMovie } from 'app/shared/model/movie.model';
import { getEntity, updateEntity, createEntity, reset } from './movie.reducer';

export const MovieUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const theatres = useAppSelector(state => state.theatres.entities);
  const movieEntity = useAppSelector(state => state.movie.entity);
  const loading = useAppSelector(state => state.movie.loading);
  const updating = useAppSelector(state => state.movie.updating);
  const updateSuccess = useAppSelector(state => state.movie.updateSuccess);

  const handleClose = () => {
    navigate('/movie');
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
    const entity = {
      ...movieEntity,
      ...values,
      theatres: mapIdList(values.theatres),
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
          ...movieEntity,
          theatres: movieEntity?.theatres?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookMyMovieApp.movie.home.createOrEditLabel" data-cy="MovieCreateUpdateHeading">
            Create or edit a Movie
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="movie-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Title" id="movie-title" name="title" data-cy="title" type="text" />
              <ValidatedField label="Genre" id="movie-genre" name="genre" data-cy="genre" type="text" />
              <ValidatedField label="Theatres" id="movie-theatres" data-cy="theatres" type="select" multiple name="theatres">
                <option value="" key="0" />
                {theatres
                  ? theatres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/movie" replace color="info">
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

export default MovieUpdate;
