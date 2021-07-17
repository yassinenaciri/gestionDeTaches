import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './pole.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PoleDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const poleEntity = useAppSelector(state => state.pole.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="poleDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.pole.detail.title">Pole</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{poleEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="gestionDeTachesApp.pole.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{poleEntity.nom}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.pole.chef">Chef</Translate>
          </dt>
          <dd>{poleEntity.chef ? poleEntity.chef.nomComplet : ''}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.pole.direction">Direction</Translate>
          </dt>
          <dd>{poleEntity.direction ? poleEntity.direction.nom : ''}</dd>
        </dl>
        <Button tag={Link} to="/pole" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pole/${poleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PoleDetail;
