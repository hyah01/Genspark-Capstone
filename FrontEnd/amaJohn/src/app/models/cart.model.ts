export interface Cart {
    id: string;
    email: string;
    cartOrder: { [productId: string]: number }; // This is the type for the cartOrder
  }