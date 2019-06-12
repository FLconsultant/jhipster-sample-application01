import { Moment } from 'moment';

export interface IMeasure {
  id?: number;
  measuresource?: string;
  measurevalue?: number;
  measurevaluelong?: number;
  measuredate?: Moment;
  measuredatetime?: Moment;
}

export class Measure implements IMeasure {
  constructor(
    public id?: number,
    public measuresource?: string,
    public measurevalue?: number,
    public measurevaluelong?: number,
    public measuredate?: Moment,
    public measuredatetime?: Moment
  ) {}
}
