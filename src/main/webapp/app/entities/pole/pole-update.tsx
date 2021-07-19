import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IChef } from 'app/shared/model/chef.model';
import { getEntities as getChefs } from 'app/entities/chef/chef.reducer';
import { IDirection } from 'app/shared/model/direction.model';
import { getEntities as getDirections } from 'app/entities/direction/direction.reducer';
import { getEntity, updateEntity, createEntity, reset } from './pole.reducer';
import { IPole } from 'app/shared/model/pole.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PoleUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const chefs = useAppSelector(state => state.chef.entities);
  const directions = useAppSelector(state => state.direction.entities);
  const poleEntity = useAppSelector(state => state.pole.entity);
  const loading = useAppSelector(state => state.pole.loading);
  const updating = useAppSelector(state => state.pole.updating);
  const updateSuccess = useAppSelector(state => state.pole.updateSuccess);

  const handleClose = () => {
    props.history.push('/pole' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getChefs({}));
    dispatch(getDirections({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...poleEntity,
      ...values,
      chef: chefs.find(it => it.id.toString() === values.chefId.toString()),
      direction: directions.find(it => it.id.toString() === values.directionId.toString()),
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
          ...poleEntity,
          chefId: poleEntity?.chef?.id,
          directionId: poleEntity?.direction?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gestionDeTachesApp.pole.home.createOrEditLabel" data-cy="PoleCreateUpdateHeading">
            <Translate contentKey="gestionDeTachesApp.pole.home.createOrEditLabel">Create or edit a Pole</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="pole-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gestionDeTachesApp.pole.nomPole')}
                id="pole-nomPole"
                name="nomPole"
                data-cy="nomPole"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="pole-chef"
                name="chefId"
                data-cy="chef"
                label={translate('gestionDeTachesApp.pole.chef')}
                type="select"
                required
              >
                <option value="" key="0" />
                {chefs
                  ? chefs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nomComplet}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="pole-direction"
                name="directionId"
                data-cy="direction"
                label={translate('gestionDeTachesApp.pole.direction')}
                type="select"
                required
              >
                <option value="" key="0" />
                {directions
                  ? directions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nomDirection}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pole" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PoleUpdate;
