export interface Cart {
    id: string;
    email: string;
    cartOrder: Map<string, number>;
  }