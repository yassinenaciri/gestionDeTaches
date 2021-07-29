import '../../modules/home/home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Chart } from 'app/entities/tache/chart';
import { IStats } from 'app/shared/model/stats';
import { getStats } from 'app/entities/tache/tache.reducer';

export const Dashboard = () => {
  const dispatch = useAppDispatch();
  const stats: IStats = useAppSelector(state => state.tache.stats);

  return (
    <Row>
      <Col md="3" className="pad">
        <Chart />
      </Col>
      <Col md="9">
        <h1></h1>
      </Col>
    </Row>
  );
};
