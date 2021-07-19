import { IChef } from 'app/shared/model/chef.model';
import { IPole } from 'app/shared/model/pole.model';
import { IIService } from 'app/shared/model/i-service.model';

export interface IDivision {
  id?: number;
  nomDivision?: string;
  chef?: IChef;
  pole?: IPole;
  services?: IIService[] | null;
}

export const defaultValue: Readonly<IDivision> = {};
