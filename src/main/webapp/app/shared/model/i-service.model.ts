import { IChef } from 'app/shared/model/chef.model';
import { IDivision } from 'app/shared/model/division.model';
import { IEmploye } from 'app/shared/model/employe.model';

export interface IIService {
  id?: number;
  nomService?: string;
  chef?: IChef;
  division?: IDivision;
  cadres?: IEmploye[] | null;
}

export const defaultValue: Readonly<IIService> = {};
