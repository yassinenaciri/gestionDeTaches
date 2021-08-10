import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import '../list.css';
import { getEntity } from './employe.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { Chart } from 'app/entities/tache/chart';

export const EmployeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const employeEntity = useAppSelector(state => state.employe.entity);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  return (
    <Row>
      <Col md="3" className="pad">
        <Chart id={employeEntity.id} />
      </Col>
      <Col md="9">
        <Row>
          <Col md="8">
            <h2 data-cy="employeDetailsHeading">
              <Translate contentKey="gestionDeTachesApp.employe.detail.title">Employe</Translate>
            </h2>
            <dl className="jh-entity-details">
              <dt>
                <span id="nomComplet">
                  <Translate contentKey="gestionDeTachesApp.employe.nomComplet">Nom Complet</Translate>
                </span>
              </dt>
              <dd>{employeEntity.nomComplet}</dd>
              <dt>
                <Translate contentKey="gestionDeTachesApp.employe.compte">Compte</Translate>
              </dt>
              <dd>{employeEntity.compte ? employeEntity.compte.email : ''}</dd>
              <dt>
                <Translate contentKey="gestionDeTachesApp.employe.service">Service</Translate>
              </dt>
              <dd>{employeEntity.service ? employeEntity.service.nomService : ''}</dd>
            </dl>
            <Button tag={Link} to="/employe" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            &nbsp;
            {isAdmin && (
              <Button tag={Link} to={`/employe/${employeEntity.id}/edit`} replace color="primary">
                <FontAwesomeIcon icon="pencil-alt" />{' '}
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.edit">Edit</Translate>
                </span>
              </Button>
            )}
          </Col>
        </Row>
        <h1></h1>
      </Col>
    </Row>
  );
};

export default EmployeDetail;
