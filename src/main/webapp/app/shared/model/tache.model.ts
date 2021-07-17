import { IIService } from 'app/shared/model/i-service.model';
import { IEmploye } from 'app/shared/model/employe.model';
import { IHistorique } from 'app/shared/model/historique.model';

export interface ITache {
  id?: number;
  nom?: string;
  dureEstime?: string;
  description?: string;
  etat?: boolean | null;
  service?: IIService | null;
  cadreAffecte?: IEmploye | null;
  affectations?: IHistorique[] | null;
}

export const defaultValue: Readonly<ITache> = {
  etat: false,
};
