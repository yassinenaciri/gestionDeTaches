import { IUser } from 'app/shared/model/user.model';
import { IIService } from 'app/shared/model/i-service.model';
import { ITache } from 'app/shared/model/tache.model';

export interface IEmploye {
  id?: number;
  nomComplet?: string;
  compte?: IUser | null;
  service?: IIService;
  tachesAffectes?: ITache[] | null;
}

export const defaultValue: Readonly<IEmploye> = {};
