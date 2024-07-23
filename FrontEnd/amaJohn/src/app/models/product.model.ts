import { Price } from "./price.model";

export interface Product {
    id: string;
    sellerId: string;
    productName: string;
    productDescription: string[];
    category: string[];
    quantity: number;
    price: Price;
    image: string[];
    reviewIds: string[];
    promotionId: string;
  }