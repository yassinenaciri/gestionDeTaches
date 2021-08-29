import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IChef } from 'app/shared/model/chef.model';
import { getEntities as getChefs } from 'app/entities/chef/chef.reducer';
import { IDivision } from 'app/shared/model/division.model';
import { getEntities as getDivisions } from 'app/entities/division/division.reducer';
import { getEntity, updateEntity, createEntity, reset } from './i-service.reducer';
import { IIService } from 'app/shared/model/i-service.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { AUTHORITIES } from 'app/config/constants';

export const IServiceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const chefs = useAppSelector(state => state.chef.entities);
  const divisions = useAppSelector(state => state.division.entities);
  const iServiceEntity = useAppSelector(state => state.iService.entity);
  const loading = useAppSelector(state => state.iService.loading);
  const updating = useAppSelector(state => state.iService.updating);
  const updateSuccess = useAppSelector(state => state.iService.updateSuccess);

  const handleClose = () => {
    props.history.push('/i-service' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getChefs({}));
    dispatch(getDivisions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...iServiceEntity,
      ...values,
      chef: chefs.find(it => it.id.toString() === values.chefId.toString()),
      division: divisions.find(it => it.id.toString() === values.divisionId.toString()),
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
          ...iServiceEntity,
          chefId: iServiceEntity?.chef?.id,
          divisionId: iServiceEntity?.division?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gestionDeTachesApp.iService.home.createOrEditLabel" data-cy="IServiceCreateUpdateHeading">
            <Translate contentKey="gestionDeTachesApp.iService.home.createOrEditLabel">Create or edit a IService</Translate>
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
                  id="i-service-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gestionDeTachesApp.iService.nomService')}
                id="i-service-nomService"
                name="nomService"
                data-cy="nomService"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="i-service-chef"
                name="chefId"
                data-cy="chef"
                label={translate('gestionDeTachesApp.iService.chef')}
                type="select"
                required
              >
                <option value="" key="0" />
                {chefs
                  ? chefs.map(otherEntity => {
                      if (otherEntity.role === AUTHORITIES.CHEFSERVICE && !otherEntity.service)
                        return (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nomComplet}
                          </option>
                        );
                      else {
                        return;
                      }
                    })
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="i-service-division"
                name="divisionId"
                data-cy="division"
                label={translate('gestionDeTachesApp.iService.division')}
                type="select"
                required
              >
                <option value="" key="0" />
                {divisions
                  ? divisions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nomDivision}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/i-service" replace color="info">
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

export default IServiceUpdate;
