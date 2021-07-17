import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/employe">
      <Translate contentKey="global.menu.entities.employe" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/chef">
      <Translate contentKey="global.menu.entities.chef" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/historique">
      <Translate contentKey="global.menu.entities.historique" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/tache">
      <Translate contentKey="global.menu.entities.tache" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/i-service">
      <Translate contentKey="global.menu.entities.iService" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/division">
      <Translate contentKey="global.menu.entities.division" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/pole">
      <Translate contentKey="global.menu.entities.pole" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/direction">
      <Translate contentKey="global.menu.entities.direction" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
