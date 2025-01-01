import { orderHistoryProducts } from "./orderHistoryProducts.model";

export interface OrderHistory {
    id: string; 
    userId: string;             
    products: orderHistoryProducts;
    orderDate: Date;         
    estimatedDate: Date;       
  }