import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMovie } from 'app/shared/model/movie.model';
import { getEntities as getMovies } from 'app/entities/movie/movie.reducer';
import { ITheatres } from 'app/shared/model/theatres.model';
import { getEntity, updateEntity, createEntity, reset } from './theatres.reducer';

export const TheatresUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const movies = useAppSelector(state => state.movie.entities);
  const theatresEntity = useAppSelector(state => state.theatres.entity);
  const loading = useAppSelector(state => state.theatres.loading);
  const updating = useAppSelector(state => state.theatres.updating);
  const updateSuccess = useAppSelector(state => state.theatres.updateSuccess);

  const handleClose = () => {
    navigate('/theatres');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMovies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...theatresEntity,
      ...values,
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
          ...theatresEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="bookMyMovieApp.theatres.home.createOrEditLabel" data-cy="TheatresCreateUpdateHeading">
            Create or edit a Theatres
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="theatres-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Theatre Name" id="theatres-theatreName" name="theatreName" data-cy="theatreName" type="text" />
              <ValidatedField label="Theatre City" id="theatres-theatreCity" name="theatreCity" data-cy="theatreCity" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/theatres" replace color="info">
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

export default TheatresUpdate;
