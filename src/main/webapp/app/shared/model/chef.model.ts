import { IUser } from 'app/shared/model/user.model';
import { IIService } from 'app/shared/model/i-service.model';
import { IPole } from 'app/shared/model/pole.model';
import { IDirection } from 'app/shared/model/direction.model';
import { IDivision } from 'app/shared/model/division.model';

export interface IChef {
  id?: number;
  nomComplet?: string;
  role?: string | null;
  compte?: IUser | null;
  service?: IIService | null;
  pole?: IPole | null;
  direction?: IDirection | null;
  division?: IDivision | null;
}

export const defaultValue: Readonly<IChef> = {};
