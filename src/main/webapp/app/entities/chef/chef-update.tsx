import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IIService } from 'app/shared/model/i-service.model';
import { getEntities as getIServices } from 'app/entities/i-service/i-service.reducer';
import { IPole } from 'app/shared/model/pole.model';
import { getEntities as getPoles } from 'app/entities/pole/pole.reducer';
import { IDirection } from 'app/shared/model/direction.model';
import { getEntities as getDirections } from 'app/entities/direction/direction.reducer';
import { IDivision } from 'app/shared/model/division.model';
import { getEntities as getDivisions } from 'app/entities/division/division.reducer';
import { getEntity, updateEntity, createEntity, reset } from './chef.reducer';
import { IChef } from 'app/shared/model/chef.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Etat } from 'app/shared/model/enumerations/etat.model';
import { AUTHORITIES } from 'app/config/constants';

export const ChefUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const iServices = useAppSelector(state => state.iService.entities);
  const poles = useAppSelector(state => state.pole.entities);
  const directions = useAppSelector(state => state.direction.entities);
  const divisions = useAppSelector(state => state.division.entities);
  const chefEntity = useAppSelector(state => state.chef.entity);
  const loading = useAppSelector(state => state.chef.loading);
  const updating = useAppSelector(state => state.chef.updating);
  const updateSuccess = useAppSelector(state => state.chef.updateSuccess);

  const handleClose = () => {
    props.history.push('/chef' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getIServices({}));
    dispatch(getPoles({}));
    dispatch(getDirections({}));
    dispatch(getDivisions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...chefEntity,
      ...values,
      compte: users.find(it => it.id.toString() === values.compteId.toString()),
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
          ...chefEntity,
          compteId: chefEntity?.compte?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gestionDeTachesApp.chef.home.createOrEditLabel" data-cy="ChefCreateUpdateHeading">
            <Translate contentKey="gestionDeTachesApp.chef.home.createOrEditLabel">Create or edit a Chef</Translate>
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
                  id="chef-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gestionDeTachesApp.chef.nomComplet')}
                id="chef-nomComplet"
                name="nomComplet"
                data-cy="nomComplet"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField id="chef-role" name="role" data-cy="role" label={translate('gestionDeTachesApp.chef.role')} type="select">
                <option value={AUTHORITIES.CHEFSERVICE} key="0">
                  {' '}
                  Chef de Service
                </option>
                <option value={AUTHORITIES.CHEFDIVISION} key="0">
                  Chef de Division
                </option>
                <option value={AUTHORITIES.CHEFPOLE} key="0">
                  Chef de Pole
                </option>
                <option value={AUTHORITIES.DIRECTEUR} key="0">
                  Directeur
                </option>
              </ValidatedField>
              <ValidatedField
                id="chef-compte"
                name="compteId"
                data-cy="compte"
                label={translate('gestionDeTachesApp.chef.compte')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.email}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chef" replace color="info">
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

export default ChefUpdate;
