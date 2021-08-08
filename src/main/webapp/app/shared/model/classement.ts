import { IEmploye } from 'app/shared/model/employe.model';

export interface IClassement {
  employe: IEmploye;
  nbreTachesTermine: number;
}

export const defaultValue: Readonly<IClassement> = { employe: undefined, nbreTachesTermine: 0 };
