import '../../modules/home/home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Chart } from 'app/entities/tache/chart';
import { IStats } from 'app/shared/model/stats';
import { getStats } from 'app/entities/tache/tache.reducer';
import { Classement } from 'app/entities/employe/classement';
import TacheDeBord from 'app/entities/tache/TacheDeBord';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const Dashboard = () => {
  const dispatch = useAppDispatch();
  const stats: IStats = useAppSelector(state => state.tache.stats);
  const isChefService = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CHEFSERVICE]));
  const isCadre = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.CADRE]));

  return (
    <div>
      ≈
      <Row>
        <Col md="0.5" className="pad"></Col>

        <Col md="3" className="pad">
          <Chart />
        </Col>
        <Col md="1" className="pad"></Col>
        <Col md="6">
          <Classement />
        </Col>
      </Row>
      <Row>
        <Col md="2" className="pad"></Col>
        <TacheDeBord filter={isCadre ? 'NonCommence' : 'Termine'} />
      </Row>
    </div>
  );
};
