import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './chef.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ChefDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const chefEntity = useAppSelector(state => state.chef.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chefDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.chef.detail.title">Chef</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chefEntity.id}</dd>
          <dt>
            <span id="nomComplet">
              <Translate contentKey="gestionDeTachesApp.chef.nomComplet">Nom Complet</Translate>
            </span>
          </dt>
          <dd>{chefEntity.nomComplet}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="gestionDeTachesApp.chef.role">Role</Translate>
            </span>
          </dt>
          <dd>{chefEntity.role}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.chef.compte">Compte</Translate>
          </dt>
          <dd>{chefEntity.compte ? chefEntity.compte.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/chef" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chef/${chefEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChefDetail;
