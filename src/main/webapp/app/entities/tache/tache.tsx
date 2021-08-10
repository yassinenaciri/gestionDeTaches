import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { filtrer, getEntities, updateEtat } from './tache.reducer';
import { ITache } from 'app/shared/model/tache.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Etat } from 'app/shared/model/enumerations/etat.model';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const Tache = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const tacheList = useAppSelector(state => state.tache.entities);

  const loading = useAppSelector(state => state.tache.loading);
  const totalItems = useAppSelector(state => state.tache.totalItems);
  let filter = useAppSelector(state => state.tache.links);
  const isChefService = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CHEFSERVICE]));
  const isCadre = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CADRE]));
  const _MS_PER_DAY = 1000 * 60 * 60 * 24;
  const _MS_PER_HOUR = 1000 * 60 * 60;
  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        query: filter,
      })
    );
  };
  function dateDiffInDays(a, b) {
    // eslint-disable-next-line no-console
    console.log(a);
    // Discard the time and time-zone information.
    const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
    const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

    // eslint-disable-next-line no-console
    console.log(utc2 - utc1);
    const result = (utc2 - utc1) / _MS_PER_DAY;
    if (result > +0) {
      return result;
    } else {
      return -result;
    }
  }

  function enRetard(a, b) {
    // eslint-disable-next-line no-console
    console.log(a);
    // Discard the time and time-zone information.
    const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate(), a.getHours());
    const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate(), b.getHours());

    return utc2 - utc1 < 0;
  }

  function dateDiffInHours(a, b) {
    // eslint-disable-next-line no-console
    console.log(a);
    // Discard the time and time-zone information.
    const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate(), a.getHours());
    const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate(), b.getHours());

    // eslint-disable-next-line no-console
    console.log(utc2 - utc1);
    const reste = (utc2 - utc1) % _MS_PER_DAY;

    const result = reste / _MS_PER_HOUR;
    if (result >= 0) {
      return result;
    } else {
      return -result;
    }
  }

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };
  const filtrerParEtat = etat => {
    filter = etat;
    dispatch(filtrer(etat));
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        query: etat,
      })
    );
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const modifierEtat = async (Id, NouveauEtat) => {
    await dispatch(
      updateEtat({
        id: Id,
        nouveauEtat: NouveauEtat,
      })
    );
    handleSyncList();
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="tache-heading" data-cy="TacheHeading">
        <Translate contentKey="gestionDeTachesApp.tache.home.title">Taches</Translate>
        <div className="d-flex justify-content-end">
          <div className="filter-group">
            <select id="filtreEtat" className="form-control" onChange={event => filtrerParEtat(event.target.value)} defaultValue={filter}>
              <option value="NonCommence">Non Commencé</option>
              <option value="Encours">En cours</option>
              <option value="Termine">Terminé</option>
              <option value="Abondonne">Abondonné</option>
              <option value="Valide">Validé</option>
              <option value="Refuse">Refusé</option>
            </select>
          </div>
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gestionDeTachesApp.tache.home.refreshListLabel">Refresh List</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {tacheList && tacheList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('intitule')}>
                  <Translate contentKey="gestionDeTachesApp.tache.intitule">Intitule</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('dateLimite')}>
                  <Translate contentKey="gestionDeTachesApp.tache.dateLimite">Date Limite</Translate> <FontAwesomeIcon icon="sort" />
                </th>

                <th className="hand" onClick={sort('etat')}>
                  <Translate contentKey="gestionDeTachesApp.tache.etat">Etat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {filter !== 'NonCommence' && (
                  <th className="hand" onClick={sort('dateDebut')}>
                    <Translate contentKey="gestionDeTachesApp.tache.dateDebut">Date Debut</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                )}
                {filter !== 'Encours' && filter !== 'NonCommence' && filter !== 'Abondonne' && (
                  <th className="hand" onClick={sort('dateFin')}>
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
                {filter !== 'Encours' && filter !== 'NonCommence' && filter !== 'Abondonne' && <th>Ecart</th>}
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
                  {filter !== 'NonCommence' && (
                    <td>{tache.dateDebut ? <TextFormat type="date" value={tache.dateDebut} format={APP_DATE_FORMAT} /> : null}</td>
                  )}
                  {filter !== 'Encours' && filter !== 'NonCommence' && filter !== 'Abondonne' && (
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
                  {filter !== 'Encours' && filter !== 'NonCommence' && filter !== 'Abondonne' && (
                    <td>
                      {enRetard(new Date(tache.dateFin), new Date(tache.dateLimite)) ? 'Retard de ' : 'En avance de'}{' '}
                      {dateDiffInDays(new Date(tache.dateFin), new Date(tache.dateLimite))} jrs{' '}
                      {dateDiffInHours(new Date(tache.dateFin), new Date(tache.dateLimite))} hrs
                    </td>
                  )}
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${tache.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      {isChefService && filter === 'NonCommence' && (
                        <Button
                          tag={Link}
                          to={`${match.url}/${tache.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                      )}
                      {isChefService && filter === 'NonCommence' && (
                        <Button
                          tag={Link}
                          to={`${match.url}/${tache.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      )}

                      {isCadre && filter === 'NonCommence' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Encours')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Commencer</span>
                        </Button>
                      )}
                      {isCadre && filter === 'Encours' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Termine')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Terminer</span>
                        </Button>
                      )}
                      {isCadre && filter === 'Encours' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Abondonne')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Abondonner</span>
                        </Button>
                      )}
                      {isChefService && filter === 'Termine' && (
                        <Button color="primary" size="sm" onClick={() => modifierEtat(tache.id, 'Valide')}>
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Valider</span>
                        </Button>
                      )}
                      {isChefService && filter === 'Termine' && (
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
      {totalItems ? (
        <div className={tacheList && tacheList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Tache;
