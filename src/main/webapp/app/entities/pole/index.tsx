import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pole from './pole';
import PoleDetail from './pole-detail';
import PoleUpdate from './pole-update';
import PoleDeleteDialog from './pole-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PoleDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pole} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PoleDeleteDialog} />
  </>
);

export default Routes;
