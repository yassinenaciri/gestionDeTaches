import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Direction from './direction';
import DirectionDetail from './direction-detail';
import DirectionUpdate from './direction-update';
import DirectionDeleteDialog from './direction-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DirectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DirectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DirectionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Direction} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DirectionDeleteDialog} />
  </>
);

export default Routes;
