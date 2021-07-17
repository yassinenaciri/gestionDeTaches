import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IChef } from 'app/shared/model/chef.model';
import { getEntities as getChefs } from 'app/entities/chef/chef.reducer';
import { IPole } from 'app/shared/model/pole.model';
import { getEntities as getPoles } from 'app/entities/pole/pole.reducer';
import { getEntity, updateEntity, createEntity, reset } from './division.reducer';
import { IDivision } from 'app/shared/model/division.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DivisionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const chefs = useAppSelector(state => state.chef.entities);
  const poles = useAppSelector(state => state.pole.entities);
  const divisionEntity = useAppSelector(state => state.division.entity);
  const loading = useAppSelector(state => state.division.loading);
  const updating = useAppSelector(state => state.division.updating);
  const updateSuccess = useAppSelector(state => state.division.updateSuccess);

  const handleClose = () => {
    props.history.push('/division' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getChefs({}));
    dispatch(getPoles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...divisionEntity,
      ...values,
      chef: chefs.find(it => it.id.toString() === values.chefId.toString()),
      pole: poles.find(it => it.id.toString() === values.poleId.toString()),
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
          ...divisionEntity,
          chefId: divisionEntity?.chef?.id,
          poleId: divisionEntity?.pole?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gestionDeTachesApp.division.home.createOrEditLabel" data-cy="DivisionCreateUpdateHeading">
            <Translate contentKey="gestionDeTachesApp.division.home.createOrEditLabel">Create or edit a Division</Translate>
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
                  id="division-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gestionDeTachesApp.division.nom')}
                id="division-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="division-chef"
                name="chefId"
                data-cy="chef"
                label={translate('gestionDeTachesApp.division.chef')}
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
                id="division-pole"
                name="poleId"
                data-cy="pole"
                label={translate('gestionDeTachesApp.division.pole')}
                type="select"
                required
              >
                <option value="" key="0" />
                {poles
                  ? poles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/division" replace color="info">
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

export default DivisionUpdate;
