import { Injectable } from '@angular/core';
import { Category } from './category';
import { Characteristic } from './characteristic';
import { Product, ProductCharacteristic, ProductCharacteristicValue, ProductCreateRequest, ProductDiscountPrice } from './product';


@Injectable({
  providedIn: 'root'
})
export class CatalogUtilService {

  constructor() { }

  initializeCategory(): Category {
    return {
      id: '',
      name: '',
      providerCode: '',
      parentId: '',
      parentName: '',
      updatedAt: '',
      categories: [],
      status: '',
      createdAt: '',
      createdBy: ''
    };
  }

  initializeCharacteristic(): Characteristic {
    return {
      id: '',
      name: '',
      updatedAt: '',
      status: '',
      createdAt: ''
    };
  }

  initializeProduct(): Product {
    return {
      id: 0,
      code: '',
      name: '',
      description: '',
      categoryId: '',
      categoryName: '',
      provider:'',
      providerName: '',
      status: '',
      createdBy: '',
      createdAt: '',
      updatedAt: null,
      stock: 0,
      links: [],
      characteristics: [],
      prices: [],
      discounts: []
    };
  }

initializeProductCreateRequest(): ProductCreateRequest {
  return {
    product: this.initializeProduct(),
    pageNumber: 1,
    pageSize: 10
  };
}

initializeProductCharacteristic(): ProductCharacteristic {
  return {
    characteristicId: '',
    characteristicsNames: '',
    characteristicsValues: [],
    type: '',
    value: '',
    id: ''
  };
}

initializeProductCharacteristicValue(): ProductCharacteristicValue {
  return {
    value: '',
    id: '',
    links: []
  };
}

initializeProductDiscountPrice(): ProductDiscountPrice {
  return {
    id: 0,
    locationId: 0,
    location: '',
    value: 0,
    startDate: '',
    endDate: '',
    type: '',
    status: '',
    createdAt: ''
  };
}

}
