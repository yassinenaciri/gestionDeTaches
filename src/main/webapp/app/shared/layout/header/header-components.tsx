import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const BrandIcon = props => (
  <div {...props} className="brand-icon ">
    <img src="content/images/logo.jpg" className={'ffffff'} alt="Logo" />
  </div>
);

export const Brand = props => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">Gestion De Taches</span>
    <span className="navbar-version">{appConfig.VERSION}</span>
  </NavbarBrand>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Taches = props => (
  <NavItem>
    <NavLink tag={Link} to="/tache" className="d-flex align-items-center">
      <span>Taches</span>
    </NavLink>
  </NavItem>
);

export const Employes = props => (
  <NavItem>
    <NavLink tag={Link} to="/employe" className="d-flex align-items-center">
      <span>Employés</span>
    </NavLink>
  </NavItem>
);
