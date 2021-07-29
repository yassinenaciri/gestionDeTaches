export interface IStats {
  nonCommence?: number;
  encours?: number;
  termine?: number;
  abondonne?: number;
  valide?: number;
  refuse?: number;
}

export const defaultValue: Readonly<IStats> = {};
