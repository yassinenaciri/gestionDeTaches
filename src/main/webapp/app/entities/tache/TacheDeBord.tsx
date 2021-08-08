import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { filtrer, getALlEntities, updateEtat } from './tache.reducer';
import { ITache } from 'app/shared/model/tache.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Etat } from 'app/shared/model/enumerations/etat.model';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const TacheDeBord = props => {
  const dispatch = useAppDispatch();
  const tacheList = useAppSelector(state => state.tache.allEntities);
  const loading = useAppSelector(state => state.tache.loading);
  const isChefService = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CHEFSERVICE]));
  const isCadre = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CADRE]));

  const getEntities = () => {
    dispatch(getALlEntities(props.filter));
  };

  useEffect(() => {
    getEntities();
  }, []);

  const _MS_PER_DAY = 1000 * 60 * 60 * 24;

  function dateDiffInDays(a, b) {
    // eslint-disable-next-line no-console
    console.log(a);
    // Discard the time and time-zone information.
    const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
    const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

    // eslint-disable-next-line no-console
    console.log(utc2 - utc1);

    return (utc2 - utc1) / _MS_PER_DAY;
  }

  const modifierEtat = async (Id, NouveauEtat) => {
    await dispatch(
      updateEtat({
        id: Id,
        nouveauEtat: NouveauEtat,
      })
    );
    getEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="tache-heading" data-cy="TacheHeading">
        <Translate contentKey="gestionDeTachesApp.tache.home.title">Taches</Translate>
      </h2>
      <div className="table-responsive">
        {tacheList && tacheList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand">
                  <Translate contentKey="gestionDeTachesApp.tache.intitule">Intitule</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand">
                  <Translate contentKey="gestionDeTachesApp.tache.dateLimite">Date Limite</Translate> <FontAwesomeIcon icon="sort" />
                </th>

                <th className="hand">
                  <Translate contentKey="gestionDeTachesApp.tache.etat">Etat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {props.filter !== 'NonCommence' && (
                  <th className="hand">
                    <Translate contentKey="gestionDeTachesApp.tache.dateDebut">Date Debut</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                )}
                {props.filter !== 'Encours' && props.filter !== 'NonCommence' && props.filter !== 'Abondonne' && (
                  <th className="hand">
                    <Translate contentKey="gestionDeTachesApp.tache.dateFin">Date Fin</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                )}
                {!isChefService && !isCadre && (
                  <th>
                    <Translate contentKey="gestionDeTachesApp.tache.service">Service</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                )}
                {!isCadre && (
                  <th>
                    <Translate contentKey="gestionDeTachesApp.tache.cadreAffecte">Cadre Affecte</Translate>
                    <FontAwesomeIcon icon="sort" />
                  </th>
                )}
                {props.filter !== 'Encours' && props.filter !== 'NonCommence' && props.filter !== 'Abondonne' && <th>Ecart</th>}
                <th />
              </tr>
            </thead>
            <tbody>
              {tacheList.map((tache, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{tache.intitule}</td>
                  <td>{tache.dateLimite ? <TextFormat type="date" value={tache.dateLimite} format={APP_DATE_FORMAT} /> : null}</td>

                  <td>
                    <Translate contentKey={`gestionDeTachesApp.Etat.${tache.etat}`} />
                  </td>
                  {props.filter !== 'NonCommence' && (
                    <td>{tache.dateDebut ? <TextFormat type="date" value={tache.dateDebut} format={APP_DATE_FORMAT} /> : null}</td>
                  )}
                  {props.filter !== 'Encours' && props.filter !== 'NonCommence' && props.filter !== 'Abondonne' && (
                    <td>{tache.dateFin ? <TextFormat type="date" value={tache.dateFin} format={APP_DATE_FORMAT} /> : null}</td>
                  )}
                  {!isCadre && !isChefService && (
                    <td>{tache.service ? <Link to={`i-service/${tache.service.id}`}>{tache.service.nomService}</Link> : ''}</td>
                  )}
                  {!isCadre && (
                    <td>
                      {tache.cadreAffecte ? <Link to={`employe/${tache.cadreAffecte.id}`}>{tache.cadreAffecte.nomComplet}</Link> : ''}
                    </td>
                  )}
                  {props.filter !== 'Encours' && props.filter !== 'NonCommence' && props.filter !== 'Abondonne' && (
                    <td>{dateDiffInDays(new Date(tache.dateFin), new Date(tache.dateLimite))}</td>
                  )}

                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`taches/${tache.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>

                      {isCadre && props.filter === 'NonCommence' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Encours')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Commencer</span>
                        </Button>
                      )}
                      {isCadre && props.filter === 'Encours' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Termine')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Terminer</span>
                        </Button>
                      )}
                      {isCadre && props.filter === 'Encours' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Abondonne')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Abondonner</span>
                        </Button>
                      )}
                      {isChefService && props.filter === 'Termine' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Valide')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Valider</span>
                        </Button>
                      )}
                      {isChefService && props.filter === 'Termine' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Refuse')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Refuser</span>
                        </Button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gestionDeTachesApp.tache.home.notFound">No Taches found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TacheDeBord;
