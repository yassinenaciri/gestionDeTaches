import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import employe from 'app/entities/employe/employe.reducer';
// prettier-ignore
import chef from 'app/entities/chef/chef.reducer';
// prettier-ignore
import historique from 'app/entities/historique/historique.reducer';
// prettier-ignore
import tache from 'app/entities/tache/tache.reducer';
// prettier-ignore
import iService from 'app/entities/i-service/i-service.reducer';
// prettier-ignore
import division from 'app/entities/division/division.reducer';
// prettier-ignore
import pole from 'app/entities/pole/pole.reducer';
// prettier-ignore
import direction from 'app/entities/direction/direction.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  employe,
  chef,
  historique,
  tache,
  iService,
  division,
  pole,
  direction,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
