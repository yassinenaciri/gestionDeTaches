import { IUser } from 'app/shared/model/user.model';
import { IIService } from 'app/shared/model/i-service.model';
import { ITache } from 'app/shared/model/tache.model';
import { IHistorique } from 'app/shared/model/historique.model';

export interface IEmploye {
  id?: number;
  nomComplet?: string;
  compte?: IUser | null;
  service?: IIService | null;
  tachesAffectes?: ITache[] | null;
  affectations?: IHistorique[] | null;
}

export const defaultValue: Readonly<IEmploye> = {};
