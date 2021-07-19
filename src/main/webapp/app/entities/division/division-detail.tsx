import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './division.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DivisionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const divisionEntity = useAppSelector(state => state.division.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="divisionDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.division.detail.title">Division</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{divisionEntity.id}</dd>
          <dt>
            <span id="nomDivision">
              <Translate contentKey="gestionDeTachesApp.division.nomDivision">Nom Division</Translate>
            </span>
          </dt>
          <dd>{divisionEntity.nomDivision}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.division.chef">Chef</Translate>
          </dt>
          <dd>{divisionEntity.chef ? divisionEntity.chef.nomComplet : ''}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.division.pole">Pole</Translate>
          </dt>
          <dd>{divisionEntity.pole ? divisionEntity.pole.nomPole : ''}</dd>
        </dl>
        <Button tag={Link} to="/division" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/division/${divisionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DivisionDetail;
