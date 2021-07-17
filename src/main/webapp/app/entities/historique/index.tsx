import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Historique from './historique';
import HistoriqueDetail from './historique-detail';
import HistoriqueUpdate from './historique-update';
import HistoriqueDeleteDialog from './historique-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HistoriqueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HistoriqueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HistoriqueDetail} />
      <ErrorBoundaryRoute path={match.url} component={Historique} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HistoriqueDeleteDialog} />
  </>
);

export default Routes;
