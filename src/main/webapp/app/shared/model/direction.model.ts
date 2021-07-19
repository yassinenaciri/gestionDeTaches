import { IChef } from 'app/shared/model/chef.model';
import { IPole } from 'app/shared/model/pole.model';

export interface IDirection {
  id?: number;
  nomDirection?: string;
  directeur?: IChef;
  poles?: IPole[] | null;
}

export const defaultValue: Readonly<IDirection> = {};
