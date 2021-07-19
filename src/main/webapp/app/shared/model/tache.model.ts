import dayjs from 'dayjs';
import { IIService } from 'app/shared/model/i-service.model';
import { IEmploye } from 'app/shared/model/employe.model';
import { Etat } from 'app/shared/model/enumerations/etat.model';

export interface ITache {
  id?: number;
  intitule?: string;
  dateLimite?: string;
  description?: string;
  etat?: Etat | null;
  dateDebut?: string | null;
  dateFin?: string | null;
  service?: IIService | null;
  cadreAffecte?: IEmploye | null;
}

export const defaultValue: Readonly<ITache> = {};
