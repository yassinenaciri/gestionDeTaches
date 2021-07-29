import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEtat } from './tache.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { Etat } from 'app/shared/model/enumerations/etat.model';
import { Chart } from 'app/entities/tache/chart';
//

export const TacheDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const isChefService = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CHEFSERVICE]));
  const isCadre = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CADRE]));

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);
  const modifierEtat = async (Id, NouveauEtat) => {
    await dispatch(
      updateEtat({
        id: Id,
        nouveauEtat: NouveauEtat,
      })
    );
  };

  const tacheEntity = useAppSelector(state => state.tache.entity);

  const filter = Etat[tacheEntity.etat];
  // eslint-disable-next-line no-console
  console.log(filter);
  return (
    <Row>
      <Col md="3" className="pad"></Col>
      <Col md="9">
        <Row>
          <Col md="8">
            <h2 data-cy="tacheDetailsHeading">
              <Translate contentKey="gestionDeTachesApp.tache.detail.title">Tache</Translate>
            </h2>
            <dl className="jh-entity-details">
              <dt>
                <span id="intitule">Intitule</span>
              </dt>
              <dd>{tacheEntity.intitule}</dd>
              <dt>
                <span id="dateLimite">
                  <Translate contentKey="gestionDeTachesApp.tache.dateLimite">Date Limite</Translate>
                </span>
              </dt>
              <dd>{tacheEntity.dateLimite ? <TextFormat value={tacheEntity.dateLimite} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
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
              <dd>
                <Translate contentKey={`gestionDeTachesApp.Etat.${tacheEntity.etat}`} />
              </dd>
              <dt>
                <span id="dateDebut">
                  <Translate contentKey="gestionDeTachesApp.tache.dateDebut">Date Debut</Translate>
                </span>
              </dt>
              <dd>{tacheEntity.dateDebut ? <TextFormat value={tacheEntity.dateDebut} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
              <dt>
                <span id="dateFin">
                  <Translate contentKey="gestionDeTachesApp.tache.dateFin">Date Fin</Translate>
                </span>
              </dt>
              <dd>{tacheEntity.dateFin ? <TextFormat value={tacheEntity.dateFin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
              <dt>
                <Translate contentKey="gestionDeTachesApp.tache.service">Service</Translate>
              </dt>
              <dd>{tacheEntity.service ? tacheEntity.service.nomService : ''}</dd>
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
            {isCadre && filter === Etat.NonCommence && (
              <Button color="primary" size="sm" onClick={() => modifierEtat(tacheEntity.id, 'Encours')}>
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Commencer</span>
              </Button>
            )}
            {isCadre && filter === Etat.Encours && (
              <Button color="primary" size="sm" onClick={() => modifierEtat(tacheEntity.id, 'Termine')}>
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Terminer</span>
              </Button>
            )}
            {isCadre && filter === Etat.Encours && (
              <Button color="primary" size="sm" onClick={() => modifierEtat(tacheEntity.id, 'Abondonne')}>
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Abondonner</span>
              </Button>
            )}
            {isChefService && filter === Etat.Termine && (
              <Button color="primary" size="sm" onClick={() => modifierEtat(tacheEntity.id, 'Valide')}>
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Valider</span>
              </Button>
            )}
            {isChefService && filter === Etat.Termine && (
              <Button color="primary" size="sm" onClick={() => modifierEtat(tacheEntity.id, 'Refuse')}>
                <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Refuser</span>
              </Button>
            )}
          </Col>
        </Row>
        <h1></h1>
      </Col>
    </Row>
  );
};

export default TacheDetail;
