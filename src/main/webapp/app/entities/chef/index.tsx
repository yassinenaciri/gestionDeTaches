import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Chef from './chef';
import ChefDetail from './chef-detail';
import ChefUpdate from './chef-update';
import ChefDeleteDialog from './chef-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ChefUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ChefUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ChefDetail} />
      <ErrorBoundaryRoute path={match.url} component={Chef} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ChefDeleteDialog} />
  </>
);

export default Routes;
