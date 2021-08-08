import './home.scss';

import React, { useEffect } from 'react';
import { Link, Redirect } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import { Chart } from 'app/entities/tache/chart';
import { Dashboard } from 'app/entities/tache/dashboard';

export const Home = () => {
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const account = useAppSelector(state => state.authentication.account);
  if (isAuthenticated) {
    return (
      <div>
        <Dashboard />
      </div>
    );
  } else {
    // eslint-disable-next-line react/jsx-no-undef
    return <Redirect to="/login" />;
  }
};

export default Home;
