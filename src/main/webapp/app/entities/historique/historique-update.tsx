import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITache } from 'app/shared/model/tache.model';
import { getEntities as getTaches } from 'app/entities/tache/tache.reducer';
import { IEmploye } from 'app/shared/model/employe.model';
import { getEntities as getEmployes } from 'app/entities/employe/employe.reducer';
import { getEntity, updateEntity, createEntity, reset } from './historique.reducer';
import { IHistorique } from 'app/shared/model/historique.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const HistoriqueUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const taches = useAppSelector(state => state.tache.entities);
  const employes = useAppSelector(state => state.employe.entities);
  const historiqueEntity = useAppSelector(state => state.historique.entity);
  const loading = useAppSelector(state => state.historique.loading);
  const updating = useAppSelector(state => state.historique.updating);
  const updateSuccess = useAppSelector(state => state.historique.updateSuccess);

  const handleClose = () => {
    props.history.push('/historique' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTaches({}));
    dispatch(getEmployes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateDebut = convertDateTimeToServer(values.dateDebut);
    values.dateFin = convertDateTimeToServer(values.dateFin);

    const entity = {
      ...historiqueEntity,
      ...values,
      tache: taches.find(it => it.id.toString() === values.tacheId.toString()),
      cadre: employes.find(it => it.id.toString() === values.cadreId.toString()),
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
          dateDebut: displayDefaultDateTime(),
          dateFin: displayDefaultDateTime(),
        }
      : {
          ...historiqueEntity,
          dateDebut: convertDateTimeFromServer(historiqueEntity.dateDebut),
          dateFin: convertDateTimeFromServer(historiqueEntity.dateFin),
          tacheId: historiqueEntity?.tache?.id,
          cadreId: historiqueEntity?.cadre?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gestionDeTachesApp.historique.home.createOrEditLabel" data-cy="HistoriqueCreateUpdateHeading">
            <Translate contentKey="gestionDeTachesApp.historique.home.createOrEditLabel">Create or edit a Historique</Translate>
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
                  id="historique-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gestionDeTachesApp.historique.dateDebut')}
                id="historique-dateDebut"
                name="dateDebut"
                data-cy="dateDebut"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="dateDebutLabel">
                <Translate contentKey="gestionDeTachesApp.historique.help.dateDebut" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('gestionDeTachesApp.historique.dateFin')}
                id="historique-dateFin"
                name="dateFin"
                data-cy="dateFin"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="historique-tache"
                name="tacheId"
                data-cy="tache"
                label={translate('gestionDeTachesApp.historique.tache')}
                type="select"
                required
              >
                <option value="" key="0" />
                {taches
                  ? taches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nom}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="historique-cadre"
                name="cadreId"
                data-cy="cadre"
                label={translate('gestionDeTachesApp.historique.cadre')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/historique" replace color="info">
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

export default HistoriqueUpdate;
