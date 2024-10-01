import {PersonDTO} from "./personDTO";

export class DeviceDTO{
  id!: string;
  description!: string;
  maxHourlyEnergyConsumption !: number;
  personDTO!: PersonDTO;

}
