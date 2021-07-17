import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tache.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TacheDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tacheEntity = useAppSelector(state => state.tache.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tacheDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.tache.detail.title">Tache</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tacheEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="gestionDeTachesApp.tache.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{tacheEntity.nom}</dd>
          <dt>
            <span id="dureEstime">
              <Translate contentKey="gestionDeTachesApp.tache.dureEstime">Dure Estime</Translate>
            </span>
          </dt>
          <dd>
            {tacheEntity.dureEstime ? <DurationFormat value={tacheEntity.dureEstime} /> : null} ({tacheEntity.dureEstime})
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gestionDeTachesApp.tache.description">Description</Translate>
            </span>
          </dt>
          <dd>{tacheEntity.description}</dd>
          <dt>
            <span id="etat">
              <Translate contentKey="gestionDeTachesApp.tache.etat">Etat</Translate>
            </span>
          </dt>
          <dd>{tacheEntity.etat ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.tache.service">Service</Translate>
          </dt>
          <dd>{tacheEntity.service ? tacheEntity.service.nom : ''}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.tache.cadreAffecte">Cadre Affecte</Translate>
          </dt>
          <dd>{tacheEntity.cadreAffecte ? tacheEntity.cadreAffecte.nomComplet : ''}</dd>
        </dl>
        <Button tag={Link} to="/tache" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tache/${tacheEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TacheDetail;
