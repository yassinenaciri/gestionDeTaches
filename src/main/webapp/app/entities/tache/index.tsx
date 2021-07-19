import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tache from './tache';
import TacheDetail from './tache-detail';
import TacheUpdate from './tache-update';
import TacheDeleteDialog from './tache-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TacheUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TacheUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TacheDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tache} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TacheDeleteDialog} />
  </>
);

export default Routes;
