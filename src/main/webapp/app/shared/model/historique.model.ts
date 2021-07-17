import dayjs from 'dayjs';
import { ITache } from 'app/shared/model/tache.model';
import { IEmploye } from 'app/shared/model/employe.model';

export interface IHistorique {
  id?: number;
  dateDebut?: string | null;
  dateFin?: string | null;
  tache?: ITache;
  cadre?: IEmploye;
}

export const defaultValue: Readonly<IHistorique> = {};
