import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './direction.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DirectionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const directionEntity = useAppSelector(state => state.direction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="directionDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.direction.detail.title">Direction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{directionEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="gestionDeTachesApp.direction.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{directionEntity.nom}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.direction.directeur">Directeur</Translate>
          </dt>
          <dd>{directionEntity.directeur ? directionEntity.directeur.nomComplet : ''}</dd>
        </dl>
        <Button tag={Link} to="/direction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/direction/${directionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DirectionDetail;
