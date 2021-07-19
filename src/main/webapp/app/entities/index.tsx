import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Employe from './employe';
import Chef from './chef';
import Tache from './tache';
import IService from './i-service';
import Division from './division';
import Pole from './pole';
import Direction from './direction';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}employe`} component={Employe} />
      <ErrorBoundaryRoute path={`${match.url}chef`} component={Chef} />
      <ErrorBoundaryRoute path={`${match.url}tache`} component={Tache} />
      <ErrorBoundaryRoute path={`${match.url}i-service`} component={IService} />
      <ErrorBoundaryRoute path={`${match.url}division`} component={Division} />
      <ErrorBoundaryRoute path={`${match.url}pole`} component={Pole} />
      <ErrorBoundaryRoute path={`${match.url}direction`} component={Direction} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
