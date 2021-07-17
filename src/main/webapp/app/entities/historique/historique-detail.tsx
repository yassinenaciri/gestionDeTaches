import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './historique.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const HistoriqueDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const historiqueEntity = useAppSelector(state => state.historique.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historiqueDetailsHeading">
          <Translate contentKey="gestionDeTachesApp.historique.detail.title">Historique</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{historiqueEntity.id}</dd>
          <dt>
            <span id="dateDebut">
              <Translate contentKey="gestionDeTachesApp.historique.dateDebut">Date Debut</Translate>
            </span>
            <UncontrolledTooltip target="dateDebut">
              <Translate contentKey="gestionDeTachesApp.historique.help.dateDebut" />
            </UncontrolledTooltip>
          </dt>
          <dd>
            {historiqueEntity.dateDebut ? <TextFormat value={historiqueEntity.dateDebut} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateFin">
              <Translate contentKey="gestionDeTachesApp.historique.dateFin">Date Fin</Translate>
            </span>
          </dt>
          <dd>{historiqueEntity.dateFin ? <TextFormat value={historiqueEntity.dateFin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.historique.tache">Tache</Translate>
          </dt>
          <dd>{historiqueEntity.tache ? historiqueEntity.tache.nom : ''}</dd>
          <dt>
            <Translate contentKey="gestionDeTachesApp.historique.cadre">Cadre</Translate>
          </dt>
          <dd>{historiqueEntity.cadre ? historiqueEntity.cadre.nomComplet : ''}</dd>
        </dl>
        <Button tag={Link} to="/historique" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/historique/${historiqueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HistoriqueDetail;
