import { CartItem } from "./cartitem.model";

export interface CartItems {
    id: string;
    items: { [key: string]: CartItem };
  }