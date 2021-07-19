import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IIService } from 'app/shared/model/i-service.model';
import { getEntities as getIServices } from 'app/entities/i-service/i-service.reducer';
import { IEmploye } from 'app/shared/model/employe.model';
import { getEntities as getEmployes } from 'app/entities/employe/employe.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tache.reducer';
import { ITache } from 'app/shared/model/tache.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TacheUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const iServices = useAppSelector(state => state.iService.entities);
  const employes = useAppSelector(state => state.employe.entities);
  const tacheEntity = useAppSelector(state => state.tache.entity);
  const loading = useAppSelector(state => state.tache.loading);
  const updating = useAppSelector(state => state.tache.updating);
  const updateSuccess = useAppSelector(state => state.tache.updateSuccess);

  const handleClose = () => {
    props.history.push('/tache' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getIServices({}));
    dispatch(getEmployes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateLimite = convertDateTimeToServer(values.dateLimite);
    values.dateDebut = convertDateTimeToServer(values.dateDebut);
    values.dateFin = convertDateTimeToServer(values.dateFin);

    const entity = {
      ...tacheEntity,
      ...values,
      service: iServices.find(it => it.id.toString() === values.serviceId.toString()),
      cadreAffecte: employes.find(it => it.id.toString() === values.cadreAffecteId.toString()),
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
          dateLimite: displayDefaultDateTime(),
          dateDebut: displayDefaultDateTime(),
          dateFin: displayDefaultDateTime(),
        }
      : {
          ...tacheEntity,
          dateLimite: convertDateTimeFromServer(tacheEntity.dateLimite),
          etat: 'NonCommence',
          dateDebut: convertDateTimeFromServer(tacheEntity.dateDebut),
          dateFin: convertDateTimeFromServer(tacheEntity.dateFin),
          serviceId: tacheEntity?.service?.id,
          cadreAffecteId: tacheEntity?.cadreAffecte?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gestionDeTachesApp.tache.home.createOrEditLabel" data-cy="TacheCreateUpdateHeading">
            <Translate contentKey="gestionDeTachesApp.tache.home.createOrEditLabel">Create or edit a Tache</Translate>
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
                  id="tache-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gestionDeTachesApp.tache.intitule')}
                id="tache-intitule"
                name="intitule"
                data-cy="intitule"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gestionDeTachesApp.tache.dateLimite')}
                id="tache-dateLimite"
                name="dateLimite"
                data-cy="dateLimite"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gestionDeTachesApp.tache.description')}
                id="tache-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('gestionDeTachesApp.tache.etat')} id="tache-etat" name="etat" data-cy="etat" type="select">
                <option value="NonCommence">{translate('gestionDeTachesApp.Etat.NonCommence')}</option>
                <option value="Encours">{translate('gestionDeTachesApp.Etat.Encours')}</option>
                <option value="Termine">{translate('gestionDeTachesApp.Etat.Termine')}</option>
                <option value="Abondonne">{translate('gestionDeTachesApp.Etat.Abondonne')}</option>
                <option value="Valide">{translate('gestionDeTachesApp.Etat.Valide')}</option>
                <option value="Refuse">{translate('gestionDeTachesApp.Etat.Refuse')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('gestionDeTachesApp.tache.dateDebut')}
                id="tache-dateDebut"
                name="dateDebut"
                data-cy="dateDebut"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('gestionDeTachesApp.tache.dateFin')}
                id="tache-dateFin"
                name="dateFin"
                data-cy="dateFin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="tache-service"
                name="serviceId"
                data-cy="service"
                label={translate('gestionDeTachesApp.tache.service')}
                type="select"
              >
                <option value="" key="0" />
                {iServices
                  ? iServices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nomService}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="tache-cadreAffecte"
                name="cadreAffecteId"
                data-cy="cadreAffecte"
                label={translate('gestionDeTachesApp.tache.cadreAffecte')}
                type="select"
              >
                <option value="" key="0" />
                {employes
                  ? employes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nomComplet}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tache" replace color="info">
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

export default TacheUpdate;
