import { IChef } from 'app/shared/model/chef.model';
import { IDirection } from 'app/shared/model/direction.model';
import { IDivision } from 'app/shared/model/division.model';

export interface IPole {
  id?: number;
  nom?: string;
  chef?: IChef;
  direction?: IDirection;
  divisions?: IDivision[] | null;
}

export const defaultValue: Readonly<IPole> = {};
