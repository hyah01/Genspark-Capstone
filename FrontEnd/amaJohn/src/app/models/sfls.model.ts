export interface SFLitem {
    productid: string;
    quantity: number;
}

export interface SFLitems {
    id: string;
    items: { [key: string]: SFLitem };
  }
