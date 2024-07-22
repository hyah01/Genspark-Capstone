import { Saving } from "./saving.model";

export interface Price {
    amount: number;
    currency: string;
    saving: Saving;
  }