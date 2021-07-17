import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './i-service.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const IServiceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const iServiceEntity = useAppSelector(state => state.iService.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="iServiceDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.iService.detail.title">IService</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{iServiceEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="gestionDeTachesApp.iService.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{iServiceEntity.nom}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.iService.chef">Chef</Translate>
          </dt>
          <dd>{iServiceEntity.chef ? iServiceEntity.chef.nomComplet : ''}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.iService.division">Division</Translate>
          </dt>
          <dd>{iServiceEntity.division ? iServiceEntity.division.nom : ''}</dd>
        </dl>
        <Button tag={Link} to="/i-service" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/i-service/${iServiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IServiceDetail;
