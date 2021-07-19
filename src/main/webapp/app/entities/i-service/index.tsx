import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import IService from './i-service';
import IServiceDetail from './i-service-detail';
import IServiceUpdate from './i-service-update';
import IServiceDeleteDialog from './i-service-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IServiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IServiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IServiceDetail} />
      <ErrorBoundaryRoute path={match.url} component={IService} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={IServiceDeleteDialog} />
  </>
);

export default Routes;
