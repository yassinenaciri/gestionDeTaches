import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IStats } from 'app/shared/model/stats';
import { getClassement } from 'app/entities/employe/employe.reducer';
import { IClassement } from 'app/shared/model/classement';
import { JhiItemCount, JhiPagination, Translate } from 'react-jhipster';
import { Button, Row, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';

export const Classement = () => {
  const dispatch = useAppDispatch();
  const classement: IClassement[] = useAppSelector(state => state.employe.classement);
  const loading = useAppSelector(state => state.employe.loading);
  useEffect(() => {
    // eslint-disable-next-line no-console
    dispatch(getClassement()).then(i => console.log(classement));
  }, []);

  return (
    <div>
      <h2 id="employe-heading" data-cy="EmployeHeading">
        Classement des Employés
        <div className="d-flex justify-content-end"></div>
      </h2>
      <div className="table-responsive">
        {classement && classement.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Classement</th>
                <th className="hand">
                  <Translate contentKey="gestionDeTachesApp.employe.nomComplet">Nom Complet</Translate> <FontAwesomeIcon icon="sort" />
                </th>

                <th>Nombre De Taches Terminé</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {classement.map((employeClasse, i) => {
                if (employeClasse !== null) {
                  return (
                    <tr key={`entity-${i}`} data-cy="entityTable">
                      <td>{i + 1}</td>
                      <td>{employeClasse.employe.nomComplet}</td>
                      <td>{employeClasse.nbreTachesTermine}</td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button
                            tag={Link}
                            to={`/employe/${employeClasse.employe.id}`}
                            color="info"
                            size="sm"
                            data-cy="entityDetailsButton"
                          >
                            <FontAwesomeIcon icon="eye" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.view">View</Translate>
                            </span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  );
                } else {
                  return;
                }
              })}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gestionDeTachesApp.employe.home.notFound">No Employes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};
